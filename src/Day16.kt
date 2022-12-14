data class Day16Valve(val name: String, val rate: Int, val connections: List<String>) {
    val distanceMap = mutableMapOf<String, Int>()


    fun computeDistances(nodes: List<Day16Valve>) = apply {
        distanceMap[name] = 0
        ArrayDeque<Day16Valve>().let { queue ->
            queue.add(this)
            val visited = mutableSetOf<String>()
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                val distance = current.distanceMap[name]!!
                visited.add(current.name)
                current.connections.filter { it !in visited }.forEach { n ->
                    val neighbor = nodes.first { it.name == n }
                    neighbor.distanceMap[name] = distance + 1
                    queue.addLast(neighbor)
                }
            }
        }
        distanceMap.remove(name)
    }

    companion object {
        val parseRegex =
            """Valve (?<name>[A-Z]+) has flow rate=(?<rate>[0-9]+); tunnels* leads* to valves* (?<connections>.+)""".toRegex()

        fun loadValves(input: List<String>): List<Day16Valve> = input.mapNotNull { line->
            parseRegex.matchEntire(line)?.let { r ->
                val groups = r.groups as MatchNamedGroupCollection
                Day16Valve(
                    groups["name"]!!.value,
                    groups["rate"]!!.value.toInt(),
                    groups["connections"]!!.value.split(",").map { it.trim() })
            }
        }
    }

}

fun main() {

    fun List<Day16Valve>.computeAllDistances(): List<Day16Valve> {
        filter { it.rate > 0 }
            .forEach { rated ->
                rated.computeDistances(this)
            }
        return this
    }

    fun Day16Valve.remaining(opened: Set<String>, timeLeft: Int) =
        distanceMap.filter { (key, timeNeeded) -> key !in opened && timeNeeded + 1 <= timeLeft }

    fun List<Day16Valve>.highestPath(
        opened: Set<String>,
        node: String,
        minutesLeft: Int,
        sum: Int,
        open: Int
    ): Pair<Int, List<String>> {
        val curNode = this.first { it.name == node }
        return when {
            minutesLeft < 0 -> Pair(0, emptyList())
            minutesLeft == 0 -> Pair(sum, emptyList())
            minutesLeft == 1 -> Pair(sum + open, emptyList())
            curNode.distanceMap.all { (key, _) -> key in opened } -> Pair(sum + minutesLeft * open, emptyList())
            else -> curNode.remaining(opened, minutesLeft)
                .map { (nNode, distance) ->
                    highestPath(
                        opened + node,
                        nNode,
                        minutesLeft - (distance + 1),
                        sum + (distance + 1) * open,
                        open + this.first { it.name == nNode }.rate
                    ).let{
                        Pair(it.first,listOf(nNode)+it.second)
                    }
                }.plus(
                    Pair(sum + minutesLeft * open, emptyList())
                )
                .maxBy{it.first}
        }
    }

    fun part1(input: List<String>): Int =
        Day16Valve.loadValves(input)
            .computeAllDistances()
            .highestPath(emptySet(), "AA", 30, 0, 0).let{
                println(it.second)
                it.first
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
