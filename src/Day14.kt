fun main() {
    class Scan(input: List<String>) {
        init {
            rocks = input.flatMap {
                it.split(" -> ").map {
                    it.split(",").let { (x, y) -> Loc(x.toInt(), y.toInt()) }
                }
                .windowed(2).flatMap { (from, to) ->
                    when {
                        from.first != to.first -> listOf(from.first, to.first).sorted()
                            .let { (a, b) -> (a..b).map { Loc(it, from.second) } }

                        from.second != to.second -> listOf(from.second, to.second).sorted()
                            .let { (a, b) -> (a..b).map { Loc(from.first, it) } }

                        else -> listOf()
                    }
                }
            }.toSet()
            bounds = Pair(rocks.minOf { it.first }, rocks.maxOf { it.first })
        }

        fun pourSand(source: Loc): Set<Loc> {
            var spot: Loc?
            do {
                spot = dropGrain(source)
            } while (spot != null)
            return sand
        }

        private fun dropGrain(source: Loc): Loc? {
            val testChart = rocks.union(sand)
            var lastPlace: Loc? = source
            generateSequence(0) { it + 1 }.takeWhile { y ->
                if (!(bounds.first..bounds.second).contains(lastPlace?.first)) {
                    lastPlace = null
                    false
                } else {
                    var nextPlace = Loc(lastPlace!!.first, y)
                    lastPlace = if (testChart.contains(nextPlace)) {
                        if (testChart.contains(nextPlace + Loc(-1, 0))) {
                            if (testChart.contains(nextPlace + Loc(1, 0))) {
                                return@takeWhile false
                            } else {
                                nextPlace + Loc(1, 0)
                            }
                        } else {
                            nextPlace + Loc(-1, 0)
                        }
                    } else {
                        nextPlace
                    }
                    true
                }
            }.last()
            lastPlace?.let{sand.add(lastPlace!!)}
            return lastPlace
        }

        val rocks: Set<Loc>
        val bounds: Pair<Int, Int>
        val sand = mutableSetOf<Loc>()
    }

    fun part1(input: List<String>): Int = Scan(input).pourSand(Loc(500, 0)).count()

    fun part2(input: List<String>): Int = 0

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 0)
    println("checked")

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
