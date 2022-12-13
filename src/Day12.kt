typealias Loc = Pair<Int, Int>

operator fun Loc.plus(other: Loc) = Loc(this.first + other.first, this.second + other.second)


class Day12Graph(val nodes: MutableMap<Loc, Node> = mutableMapOf()) {
    fun add(node: Node) {
        nodes[node.loc] = node
    }

    fun path(start: Node, end: Node): List<Node>? {
        initialize(start)
        var visiting: Node? = start
        while (visiting != end) {
            visiting!!.visited = true
            priorityQueue.remove(visiting)
            visiting.adjacentNodes.filterNot { it.visited }.forEach { toNode ->
                priorityQueue.add(toNode)
                val toDistance = visiting!!.distance + 1
                if (toNode.distance > toDistance) {
                    toNode.distance = toDistance
                    toNode.from = visiting
                }
            }
            visiting = priorityQueue.sortedBy { it.distance }.firstOrNull()
            if (visiting == null) return null
        }
        return end.path
    }

    private val priorityQueue = mutableSetOf<Node>()
    private fun initialize(start: Node) {
        priorityQueue.clear()
        nodes.values.map {
            it.visited = false
            it.from = null
            if (it.loc != start.loc) {
                it.distance = Int.MAX_VALUE
            } else {
                it.distance = 0
            }
        }
    }

    class Node(val loc: Loc, var elevation: Char) {
        val adjacentNodes: MutableList<Node> = mutableListOf()
        var distance: Int = Int.MAX_VALUE
        var visited = false
        var from: Node? = null

        val path: List<Node> get() = from?.let { it.path + this } ?: listOf(this)
    }

    companion object {
        fun load(input: List<String>): Triple<Node, Node, MutableMap<Loc, Node>> {
            var start: Node? = null
            var end: Node? = null
            val allNodes: MutableMap<Loc, Node> = mutableMapOf()

            (0..input.lastIndex).map { y ->
                (0..input[y].lastIndex).map { x ->
                    val node = Node(Loc(x, y), input[y][x])
                    node.apply {
                        when (elevation) {
                            'S' -> {
                                start = this
                                elevation = 'a'
                            }

                            'E' -> {
                                end = this
                                elevation = 'z'
                            }

                            else -> Unit
                        }
                    }
                    allNodes.put(Loc(x, y), node)
                }
            }

            return Triple(start!!, end!!, allNodes)
        }

        fun generateGraph(
            allNodes: MutableMap<Loc, Node>,
            start: Node,
            end: Node
        ): Day12Graph {
            val graph = Day12Graph()
            graph.add(start)
            graph.add(end)
            fun adjacentNodes(node: Node): List<Node> =
                listOf(Loc(-1, 0), Loc(1, 0), Loc(0, -1), Loc(0, 1)).mapNotNull { move ->
                    val newPos = node.loc + move
                    allNodes[newPos]?.let { newNode ->
                        if ((0..node.elevation.code + 1).contains(allNodes[newPos]!!.elevation.code)) {
                            if (!graph.nodes.contains(newNode.loc)) {
                                graph.add(newNode)
                                newNode.adjacentNodes.addAll(adjacentNodes(newNode))
                            }
                            newNode
                        } else null
                    }
                }

            start.adjacentNodes.addAll(adjacentNodes(start))
            return graph
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val (start, end, nodes) = Day12Graph.load(input)
        val path = Day12Graph.generateGraph(nodes, start, end).path(start, end)
        return path!!.count() - 1
    }

    fun part2(input: List<String>): Int {
        val (_, end, nodes) = Day12Graph.load(input)
        return nodes.values.filter { it.elevation == 'a' }.mapNotNull { start ->
            Day12Graph.generateGraph(nodes, start, end).path(start, end)?.let{it.count() - 1}
        }.min()
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    val part1 = part1(testInput)
    check(part1 == 31)
    check(part2(testInput) == 29)
    println("checked")

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
