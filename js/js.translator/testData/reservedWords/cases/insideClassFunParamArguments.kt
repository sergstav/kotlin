package foo

// NOTE THIS FILE IS AUTO-GENERATED by the generateTestDataForReservedWords.kt. DO NOT EDIT!

class TestClass {
    fun foo(arguments: String) {
    assertEquals("123", arguments)
    testRenamed("arguments", { arguments })
}

    fun test() {
        foo("123")
    }
}

fun box(): String {
    TestClass().test()

    return "OK"
}