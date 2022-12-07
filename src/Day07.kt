fun main() {
    open class File(val name: String, open val size: Long)
    class Dir(name: String, val parent: Dir? = null, val files: MutableList<File> = mutableListOf()) : File(name, 0) {
        val head: Dir get() = parent?.head ?: this
        override val size: Long get() = files.sumOf(File::size)
        operator fun get(name: String): Dir? = files.firstOrNull { it.name == name && it is Dir } as Dir

        fun asList(): List<File> = files.flatMap { if (it is Dir) it.asList() else listOf(it) } + this
    }

    fun parseTree(input: List<String>): Dir =
        input.fold(null as Dir?) { acc, cur ->
            val tokens = cur.split(' ')

            when {
                // Parse commands
                tokens[0] == "$" ->
                    when (tokens[1]) {
                        // change directory, move accumulator
                        "cd" ->
                            when (tokens[2]) {
                                ".." -> acc?.parent
                                else -> acc?.let {
                                    acc[tokens[2]] ?: acc.run {
                                        files.add(Dir(tokens[2], acc))
                                        acc[tokens[2]]
                                    }
                                } ?: Dir(tokens[2])
                            }

                        else -> acc
                    }

                else -> {
                    when {
                        tokens[0] == "dir" ->
                            acc?.apply { files.add(Dir(tokens[1], acc)) }

                        else ->
                            acc?.apply { files.add(File(tokens[1], tokens[0].toLong())) }
                    }
                    acc
                }
            }
        }?.head ?: Dir("Not Found")

    fun part1(input: List<String>): Long =
        parseTree(input).asList().sumOf { if (it is Dir && it.size <= 100_000) it.size else 0L } ?: 0L

    fun part2(input: List<String>): Long = parseTree(input).run{
        val need = 30_000_000 - (70_000_000 - size)
        asList().filterIsInstance<Dir>().sortedBy { it.size }.first { it.size > need }.size
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
