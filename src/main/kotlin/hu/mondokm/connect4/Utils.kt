package hu.mondokm.connect4

import java.util.*
import kotlin.math.floor

object GameConstants {
    const val WINCONDITION = 4
    const val ROWS = 6
    const val COLUMNS = 7
    val DEFAULT_COLUMN = floor(COLUMNS / 2.0).toInt()
}

typealias GameMap = Array<MutableList<CellStatus>>

fun createGameMap(): GameMap = Array(GameConstants.COLUMNS) { mutableListOf() }

enum class WinStatus {
    YELLOW_WON,
    RED_WON,
    NEITHER_WON,
}

private fun mapCellStatusToWinStatus(cellStatus: CellStatus) = when (cellStatus) {
    CellStatus.YELLOW -> WinStatus.YELLOW_WON
    CellStatus.RED -> WinStatus.RED_WON
    else -> WinStatus.NEITHER_WON
}

fun checkWinCondition(columns: GameMap, player: CellStatus): WinStatus {
    return if (checkColumns(columns, player) ||
        checkRows(columns, player) ||
        checkDiagonals(columns, player)
    ) mapCellStatusToWinStatus(player)
    else WinStatus.NEITHER_WON
}

private fun createWinConditionReferenceList(player: CellStatus) = List(GameConstants.WINCONDITION) { player }

private fun checkColumns(columns: GameMap, player: CellStatus): Boolean =
    checkIterableofLists(columns.asIterable(), player)

private fun checkRows(columns: GameMap, player: CellStatus): Boolean {
    val winCondition = createWinConditionReferenceList(player)
    return (0 until GameConstants.ROWS).map { row ->
        columns.map { column ->
            column.getOrElse(row) { CellStatus.EMPTY }
        }.toList()
    }.let {
        checkIterableofLists(it, player)
    }
}

private fun checkDiagonals(columns: GameMap, player: CellStatus): Boolean {
    val lists: MutableList<List<CellStatus>> = mutableListOf()
    // x = y
    (0 until GameConstants.COLUMNS).forEach { startColumn ->
        (0 until GameConstants.COLUMNS - startColumn).map { row -> columns[startColumn + row].getOrElse(row) { CellStatus.EMPTY } }
            .toList().also { lists.add(it) }
    }
    (0 until GameConstants.COLUMNS).forEach { startColumn ->
        ((GameConstants.ROWS - 1) downTo GameConstants.ROWS - 1 - startColumn).map { row ->
            columns[startColumn + row - (GameConstants.ROWS - 1)].getOrElse(
                row
            ) { CellStatus.EMPTY }
        }
            .toList().also { lists.add(it) }
    }
    // x = -y
    (0 until GameConstants.COLUMNS).forEach { startColumn ->
        (0 until startColumn + 1).map { row -> columns[startColumn - row].getOrElse(row) { CellStatus.EMPTY } }
            .toList().also { lists.add(it) }
    }
    (0 until GameConstants.COLUMNS).forEach { startColumn ->
        ((GameConstants.COLUMNS - startColumn - 1) downTo 0).map { row ->
            columns[startColumn + (GameConstants.COLUMNS - startColumn - row) - 1].getOrElse(
                row
            ) { CellStatus.EMPTY }
        }
            .toList().also { lists.add(it) }
    }
    return checkIterableofLists(lists, player)
}

private fun checkIterableofLists(iterable: Iterable<List<CellStatus>>, player: CellStatus): Boolean =
    createWinConditionReferenceList(player).run {
        return iterable.any {
            Collections.indexOfSubList(it, this) != -1
        }
    }