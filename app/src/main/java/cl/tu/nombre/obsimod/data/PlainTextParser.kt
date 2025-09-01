package cl.tu.nombre.obsimod.data

object PlainTextParser {
    fun parse(fileName: String, text: String): Module {
        val lines = text.replace("\r\n", "\n").split("\n")
        var moduleTitle: String? = null

        data class Tmp(val title: String, val sb: StringBuilder)
        val subs = mutableListOf<Tmp>()
        var current: Tmp? = null

        fun pushCurrent() { current?.let { subs += it }; current = null }

        for (raw in lines) {
            val line = raw.trimEnd()
            when {
                line.startsWith("# ") && moduleTitle == null ->
                    moduleTitle = line.removePrefix("# ").trim()
                line.startsWith("## ") -> { pushCurrent(); current = Tmp(line.removePrefix("## ").trim(), StringBuilder()) }
                else -> current?.sb?.appendLine(line)
            }
        }
        pushCurrent()

        val submodules = subs.map { Submodule(it.title, it.sb.toString().trim()) }
        val titleFinal = moduleTitle ?: fileName.substringBeforeLast(".")
        return Module(fileName = fileName, title = titleFinal, submodules = submodules)
    }
}
