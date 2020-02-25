package ru.tohaman.livedatatest.data

object UserData {

    //две static функции, возвращающие два разных списка типа List<TestItem> для разных тестов

    fun getList1() = listOf(
        TestItem(0,1, "American actress, singer, model"),
        TestItem(0,2, "US President during American civil war"),
        TestItem(0,3, "Macedonian Catholic missionary nun"),
        TestItem(0,4, "US President 1961 – 1963")
    )

    fun getList2() = listOf(
        TestItem(0,1, "American civil rights campaigner"),
        TestItem(0,1, "South African President anti-apartheid campaigner"),
        TestItem(0,2, "British monarch since 1954"),
        TestItem(0,2, "British Prime Minister during WWII"),
        TestItem(0,3, "Businessman, US President."),
        TestItem(0,3, "American businessman, founder of Microsoft"),
        TestItem(0,1, "American Boxer and civil rights campaigner"),
        TestItem(0,1, "Leader of Indian independence movement"),
        TestItem(0,2, "British Prime Minister 1979 – 1990"),
        TestItem(0,2, "Italian explorer"),
        TestItem(0,3, "British scientist, theory of evolution"),
        TestItem(0,3, "American musician"),
        TestItem(0,4, "German scientist, theory of relativity"),
        TestItem(0,4, "British musician, member of Beatles")
    )
}