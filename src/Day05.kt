fun main() {
    val move = """move (\d+) from (\d+) to (\d+)""".toRegex()

    fun playInstructions(input: List<String>, allAtOnce: Boolean = false): String {
        val state = mutableListOf<ArrayDeque<Char>>()
        input.takeWhile { !it.startsWith(" 1 ") }.map {
            while (state.size < Math.floorDiv((it.length - 1), 4) + 1) {
                state.add(ArrayDeque())
            }
            it.filterIndexed { index, _ -> (index - 1) % 4 == 0 }.mapIndexed { index, c -> state[index].add(c) }
        }

        state.forEach {
            it.removeIf(Char::isWhitespace)
            it.reverse()
        }

        input.takeLastWhile { it.isNotBlank() }
            .forEach {
                move.matchEntire(it)?.let {
                    it.groupValues.drop(1).map { group -> group.trim().toInt() }
                        .let { (move, from, to) ->
                            if (allAtOnce) {
                                state[to - 1].addAll(state[from - 1].takeLast(move))
                                (1..move).forEach { _ -> state[from - 1].removeLast() }
                            } else {
                                (1..move).forEach { _ -> state[to - 1].add(state[from - 1].removeLast()) }
                            }
                        }
                }
            }
        return String(state.map { it.last() }.toCharArray())
    }

    fun part1(input: List<String>): String = playInstructions(input)

    fun part2(input: List<String>): String = playInstructions(input, true)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
