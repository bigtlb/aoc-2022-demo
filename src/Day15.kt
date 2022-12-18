import kotlin.math.abs

fun main() {

    val pairRegex = """x=(?<x>[\-0-9]+), y=(?<y>[\-0-9]+)""".toRegex()
    fun parseList(input: List<String>): List<Pair<Loc, Loc>> =
        input.mapNotNull {
            pairRegex.findAll(it)?.toList()?.let { (first, second) ->
                val source = first.groups as MatchNamedGroupCollection
                val beacon = second.groups as MatchNamedGroupCollection
                Pair(
                    Loc(source["x"]?.value?.toInt() ?: 0, source["y"]?.value?.toInt() ?: 0),
                    Loc(beacon["x"]?.value?.toInt() ?: 0, beacon["y"]?.value?.toInt() ?: 0)
                )
            }
        }

    data class Diamond(val top: Loc, val bottom: Loc, val left: Loc, val right: Loc, val center: Loc) {
        fun contains(test: Loc): Boolean =
            (left.first..right.first).contains(test.first) && (top.second..bottom.second).contains(test.second)
        fun rightEdge(row:Int):Int = if ((top.second..bottom.second).contains(row)) right.first - abs(center.second - row) else Int.MAX_VALUE
    }

    fun Pair<Loc, Loc>.getDistance() = abs(first.second - second.second) + abs(first.first - second.first)
    fun Pair<Loc, Loc>.getDiamond(): Diamond {
        val distance = getDistance()
        return Diamond(
            first + Loc(0, -distance),
            first + Loc(0, distance),
            first + Loc(-distance, 0),
            first + Loc(distance, 0),
            this.first
        )
    }

    fun List<Pair<Loc, Loc>>.checkRow(row: Int): String {
        val testSet = mutableSetOf<Loc>()
        val beaconSet = mutableSetOf<Loc>()
        val sourceSet = mutableSetOf<Loc>()
        filter {
            it.getDiamond().contains(Loc(it.first.first, row))
        }.map {
            val distance = it.getDistance()
            val width = when {
                row > it.first.second -> it.first.second + distance - row
                row < it.first.second -> row - (it.first.second - distance)
                else -> distance
            }
            testSet.addAll((it.first.first - width..it.first.first + width).toList().map { Loc(it, row) })
            if (it.first.second == row) sourceSet.add(it.first)
            if (it.second.second == row) beaconSet.add(it.second)
        }
        val bounds = Loc(testSet.minOf { it.first }, testSet.maxOf { it.first })
        val result = String((bounds.first..bounds.second).toList().map {
            val test = Loc(it, row)
            when {
                sourceSet.contains(test) -> 'S'
                beaconSet.contains(test) -> 'B'
                testSet.contains(test) -> '#'
                else -> '.'
            }
        }.toCharArray())
        return result
    }

    fun List<Pair<Loc, Loc>>.checkAll(searchSpace: Int): Loc {
        val diamonds = this.map { Pair(it.first, it.getDiamond()) }
        var found:Loc? = null
        var curDiamond: Pair<Loc, Diamond>?=null
        var x:Int=0
        var y:Int=0
        while(found == null && y <= searchSpace){
            while(found == null && x <= searchSpace){
                val test = Loc(x,y)
                curDiamond = diamonds.firstOrNull { it.second.contains(test) }
                if (curDiamond == null){
                    found = Loc(x,y)
                } else {
                        x = curDiamond.second.rightEdge(y)
                }
                x++
            }
            x=0
            y++
        }
        return found!!
    }

    fun part1(input: List<String>, row:Int): Int = parseList(input).checkRow(row).count { it == '#' }

    fun part2(input: List<String>, searchSpace: Int): Int {
        val found = parseList(input).checkAll(searchSpace)
        return found?.let{
            it.first * 4000000 + it.second
        }?:0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput,10) == 26)
    check(part2(testInput, 20) == 56000011)
    println("checked")

    val input = readInput("Day15")
    println(part1(input,2000000))
    println(part2(input, 4000000))
}
