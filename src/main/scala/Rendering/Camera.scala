package Rendering

import Gui.Settings
import RayMath.{Ray, RotationMatrix3D, Vector3D}

/**
 * Holds the information about the camera
 * @param initialPos The initial position of the camera
 * @param fov The field of view of the camera
 */

class Camera(initialPos: Vector3D, private val fov: Int):
  var pos   = initialPos.copy
  var pitch: Double = 0
  var yaw: Double   = 0
  var focalDistance = 1.0
  var dofBlurriness = 1.0

  private val canvasWidth = Settings.VIEWPORT_WIDTH / Settings.VIEWPORT_PIXEL_SIZE
  private val canvasHeight = Settings.VIEWPORT_HEIGHT / Settings.VIEWPORT_PIXEL_SIZE
  private val aspectRatio = (canvasHeight.toFloat / canvasWidth.toFloat)


  def rotationMatrix: RotationMatrix3D = RotationMatrix3D(pitch.toRadians, yaw.toRadians, 0)


  def getRay(screenX: Int, screenY: Int): Ray =
    val rm = RotationMatrix3D(this.pitch.toRadians,this.yaw.toRadians,0)

    val aspectRatio = (canvasHeight.toFloat / canvasWidth.toFloat)
    // Use the aspectratio and the fov to calculate vectors that point to the top left corner of the viewport

    val sideLength = Math.sin(Math.PI / 2) * focalDistance / Math.sin((Math.PI - this.fov) / 2)
    val leftEdge = RotationMatrix3D(0,fov/2,0) * Vector3D(0,0,sideLength)
    var topEdge = RotationMatrix3D(fov/2,0,0) * Vector3D(0,0,sideLength)
    topEdge = Vector3D(topEdge.x, topEdge.y * aspectRatio, topEdge.z)

    val topLeft = leftEdge + Vector3D(0.0, topEdge.y, 0.0)
    val topRight = Vector3D(-topLeft.x, topLeft.y, topLeft.z)
    val bottomLeft = Vector3D(topLeft.x, -topLeft.y, topLeft.z)

    val pX = screenX / canvasWidth.toDouble
    val pY = screenY / canvasHeight.toDouble

    var point = rm * (topLeft + (topRight - topLeft) * pX + (bottomLeft - topLeft) * pY)

    point = point + Renderer.randomDirection() * 0.01


    val origin = pos + Renderer.randomDirection() * dofBlurriness

    val dir = (point - origin).normalize

    val ray = Ray(origin, dir)
    ray
