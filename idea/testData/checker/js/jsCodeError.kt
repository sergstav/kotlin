fun box() {
    val i = "i"
    val n = 10

    <error descr="[VARIABLE_EXPECTED] Variable expected">js(<error descr="Expecting ')'"><error descr="Expecting an expression"><</error></error>error <error descr="[UNRESOLVED_REFERENCE] Unresolved reference: descr">descr</error></error>="""[JSCODE_ERROR] JavaScript: missing variable name in code:
var      = 10;
   ^^^^^^^^   """>"""var      = $n;"""<<error descr="Expecting an element">/</error><error descr="[DEBUG] Reference is not resolved to anything, but is not marked unresolved">error</error><error descr="Expecting an element">></error><error descr="Unexpected tokens (use ';' to separate expressions on the same line)">)</error>

    js(<error descr="[JSCODE_ERROR] JavaScript: syntax error in code:
for (var i =
            ^
    )
^^^^^">"""for (var $i =
    )"""</error>)

    js(<error descr="[JSCODE_ERROR] JavaScript: missing variable name in code:
var a = 10;
    var      = 10;
       ^^^^^^^^
    var c = 15;">"""var a = 10;
    var      = $n;
    var c = 15;"""</error>)
}