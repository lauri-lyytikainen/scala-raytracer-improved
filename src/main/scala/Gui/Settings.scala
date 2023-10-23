package Gui

import RayMath.Vector3D
import scalafx.scene.paint.Color

object Settings:
  val WINDOW_WIDTH = 1920
  val WINDOW_HEIGHT = 1080
  val VIEWPORT_WIDTH: Int = 1920 - 16
  val VIEWPORT_HEIGHT: Int = 1080 - 39
  val TARGET_FPS = 100
  val VIEWPORT_PIXEL_SIZE = 4

  val RENDERING_CHUNKS = 1
  val RENDERING_THREADS = 1

  val MAX_BOUNCE_LIMIT = 3
  val RAYS_PER_PIXEL = 3

  val IMAGE_SAMPLES = 10

  val SHOW_NORMALS = false

  val IMAGE_SMOOTHING = false

  val BACKGROUND_COLOR = Vector3D(0.8, 0.8, 0.922)

