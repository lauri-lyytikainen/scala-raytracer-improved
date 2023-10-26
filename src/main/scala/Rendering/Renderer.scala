package Rendering

import scalafx.scene.canvas.GraphicsContext

import java.awt.image.BufferedImage
import scala.collection.mutable.Stack
import Gui.Settings
import RayMath.{Ray, Vector3D}
import scalafx.embed.swing.SwingFXUtils

import scala.collection.mutable
object Renderer:

  var world: Option[World] = None

  def render(gc: GraphicsContext, currentFrame: Int): Unit =
    //Time the rendering

    val startTime = System.nanoTime()

    val w = Settings.VIEWPORT_WIDTH / Settings.VIEWPORT_PIXEL_SIZE
    val h = Settings.VIEWPORT_HEIGHT / Settings.VIEWPORT_PIXEL_SIZE

    val chunkWidth = w / Settings.RENDERING_CHUNKS
    val chunkHeight = h / Settings.RENDERING_CHUNKS

    //create a buffered image to store the pixels
    val bufferedImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB)

    class RenderThread(startX: Int, startY: Int, w: Int, h: Int, frameCount: Int) extends Thread:
      override def run(): Unit =
        renderChunk(bufferedImage, startX, startY, w, h, frameCount)

    // Make a stack of regions to render
    val regions = mutable.Stack[Array[Int]]()

    for
      startX <- 0 until Settings.RENDERING_CHUNKS
      startY <- 0 until Settings.RENDERING_CHUNKS
    do regions.push(Array(startX * chunkWidth, startY * chunkHeight, chunkWidth, chunkHeight))

    //Make a list of threads
    var threads = mutable.ListBuffer[RenderThread]()

    while regions.nonEmpty do
      if threads.size < Settings.RENDERING_THREADS then
        val region = regions.pop()
        val startX = region(0)
        val startY = region(1)
        val rw = region(2)
        val rh = region(3)
        val thread = new RenderThread(startX, startY, rw, rh, currentFrame)
        threads.addOne(thread)
        thread.start()

      // loop through all threads and remove the ones that are finished
      threads = threads.filter(thread => thread.isAlive)
    // Wait for all threads to finish
    threads.foreach(_.join())

    //draw the buffered image
    gc.setGlobalAlpha(1 - Math.log(currentFrame) / Math.log(Settings.IMAGE_SAMPLES))
    gc.setImageSmoothing(Settings.IMAGE_SMOOTHING)
    gc.drawImage(SwingFXUtils.toFXImage(bufferedImage, null), 0, 0, Settings.VIEWPORT_WIDTH, Settings.VIEWPORT_HEIGHT)


    val endTime = System.nanoTime()
    val timeTaken = (endTime - startTime) / 1000000.0
    println(s"Frame $currentFrame took $timeTaken ms")

  private def renderChunk(bufferedImage: BufferedImage, startX: Int, startY: Int, w: Int, h: Int, frameCount: Int): Unit =
    val coords: Unit = for
      x <- startX until startX + w
      y <- startY until startY + h
    do
      val pxCol = averagePixelColor(x, y).clamp(0, 1)

      // Convert the color to an int using bit shifting in BRGA format
      val intColor = (pxCol.x * 255).toInt << 16 | (pxCol.y * 255).toInt << 8 | (pxCol.z * 255).toInt << 0 | 0xFF << 24

      bufferedImage.setRGB(x, y, intColor)

  private def averagePixelColor(x: Int, y: Int): Vector3D =
    // Repeat for Settings.RAYS_PER_PIXEL times
    // Calculate the color for each ray
    // Add the color to the total color
    // Divide the total color by the amount of rays

    var totalColor = Vector3D(0,0,0)
    var limit = Settings.RAYS_PER_PIXEL
    if Settings.SHOW_NORMALS then
      limit = 1
    for i <- 0 until limit do
      totalColor = totalColor + calculatePixelColor(x, y)
    totalColor / limit

  private def calculatePixelColor(x: Int, y: Int): Vector3D =
    val ray = world.get.camera.getRay(x, y)

    var incomingLight = Vector3D(0,0,0)
    var rayColor = Vector3D(1,1,1)

    var maxBounces = Settings.MAX_BOUNCE_LIMIT
    var bounce = 0
    while bounce < maxBounces do
      val rayHit = world.get.traceRay(ray)
      if rayHit.isDefined then

        if Settings.SHOW_NORMALS then
          val norm = rayHit.get.normal * 0.5 + Vector3D(0.5, 0.5, 0.5)
          return norm

        ray.origin = rayHit.get.hitPos + rayHit.get.normal * 0.000001
        val diffuseDir = rayHit.get.normal + randomDiffuseDirection(rayHit.get.normal)
        val specularDir = ray.direction.reflect(rayHit.get.normal)

        val material = rayHit.get.solid.material

        var isSpecularBounce = 0
        if material.specularProbability > Math.random() then
          isSpecularBounce = 1

        ray.direction = diffuseDir.lerp(specularDir, material.smoothness * isSpecularBounce).normalize

        val emittedLight = material.emissionColor * material.emissionStrength
        incomingLight = incomingLight + rayColor * emittedLight
        rayColor = rayColor * material.color.lerp(material.specularColor, isSpecularBounce)
        bounce += 1
      else
        maxBounces = -1
        incomingLight = rayColor * getEnvironmentColor(ray) + incomingLight
    incomingLight


  private def getEnvironmentColor(ray: Ray): Vector3D =
    Settings.BACKGROUND_COLOR

  private def randomDiffuseDirection(normal: Vector3D): Vector3D =
    val randDir = randomDirection()
    if randDir.dot(normal) < 0 then
      randDir * -1
    else
      randDir
  def randomDirection(): Vector3D =
    val x = randomValueNormalDistributed()
    val y = randomValueNormalDistributed()
    val z = randomValueNormalDistributed()
    Vector3D(x, y, z).normalize
  private def randomValueNormalDistributed(): Double =
    val r1 = Math.random()
    val r2 = Math.random()
    Math.sqrt(-2 * Math.log(r1)) * Math.cos(2 * Math.PI * r2)
