import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

typealias Pos = Pair<Int, Int>

typealias Loc = Pair<Int, Int>

operator fun Loc.plus(other: Loc) = Loc(this.first + other.first, this.second + other.second)
