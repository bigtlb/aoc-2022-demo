fun main() {
    class Scan(input: List<String>, val withFloor:Boolean=false) {
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
            bottom = rocks.maxOf{it.second}
            if (withFloor){
                //rocks += (bounds.first-1000..bounds.second+1000).map{Loc(it,bottom+2)}
                bottom += 2
            }
        }

        fun pourSand(source: Loc): Set<Loc> {
            var spot: Loc?
            do {
                spot = dropGrain(source)
            } while (spot != null)
            printChart()
            return sand
        }

        private fun printChart() {
            var chart = rocks.union(sand)
            val bounds = Pair(sand.minOf { it.first }, sand.maxOf { it.first })
            (0..chart.maxOf { it.second }).map { y ->
                (bounds.first-1..bounds.second+1).map {
                    val test = Loc(it, y)
                    when {
                        rocks.contains(test) -> print("#")
                        sand.contains(test) -> print("o")
                        else -> print(".")
                    }
                }
                println()
            }
            println("\n\n")
        }

        private fun dropGrain(source: Loc): Loc? {
            val testChart = rocks.union(sand)
            if (testChart.contains(source)) return null

            var lastPlace: Loc? = source+Loc(0,-1)
            generateSequence(0) { it + 1 }.takeWhile { y ->
                if (lastPlace == null || y > bottom || (y>1 && lastPlace == source)) {
                    lastPlace = null
                    false
                } else {
                    var test = listOf(lastPlace!! + Loc(0, 1), lastPlace!! + Loc(-1, 1), lastPlace!! + Loc(1, 1))
                        .firstOrNull { (!withFloor || it.second < bottom) && !(testChart.contains(it)) }
                    if (test == null) {
                        false
                    } else {
                        lastPlace = test!!
                        true
                    }
                }
            }.lastOrNull()
            lastPlace?.let { sand.add(lastPlace!!) }
            return lastPlace
        }

        private var bottom: Int
        private val bounds: Pair<Int, Int>
        var rocks: Set<Loc>
        val sand = mutableSetOf<Loc>()
    }

    fun part1(input: List<String>): Int = Scan(input).pourSand(Loc(500, 0)).count()

    fun part2(input: List<String>): Int = Scan(input, true).pourSand(Loc(500, 0)).count()

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)
    println("checked")

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
