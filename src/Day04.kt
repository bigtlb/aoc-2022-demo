fun main() {

    fun List<String>.parseList(): List<Pair<IntRange, IntRange>> = this.map {
        it.split(',').zipWithNext { a, b ->
            Pair(
                a.split('-').map(String::toInt).zipWithNext { begin, end -> IntRange(begin, end) }.single(),
                b.split('-').map(String::toInt).zipWithNext { begin, end -> IntRange(begin, end) }.single()
            )
        }.single()
    }

    fun part1(input: List<String>): Int = input.parseList()
        .sumOf { if ((it.first subtract it.second.toSet()).isNotEmpty() && (it.second subtract it.first.toSet()).isNotEmpty()) 0 else 1 as Int }


    fun part2(input: List<String>): Int = input.parseList()
        .sumOf { if ((it.first intersect it.second.toSet()).isNotEmpty()) 1 else 0 as Int }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val part1 = part1(testInput)
    check(part1 == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
