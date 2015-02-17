// !DIAGNOSTICS_NUMBER: 1
// !DIAGNOSTICS: JSCODE_ERROR

fun box() {
    val i = "i"
    val n = 10

    js("var      = $n;")

    js("""for (var $i =
     )""")

    js("""var a = 10;
          var      = $n;
          var c = 15;""")
}