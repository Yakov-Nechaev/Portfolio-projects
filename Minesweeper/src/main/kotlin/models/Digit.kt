package models

enum class Digit(val pattern: List<Boolean>) {

    ZERO(listOf(true, true, true, false, true, true, true)),
    ONE(listOf(false, false, true, false, false, true, false)),
    TWO(listOf(true, false, true, true, true, false, true)),
    THREE(listOf(true, false, true, true, false, true, true)),
    FOUR(listOf(false, true, true, true, false, true, false)),
    FIVE(listOf(true, true, false, true, false, true, true)),
    SIX(listOf(true, true, false, true, true, true, true)),
    SEVEN(listOf(true, false, true, false, false, true, false)),
    EIGHT(listOf(true, true, true, true, true, true, true)),
    NINE(listOf(true, true, true, true, false, true, true));
}

/*
    ----0----
    |       |
    1       2
    |       |
    |---3---|
    |       |
    4       5
    |       |
    |___6___|
 */
