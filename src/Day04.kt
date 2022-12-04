fun main() {

    fun List<String>.parseList(): List<List<Set<Int>>> = this.map { line ->
        line.split(',').map { region ->
                region.split('-').map(String::toInt).let { (begin, end) -> (begin..end).toSet() }
        }
    }

    fun part1(input: List<String>): Int = input.parseList()
        .count { (first, second) -> (first subtract second).isEmpty() || (second subtract first).isEmpty()}


    fun part2(input: List<String>): Int = input.parseList()
        .count { (first, second) -> (first intersect second).isNotEmpty()}


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val part1 = part1(testInput)
    check(part1 == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
