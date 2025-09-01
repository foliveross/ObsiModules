package cl.tu.nombre.obsimod.data

data class Submodule(val title: String, val content: String)
data class Module(
    val fileName: String,
    val title: String,
    val submodules: List<Submodule>
)
