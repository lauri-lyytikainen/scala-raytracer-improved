package Gui

import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.canvas.{Canvas, GraphicsContext}
import scalafx.scene.layout.{BorderPane, HBox}
import scalafx.scene.paint.Color

import Gui.Settings
import Rendering.Renderer
object Viewport extends JFXApp3:
  private val windowWidth = Settings.WINDOW_WIDTH
  private val windowHeight = Settings.WINDOW_HEIGHT

  private var currentFrame = 0
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
