package a

import a.A.*
import a.A.C
import a.A.<!CANNOT_STAR_IMPORT_FROM_SINGLETON!>C<!>.*
import a.A.<!CANNOT_STAR_IMPORT_FROM_SINGLETON!>D<!>.*
import a.A.<!CANNOT_STAR_IMPORT_FROM_SINGLETON!>C<!>.*
import a.A.C.G
import a.A.E.<!CANNOT_STAR_IMPORT_FROM_SINGLETON!>J<!>.*

import a.B.C.*
import a.B.C.<!CANNOT_STAR_IMPORT_FROM_SINGLETON!>A<!>.*
import a.B.C.<!CANNOT_STAR_IMPORT_FROM_SINGLETON!>D<!>.*

class A {
    object C {
        object G
    }
    object D {

    }

    class E {
        object J
    }
}

object B {
    class C {
        object A
        object D
    }
}