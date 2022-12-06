fun main() {
    fun part1(input: List<String>): Int =
        input.single().windowedSequence(4, 1, false).indexOfFirst { it.length == it.toSet().size } + 4

    fun part2(input: List<String>): Int =
        input.single().windowedSequence(14, 1, false).indexOfFirst { it.length == it.toSet().size } + 14

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
