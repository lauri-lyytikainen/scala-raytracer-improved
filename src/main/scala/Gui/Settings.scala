package Gui

import RayMath.Vector3D
import scalafx.scene.paint.Color

object Settings:
  val WINDOW_WIDTH = 1920
  val WINDOW_HEIGHT = 1080
  val VIEWPORT_WIDTH: Int = 1920 - 16
  val VIEWPORT_HEIGHT: Int = 1080 - 39
  val TARGET_FPS = 60
  val VIEWPORT_PIXEL_SIZE = 2

  val RENDERING_CHUNKS = 8
  val RENDERING_THREADS = 4

  val MAX_BOUNCE_LIMIT = 4
  val RAYS_PER_PIXEL = 3

  val IMAGE_SAMPLES = 10

  val BACKGROUND_COLOR = Vector3D(0.8, 0.8, 0.922)

