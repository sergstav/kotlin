package builders

inline fun call(inlineOptions(InlineOption.ONLY_LOCAL_RETURN) init: () -> Unit) {
    return {
        init()
    }()
}

//SMAP
//anonymousLambda.2.kt
//Kotlin
//*S Kotlin
//*F
//+ 1 anonymousLambda.2.kt
//builders/BuildersPackage$anonymousLambda_2$HASH$call$1
//*L
//1#1,18:1
//*E