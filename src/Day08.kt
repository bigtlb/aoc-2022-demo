fun main() {
    fun isVisible(row: Int, col: Int, height: Int, forest: List<String>): Boolean {
        if (col == 0 || col == forest[row].lastIndex || row == 0 || row == forest.lastIndex) return true
        if (forest[row].subSequence(0 until col).map(Char::digitToInt).all { it < height }
            || (col < forest[row].length - 1 &&
                    forest[row].subSequence(col + 1..forest[row].lastIndex).map(Char::digitToInt)
                        .all { it < height }
                    ) ||
            (0 until row).toList().map { forest[it][col].digitToInt() }.all { it < height }
            || (row < forest.size - 1 &&
                    (row + 1..forest.lastIndex).toList().map { forest[it][col].digitToInt() }
                        .all { it < height }
                    )
        ) return true

        return false
    }

    fun List<Int>.canopy(height: Int): List<Int> =
        slice(0..if (indexOfFirst { it >= height } > -1) indexOfFirst { it >= height } else lastIndex)

    fun treeScore(row: Int, col: Int, height: Int, forest: List<String>): Int {
        val left = if (col > 0) forest[row].subSequence(0 until col).reversed().map(Char::digitToInt).canopy(height)
            .count() else 0
        val right = if (col < forest[row].length - 1) forest[row].subSequence(col + 1..forest[row].lastIndex)
            .map(Char::digitToInt).canopy(height).count() else 0
        val top = if (row > 0) (0 until row).toList().reversed().map { forest[it][col].digitToInt() }.canopy(height)
            .count() else 0
        val bottom =
            if (row < forest.size - 1) (row + 1..forest.lastIndex).toList().map { forest[it][col].digitToInt() }
                .canopy(height).count() else 0
        return left * right * top * bottom
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
