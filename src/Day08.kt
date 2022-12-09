fun main() {
    fun isVisible(row: Int, col: Int, height: Int, forest: List<String>): Boolean = when {
        // Outter trees are always visible
        (col == 0 || col == forest[row].lastIndex || row == 0 || row == forest.lastIndex) -> true

        // Visible from Left
        forest[row].subSequence(0 until col).map(Char::digitToInt).all { it < height } -> true

        // Visible from right
        (col < forest[row].lastIndex &&
                forest[row].subSequence(col + 1..forest[row].lastIndex).map(Char::digitToInt)
                    .all { it < height }) -> true

        // Visible from top
        (0 until row).toList().map { forest[it][col].digitToInt() }.all { it < height } -> true

        // Visible from bottom
        (row < forest.lastIndex &&
                (row + 1..forest.lastIndex).toList().map { forest[it][col].digitToInt() }.all { it < height }) -> true

        else -> false
    }

    fun List<Int>.canopy(height: Int): List<Int> =
        slice(0..if (indexOfFirst { it >= height } > -1) indexOfFirst { it >= height } else lastIndex)

    fun treeScore(row: Int, col: Int, height: Int, forest: List<String>): Int {
        if (col == 0 || col == forest[row].lastIndex || row == 0 || row == forest.lastIndex) return 0
        return forest[row].subSequence(0 until col).reversed().map(Char::digitToInt).canopy(height).count() *
                forest[row].subSequence(col + 1..forest[row].lastIndex).map(Char::digitToInt).canopy(height).count() *
                (0 until row).toList().reversed().map { forest[it][col].digitToInt() }.canopy(height).count() *
                (row + 1..forest.lastIndex).toList().map { forest[it][col].digitToInt() }.canopy(height).count()
    }

    fun part1(input: List<String>): Int =
        input.foldIndexed(0) { rowIndex, rowAccumulator, cur ->
            cur.foldIndexed(rowAccumulator) { colIndex, acc, tree ->
                if (isVisible(rowIndex, colIndex, tree.digitToInt(), input)) acc + 1 else acc
            }
        }


    fun part2(input: List<String>): Int =
        input.mapIndexed { rowIndex, cur ->
            cur.mapIndexed { colIndex, tree ->
                treeScore(rowIndex, colIndex, tree.digitToInt(), input)
            }.max()
        }.max()

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
