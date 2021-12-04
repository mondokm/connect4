package hu.mondokm.connect4

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.stage.Stage

class Game : Application() {

    companion object {
        private const val WIDTH = 512
        private const val HEIGHT = 512
    }

    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext
    private lateinit var grid: GridPane

    private lateinit var columns: Array<MutableList<CellStatus>>
    private var currentColumn: Int = 3
    private lateinit var currentPlayer: CellStatus

    // use a set so duplicates are not possible
    private val currentlyActiveKeys = mutableSetOf<KeyCode>()

    override fun start(mainStage: Stage) {
        mainStage.title = "Event Handling"

        val root = Group()
        mainScene = Scene(root)
        mainStage.scene = mainScene

        columns = Array(6) {i -> mutableListOf()}

        grid = GridPane()

        root.children.add(grid)

        prepareActionHandlers()

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                render()
            }
        }.start()

        mainStage.show()
    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler { event ->
            currentlyActiveKeys.add(event.code)
        }
        mainScene.onKeyReleased = EventHandler { event ->
            currentlyActiveKeys.remove(event.code)
        }
    }

    private fun render() {


        // clear canvas
        graphicsContext.clearRect(0.0, 0.0, WIDTH.toDouble(), HEIGHT.toDouble())

        // draw background
        graphicsContext.drawImage(space, 0.0, 0.0)

        // perform world updates
        updateSunPosition()

        // draw sun
        graphicsContext.drawImage(sun, sunX.toDouble(), sunY.toDouble())

        // display crude fps counter
        val elapsedMs = elapsedNanos / 1_000_000
        if (elapsedMs != 0L) {
            graphicsContext.fill = Color.WHITE
            graphicsContext.fillText("${1000 / elapsedMs} fps", 10.0, 10.0)
        }
    }

    private fun updateSunPosition() {
        if (currentlyActiveKeys.contains(KeyCode.LEFT)) {
            sunX--
        }
        if (currentlyActiveKeys.contains(KeyCode.RIGHT)) {
            sunX++
        }
        if (currentlyActiveKeys.contains(KeyCode.UP)) {
            sunY--
        }
        if (currentlyActiveKeys.contains(KeyCode.DOWN)) {
            sunY++
        }
    }

}
