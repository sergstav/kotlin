fun test() {
   <warning descr="'MyClass.Default' is deprecated. Use A instead">MyClass</warning>.test
   MyClass()
   val a: MyClass? = null
   val b: MyTrait? = null
   <warning descr="'MyTrait.Default' is deprecated. Use A instead">MyTrait</warning>.test

   a == b
}

class MyClass(): MyTrait {
    deprecated("Use A instead") class object {
        val test: String = ""
    }
}

trait MyTrait {
    deprecated("Use A instead") class object {
        val test: String = ""
    }
}

// NO_CHECK_INFOS
// NO_CHECK_WEAK_WARNINGS