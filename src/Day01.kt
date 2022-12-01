fun main() {
    fun part1(input: List<String>): Long {
        var prevCalCount: Long = 0
        val lastCalCount = input.fold(0L) { acc, cal ->
            val cur = cal.toLongOrNull()
            if (cur == null) {
                if (prevCalCount < acc) {
                    prevCalCount = acc
                }
                0
            } else {
                acc + cur
            }
        }
        return maxOf(prevCalCount, lastCalCount)
    }

    fun part2(input: List<String>): Long {
        val elfCounts = mutableListOf<Long>()
        elfCounts.add(
            input.fold(0L) { acc, cal ->
                val cur = cal.toLongOrNull()
                if (cur == null) {
                    elfCounts.add(acc)
                    0
                } else {
                    acc + cur
                }
            }
        )
        return elfCounts.sortedDescending().toList().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000L)
    check(part2(testInput) == 45000L)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
