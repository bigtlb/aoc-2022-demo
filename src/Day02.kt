fun main() {
    fun score(round: String, strategy: Int = 1): Int {
        var score = 0
        var (them, you) = round.uppercase().toCharArray().filterNot { it == ' ' }

        //For strategy 2, choos what you throw by seeing if you should win, lose, or draw
        if (strategy == 2){
            you = when (you){
                // x = lose
                'X' -> "XYZ"[("ABC".indexOf(them) + 2) % 3]
                // z = win
                'Z' -> "XYZ"[("ABC".indexOf(them) + 1) % 3]
                else -> "XYZ"["ABC".indexOf(them)]
            }
        }

        // Points for your choise
        score += when (you) {
            'X' -> 1 // rock
            'Y' -> 2 // paper
            'Z' -> 3 // scissors
            else -> 0
        }

        score += when {
            (them == 'A' && you == 'Y') || // them = rock, you = paper
            (them == 'B' && you == 'Z') || // them = paper, you = scissors
            (them == 'C' && you == 'X')    // them = scissors and you = rock
            -> 6
            "ABC".indexOf(them) == "XYZ".indexOf(you) -> 3
            else -> 0
        }

        return score
    }

    fun part1(input: List<String>): Int {
        return input.fold(0) { acc, round -> acc + score(round) }
    }

    fun part2(input: List<String>): Int {
        return input.fold(0) { acc, round -> acc + score(round, 2) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
