fun main() {
    fun itemSum(items: Iterable<Char>): Int =
        items.map { "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(it) + 1 }.sum()

    fun part1(input: List<String>): Int =
        itemSum(input.map {
            it.toCharArray(endIndex = it.length / 2).intersect(it.toCharArray(it.length / 2).toSet()).first()
        })

    fun part2(input: List<String>): Int =
        itemSum(input.map { it.toSet() }.chunked(3).fold(listOf<Char>()) { acc, cur ->
            acc.plus(cur.reduce { a, b ->
                a.intersect(b)
            }.first())
        })

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val part1 = part1(testInput)
    check(part1 == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
