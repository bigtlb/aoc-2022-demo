import java.lang.Math.abs

fun main() {
    fun generateValues(input: List<String>): List<Int> {
        val values = mutableListOf<Int>()
        input.fold(1) { acc, cur ->
            when (cur.substring(0..3)) {
                "noop" -> {
                    values.add(acc)
                    acc
                }

                "addx" -> {
                    repeat(2) { values.add(acc) }
                    acc + cur.substring(5).toInt()
                }

                else -> acc
            }
        }
        return values
    }

    fun part1(input: List<String>): Int =
        generateValues(input).mapIndexed { idx, cur -> if ((idx - 19) % 40 == 0) cur * (idx + 1) else 0 }.sum()


    fun part2(input: List<String>) {
        val values = generateValues(input)
        println((0..5).map{row ->
            println(String((0..39).map{col -> if (abs(values[row * 40 + col] - col)<=1) 'X' else '.'}.toCharArray()))
        })
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    //check(part2(testInput) == 0)
    part2(testInput)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
