data class Day16Valve(val name: String, val rate: Int, val connections: List<String>) {
    val distanceMap = mutableMapOf<String, Int>()

    companion object {
        val parseRegex =
            """Valve (?<name>[A-Z]+) has flow rate=(?<rate>[0-9]+); tunnels* leads* to valves* (?<connections>.+)""".toRegex()

        fun loadValves(input: List<String>): Map<String, Day16Valve> = input.mapNotNull {
            parseRegex.matchEntire(it)?.let { it ->
                val groups = it.groups as MatchNamedGroupCollection
                Pair(
                    groups["name"]!!.value,
                    Day16Valve(
                        groups["name"]!!.value,
                        groups["rate"]!!.value.toInt(),
                        groups["connections"]!!.value.split(",").map { it.trim() })
                )
            }
        }.associate { it }
    }

}

fun main() {
    fun Map<String, Day16Valve>.score(start: String, order: List<String>, minutes: Int): Int {
        var cur = start
        var left = minutes
        var remaining = ArrayDeque(order)
        var total = 0
        var currentRate = 0

        while (left > 0) {
            if (remaining.count() > 0) {
                val next = remaining.removeFirst()
                val moves = this[cur]!!.pathTo[next]
                var turns = moves!!.count().coerceAtMost(left)
                total += turns * currentRate
                if (turns < left) {
                    turns++
                    total += currentRate
                    currentRate += this[next]!!.rate
                }
                left -= turns
                cur = next
            } else {
                total += left * currentRate
                left = 0
            }
        }
        return total
    }

    fun permutations(input: List<String>, idx: Int = input.lastIndex): List<List<String>> {
        if (idx == 1) return listOf(input)

        var tmp = input.toMutableList()
        var result = mutableListOf<List<String>>()

        for (i in (0 until idx)) {
            result.addAll(permutations(tmp, idx - 1))
            val pos = if (idx % 2 == 0) i else 0
            val tchar = tmp[pos]
            tmp[pos] = tmp[idx]
            tmp[idx] = tchar
            result.add(tmp.toList())
        }
        return result
    }

    fun Map<String, Day16Valve>.highestPath(opened:Set<String>, node: String, minutes: Int, sum:Int, open:Int): Int
    Pair<Int, List<String>> {
        map {
            val visited = mutableListOf(it.key)
            val valve = it.value
            valve.pathTo.clear()
            valve.pathTo.putAll(valve.connections.map { to -> Pair(to, listOf(to)) }.toMap())
            visited.addAll(it.value.connections)
            while (visited.size < this.keys.size) {
                valve.pathTo.putAll(valve.pathTo.flatMap { pathEntry ->
                    this[pathEntry.key]!!.connections.filter { to ->
                        !visited.contains(to)
                    }.mapNotNull { to ->
                        visited.add(to)
                        Pair(to, pathEntry.value + to)
                    }
                }.toMap())
            }
        }


        println("Valves: ${values.filter { it.rate > 0 }.count()}")
        val valves = values.filter { it.rate > 0 }.sortedWith(compareByDescending { it.rate }).map{it.name}.toMutableList()
        var best = Pair(this.score(start,valves,minutes), valves.toList())
        println("Best: $best")
        var n = valves.lastIndex
        val stack = (0..n).map{0}.toMutableList()
        var i = 0
        while (i < n) {
            if (stack[i] < i) {
                val from = if (i % 2 == 0) 0 else stack[0]
                val tmp = valves[from]
                valves[from] = valves[i]
                valves[i] = tmp
                val score = this.score(start, valves, minutes)
                if (score > best.first){
                    best = Pair(score, valves.toList())
                    println("Best: $best")
                }
                stack[i]++
                i = 0
            } else {
                stack[i] = 0
                i++
            }
        }
        println("Result: ${best.first} ${best.second}")
        return best
    }

    fun part1(input: List<String>): Int {
        val result = Day16Valve.loadValves(input).highestPath("AA", 30)
        return result.first
    }

    fun part2(input: List<String>): Int = 0

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 1651)
    check(part2(testInput) == 0)
    println("checked")

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
