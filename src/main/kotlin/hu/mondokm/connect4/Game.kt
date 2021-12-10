package hu.mondokm.connect4

import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.util.*


class Game : Application() {

    private lateinit var mainStage: Stage
    private lateinit var mainScene: Scene
    private lateinit var grid: GridPane
    private lateinit var selectorLine: HBox

    private lateinit var columns: GameMap
    private var currentColumn: Int = GameConstants.DEFAULT_COLUMN
    private lateinit var currentPlayer: CellStatus

    override fun start(mainStage: Stage) {
        check(GameConstants.COLUMNS >= GameConstants.ROWS) { "Number of columns must be greater than the number of rows" }

        this.mainStage = mainStage
        mainStage.isResizable = false

        mainStage.title = "Connect four"

        val root = Group()
        mainScene = Scene(root)
        mainStage.scene = mainScene

        val vbox = VBox()

        columns = createGameMap()
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
        for (i in 0 until GameConstants.COLUMNS) {
            selectorLine.children.add(Cell(if (currentColumn == i) currentPlayer else CellStatus.BACKGROUND))
        }

        grid.children.clear()
        columns.forEachIndexed { index, mutableList ->
            val cells =
                List(GameConstants.ROWS) { i -> mutableList.getOrElse(GameConstants.ROWS - 1 - i) { CellStatus.EMPTY } }.map {
                    Cell(it)
                }
            grid.addColumn(index, *cells.toTypedArray())
        }

        mainStage.sizeToScene()
    }

    private fun keyPressed(code: KeyCode) {
        if (code == KeyCode.LEFT) {
            currentColumn = if (currentColumn > 0) currentColumn - 1 else currentColumn
        }
        if (code == KeyCode.RIGHT) {
            currentColumn = if (currentColumn < GameConstants.COLUMNS - 1) currentColumn + 1 else currentColumn
        }
        if (code == KeyCode.DOWN) {
            check(currentColumn in 0 until GameConstants.COLUMNS)
            val column = columns.get(currentColumn)
            if (column.size >= GameConstants.ROWS) return
            column.add(currentPlayer)
            if(checkWinCondition(columns, currentPlayer) != WinStatus.NEITHER_WON){
                val alert = Alert(
                    AlertType.INFORMATION,
                    "The ${currentPlayer.toString().lowercase(Locale.getDefault()).capitalize()} player won the game",
                    ButtonType.OK,
                )
                alert.headerText = null
                alert.showAndWait()
                    .filter{it == ButtonType.OK}
                    .ifPresent{
                        Platform.exit()
                        System.exit(0)
                    }
            }
            currentPlayer = if (currentPlayer == CellStatus.RED) CellStatus.YELLOW else CellStatus.RED
            currentColumn = GameConstants.DEFAULT_COLUMN
        }
    }


}
