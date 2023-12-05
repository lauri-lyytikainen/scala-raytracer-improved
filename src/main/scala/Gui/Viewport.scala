package Gui

import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.paint.Color
import Gui.Settings
import RayMath.{RotationMatrix3D, Vector3D}
import Rendering.{Material, Renderer, World}
import Solids.{Model, Sphere}
object Viewport extends JFXApp3:
  private val windowWidth = Settings.WINDOW_WIDTH
  private val windowHeight = Settings.WINDOW_HEIGHT

  private var currentFrame = 0

  val world = new World()
  world.camera.focalDistance = 10
  world.camera.dofBlurriness = 0

  val greenMaterial = new Material(Vector3D(0.125, 0.900, 0.125), Vector3D(0, 0, 0), 0, 0.3, 0.3, Vector3D(1, 1, 1))
  val redMaterial = new Material(Vector3D(0.9, 0.125, 0.125), Vector3D(0, 0, 0), 0, 0.3, 0.3, Vector3D(1, 1, 1))
  val blueMaterial = new Material(Vector3D(0.125, 0.125, 0.9), Vector3D(0, 0, 0), 0, 0.3, 0.3, Vector3D(1, 1, 1))
  val lightMaterial = new Material(Vector3D(0, 0, 0), Vector3D(0.9, 0.8, 0.6), 5)

  val whiteMaterial = new Material(Vector3D(0.9,0.9,0.9), Vector3D(0, 0, 0), 0, 0.4, 0.4, Vector3D(1, 1, 1))
  val mirrorMaterial = new Material(Vector3D(0.9,0.9,0.9), Vector3D(0, 0, 0), 0.9, 0.9, 0.9, Vector3D(1, 1, 1))


//  world.addSolid(new Sphere(Vector3D(0,0,5),1, greenMaterial))
  world.addSolid(new Sphere(Vector3D(3, 5, 0), 1, lightMaterial))

  val floor = new Model(
    "plane.obj",
    Vector3D(0, -1, 10),
    RotationMatrix3D(0, 0, 0),
    20,
    whiteMaterial,
  )

  world.addSolid(floor)

  val redBall = new Sphere(
    Vector3D(-2, 0, 10),
    1,
    redMaterial
  )
  world.addSolid(redBall)

  val greenBall = new Sphere(
    Vector3D(0, 0, 10),
    1,
    greenMaterial
  )
  world.addSolid(greenBall)

  val blueBall = new Sphere(
    Vector3D(2, 0, 10),
    1,
    blueMaterial
  )
  world.addSolid(blueBall)

  Renderer.world = Some(world)


  private def tick(gc: GraphicsContext): Unit =
    Renderer.render(gc, currentFrame)
    currentFrame += 1

  override def start(): Unit =
    var gc: GraphicsContext = null
    val oneSec = 1_000_000_000L
    var lastTick = 0L
    val timer: AnimationTimer = AnimationTimer(now => {
      if lastTick == 0L || (now - lastTick > oneSec / Settings.TARGET_FPS) then {
        lastTick = now
        if currentFrame < Settings.IMAGE_SAMPLES then
          tick(gc)
      }
    })

    stage = new PrimaryStage {
      title = "Scala Raytracer Improved"
      width = windowWidth
      height = windowHeight
      scene = new Scene:
        val rootPane = new BorderPane

        rootPane.center = new HBox(1) {
          children = List(
            new Canvas {
              width = Settings.VIEWPORT_WIDTH
              height = Settings.VIEWPORT_HEIGHT
              gc = this.graphicsContext2D
            }
          )
        }
        root = rootPane
    }
    stage.setWidth(Settings.WINDOW_WIDTH)
    stage.setHeight(Settings.WINDOW_HEIGHT)
    timer.start()
    gc.fill = Color.Black
    gc.fillRect(0, 0, Settings.VIEWPORT_WIDTH, Settings.VIEWPORT_HEIGHT)
