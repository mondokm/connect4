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
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage

class Game : Application() {

    companion object {
        private const val WIDTH = 512
        private const val HEIGHT = 512
    }

    private lateinit var mainStage: Stage
    private lateinit var mainScene: Scene
    private lateinit var graphicsContext: GraphicsContext
    private lateinit var grid: GridPane
    private lateinit var selectorLine: HBox

    private lateinit var columns: Array<MutableList<CellStatus>>
    private var currentColumn: Int = 3
    private lateinit var currentPlayer: CellStatus

    override fun start(mainStage: Stage) {
        this.mainStage = mainStage
        mainStage.isResizable = false

        mainStage.title = "Connect four"

        val root = Group()
        mainScene = Scene(root)
        mainStage.scene = mainScene

        val vbox = VBox()

        columns = Array(7) { i -> mutableListOf() }
        currentPlayer = CellStatus.YELLOW

        grid = GridPane()
        selectorLine = HBox()
        vbox.children.add(selectorLine)
        vbox.children.add(grid)

        root.children.add(vbox)

        render()

        prepareActionHandlers()

        mainStage.show()
    }

    private fun prepareActionHandlers() {
        mainScene.onKeyPressed = EventHandler { event ->
            keyPressed(event.code)
            render()
        }
    }

    private fun render() {
        selectorLine.children.clear()
        for (i in 0..6) {
            selectorLine.children.add(Cell(if(currentColumn == i) currentPlayer else CellStatus.BACKGROUND))
        }

        grid.children.clear()
        columns.forEachIndexed { index, mutableList ->
            val cells = List(6) { i -> mutableList.getOrElse(5 - i) { CellStatus.EMPTY } }.map { Cell(it) }
            grid.addColumn(index, *cells.toTypedArray())
        }

        mainStage.sizeToScene()
    }

    private fun keyPressed(code: KeyCode) {
        if (code == KeyCode.LEFT) {
            currentColumn = if (currentColumn > 0) currentColumn - 1 else currentColumn
        }
        if (code == KeyCode.RIGHT) {
            currentColumn = if (currentColumn < 6) currentColumn + 1 else currentColumn
        }
        if (code == KeyCode.DOWN) {
            check(currentColumn in 0..6)
            val column = columns.get(currentColumn)
            if (column.size >= 6) return
            column.add(currentPlayer)
            currentPlayer = if (currentPlayer == CellStatus.RED) CellStatus.YELLOW else CellStatus.RED
            currentColumn = 3
        }
    }

}
