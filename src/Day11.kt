
class Monkey(
    val items: ArrayDeque<Long>,
    val monkeyOperation: (new: Long) -> Long,
    val modVal:Long,
    val trueMonkey:Int,
    val falseMonkey:Int,
    val worryLevel: Long = 3
) {
    var counter = 0L
    fun throwThings(monkeys: List<Monkey>) {
        val mod = monkeys.map{it.modVal}.reduce(Long::times)
        while (items.isNotEmpty()) {
            val item = items.removeFirst()
            counter++
            val newItemVal = Math.floorDiv(monkeyOperation(item), worryLevel) % mod
            monkeys[if(newItemVal.rem(modVal) == 0L) trueMonkey else falseMonkey].items.add(newItemVal)
        }
    }

    companion object {
        fun load(lines: List<String>, worry:Boolean): Monkey {
            val items: ArrayDeque<Long> = ArrayDeque()
            var op: ((new: Long) -> Long)? = null
            var modVal = 0L
            var trueMonkey = 0
            var falseMonkey = 0
            lines.mapIndexed { idx, line ->
                when {
                    line.contains("items:") -> items.addAll(
                        line.takeLastWhile { it != ':' }.split(',').map { it.trim().toLong() })

                    line.contains("Operation:") -> {
                        val terms = line.takeLastWhile { it != ':' }.split(' ')
                        op = fun(old: Long): Long {
                            val secondVal = terms.last().toLongOrNull() ?: old
                            return when (terms[4]) {
                                "+" -> old + secondVal
                                "*" -> old * secondVal
                                else -> old
                            }
                        }
                    }

                    line.contains("Test:") -> {
                        modVal = line.takeLastWhile { it != ' ' }.toLong()
                        trueMonkey = lines[idx + 1].takeLastWhile { it != ' ' }.toInt()
                        falseMonkey = lines[idx + 2].takeLastWhile { it != ' ' }.toInt()
                    }

                    else -> Unit
                }
            }
            return Monkey(items, op!!, modVal, trueMonkey, falseMonkey, if (worry) 3L else 1L)
        }
    }
}

fun main() {


    fun List<Monkey>.throwThings() {
        forEach { monkey ->
            monkey.throwThings(this)
        }
    }

    val monkeyRegex = """Monkey \d+:$""".toRegex()
    fun loadMonkeys(input: List<String>, worry:Boolean=true): List<Monkey> =
        (input.mapIndexed { idx, line -> if (monkeyRegex.matches(line)) idx else -1 }.filter { it != -1 } + listOf(-1))
            .windowed(2) { (startIdx, nextIdx) ->
                Monkey.load(input.subList(startIdx, if (nextIdx == -1) input.size else nextIdx), worry)
            }


    fun part1(input: List<String>): Long {
        val monkeys = loadMonkeys(input)
        repeat(20) { _ -> monkeys.throwThings() }
        return monkeys.sortedByDescending { it -> it.counter }.take(2).let { (one, two) -> one.counter * two.counter }
    }

    fun part2(input: List<String>): Long {
    val monkeys = loadMonkeys(input, false)
    repeat(10000) { _ -> monkeys.throwThings() }
    return monkeys.sortedByDescending { it -> it.counter }.take(2).let { (one, two) -> one.counter * two.counter }
}

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
