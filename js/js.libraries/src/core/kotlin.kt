package kotlin

import java.util.*
import kotlin.jvm.internal.Intrinsic

library [Intrinsic("kotlin.arrays.array")]
public fun <T> array(vararg value : T): Array<T> = noImpl

// "constructors" for primitive types array

library [Intrinsic("kotlin.arrays.array")]
public fun doubleArray(vararg content : Double): DoubleArray    = noImpl

library [Intrinsic("kotlin.arrays.array")]
public fun floatArray(vararg content : Float): FloatArray       = noImpl

library [Intrinsic("kotlin.arrays.array")]
public fun longArray(vararg content : Long): LongArray          = noImpl

library [Intrinsic("kotlin.arrays.array")]
public fun intArray(vararg content : Int): IntArray             = noImpl

library [Intrinsic("kotlin.arrays.array")]
public fun charArray(vararg content : Char): CharArray          = noImpl

library [Intrinsic("kotlin.arrays.array")]
public fun shortArray(vararg content : Short): ShortArray       = noImpl

library [Intrinsic("kotlin.arrays.array")]
public fun byteArray(vararg content : Byte): ByteArray          = noImpl

library [Intrinsic("kotlin.arrays.array")]
public fun booleanArray(vararg content : Boolean): BooleanArray = noImpl

library("copyToArray")
public fun <reified T> Collection<T>.copyToArray(): Array<T> = noImpl
