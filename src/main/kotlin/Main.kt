fun main() {
    println(System.getProperty("java.library.path"))
    println(Symbol::class.java.name)
    val lib = CPythonAdapter()
    val code = """
        print("Hello from Python!", flush=True)
        def f(x):
            if x == 1:
                return 1

            a = [2, x] == [2, 2]
            return a == x
    """.trimIndent()
    lib.run(code, "f", arrayOf(Symbol("x")))
}