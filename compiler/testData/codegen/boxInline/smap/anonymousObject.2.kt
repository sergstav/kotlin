package builders

inline fun call(inlineOptions(InlineOption.ONLY_LOCAL_RETURN) init: () -> Unit) {
    return object {
        fun run () {
            init()
        }
    }.run()
}


//SMAP
//anonymousObject.2.kt
//Kotlin
//*S Kotlin
//*F
//+ 1 anonymousObject.2.kt
//builders/BuildersPackage$anonymousObject_2$HASH$call$1
//*L
//1#1,21:1
//*E