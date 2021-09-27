package ccl

fun main() {
    var flag = ""
    val possible = "abcdefghijklmnopqrstuvwxyz_{}ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    while (true) {
        var success = false
        for (c in possible) {
            if (Blind.guess(flag.length, c)) {
                flag += c
                println(flag)
                success = true
                break
            }
        }
        if (!success)
            break
    }
}
