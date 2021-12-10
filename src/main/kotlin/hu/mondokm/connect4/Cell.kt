package hu.mondokm.connect4

import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class Cell(
    status: CellStatus
): Pane() {
    private val circle = Circle(40.0,
        when (status) {
            CellStatus.EMPTY -> Color.WHITE
            CellStatus.RED -> Color.RED
            CellStatus.YELLOW -> Color.YELLOW
            CellStatus.BACKGROUND -> Color.TRANSPARENT
        })

    init {
        setPrefSize(120.0,120.0)
        style = "-fx-background-color: #0000DD;"
        circle.relocate(20.0,20.0)
        children.add(circle)
    }
}