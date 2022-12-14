fun main() {
    class AdventList : Comparable<AdventList> {
        fun terms(input: String): List<String> {
            var parenLevel = 0
            var start = 0
            var next = 0
            val termList = mutableListOf<String>()
            while (next <= input.length) {
                when {
                    next == input.length || (parenLevel == 0 && ",]".contains(input[next])) -> {
                        termList.add(input.substring(start until next))
                        start = next + 1
                    }

                    parenLevel > 0 && input[next] == ']' -> parenLevel--
                    input[next] == '[' -> parenLevel++
                    else -> Unit
                }
                next++
            }
            return termList
        }

        constructor(input: String) {
            list.addAll(terms(input.slice(1 until input.lastIndex)).map { it.trim() }
                .map {
                    when {
                        it.startsWith('[') -> Pair(null, AdventList(it))
                        else -> Pair(it.toIntOrNull(), null)
                    }
                })
        }

        var list = mutableListOf<Pair<Int?, AdventList?>>()

        fun Pair<Int?, AdventList?>.compareTo(other: Pair<Int?, AdventList?>) = when {
            this == other -> 0
            first != null && other.first != null -> first!!.compareTo(other.first!!)
            first != null && other.second != null -> AdventList("[$first]").compareTo(other.second!!)
            second != null && other.first != null -> second!!.compareTo(AdventList("[${other.first}]"))
            second != null && other.second != null -> second!!.compareTo(other.second!!)
            first == null && second == null && (other.first != null || other.second != null) -> -1
            else -> {
                1
            }
        }

        override fun compareTo(other: AdventList): Int {
            val result =
                this.list.zip(other.list).fold(0) { acc, (one, two) -> if (acc != 0) acc else one.compareTo(two) }
            return if (result == 0) listCount.compareTo(other.listCount) else result
        }

        val listCount: Int get() = list.sumOf { if (it.first != null) 1 else (it.second?.listCount ?: 0) + 1 }

    }

    fun part1(input: List<String>): Int = input.chunked(3).map { (first, second) ->
        AdventList(first).compareTo(AdventList(second)) <= 0
    }.mapIndexed { idx, cur -> if (cur) idx + 1 else 0 }.sum()

    fun part2(input: List<String>): Int {
        val dividers = listOf(AdventList("[[2]]"), AdventList("[[6]]"))
        return input.filterNot { it.isNullOrBlank() }.map { AdventList(it) }.union(dividers).sorted()
            .mapIndexedNotNull { idx, node -> if (dividers.contains(node)) idx + 1 else null }
            .fold(1) { acc, idx -> acc * idx }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)
    println("checked")

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
