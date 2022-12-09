import kotlin.math.abs

typealias Pos = Pair<Int, Int>

fun main() {
    fun distance(head: Pos, tail: Pos): Int = Math.max(abs(head.first - tail.first), abs(head.second - tail.second))

    fun follow(head: Pos, tail: Pos): Pos =
        tail.copy(tail.first + if (distance(head, tail)>1) head.first.compareTo(tail.first) else 0,
            tail.second + if (distance(head,tail) >1) head.second.compareTo(tail.second) else 0)

    fun move(head:Pos, direction:Char):Pos = when (direction) {
        'R' -> head.copy(first = head.first + 1)

        'L' -> head.copy(first = head.first - 1)

        'U' -> head.copy(second = head.second + 1)

        'D' -> head.copy(second = head.second - 1)
        else -> head
    }
    fun moves(knots:MutableList<Pos>, line:String): Collection<Pos> {
        val results = mutableListOf<Pos>()
        repeat(line.substring(2).toInt()) {

            List(knots.size) { idx ->
                when{
                    (idx==0) -> knots[0] = move(knots[0], line[0])
                    else -> knots[idx] = follow(knots[idx-1], knots[idx])
                }
            }
            results.add(knots.last())
        }
        return results
    }

    fun moves(head: Pos, tail: Pos, line: String): Triple<Pos, Pos, Collection<Pos>> {
        var h = head
        var t = tail
        val results = mutableListOf<Pos>()
        repeat(line.substring(2).toInt()) {
            h = move(h, line[0])
            t = follow(h,t)

            results.add(t)
        }
        return Triple(h, t, results)
    }

    fun part1(input: List<String>): Int {
        var head = Pos(0, 0)
        var tail = Pos(0, 0)
        return input.fold(listOf(tail)) { acc, line ->
            val (h, t, trail) = moves(head, tail, line)
            head = h
            tail = t
            acc + trail
        }.toSet().count()
    }

    fun part2(input: List<String>): Int {
        val knots = (0..9).map{Pos(0,0)}.toMutableList()
        return input.fold(listOf(knots.last())) { acc, line ->
            acc + moves(knots, line)
        }.toSet().count()
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
