package Gui

import RayMath.Vector3D

object Settings:
  val WINDOW_WIDTH = 1280
  val WINDOW_HEIGHT = 720
  val VIEWPORT_WIDTH: Int = 1280 - 16
  val VIEWPORT_HEIGHT: Int = 720 - 39
  val TARGET_FPS = 100
  val VIEWPORT_PIXEL_SIZE = 4

  val RENDERING_CHUNKS = 8
  val RENDERING_THREADS = 8

  val MAX_BOUNCE_LIMIT = 4
  val RAYS_PER_PIXEL = 5

  val IMAGE_SAMPLES = 10

  val SHOW_NORMALS = false

  val IMAGE_SMOOTHING = false

  val BACKGROUND_COLOR: Vector3D = Vector3D(0.8, 0.8, 0.922)

