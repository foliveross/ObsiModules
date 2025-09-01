package cl.tu.nombre.obsimod.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import androidx.core.content.FileProvider

object ModuleRepository {

    private fun modulesDir(context: Context): File {
        val dir = File(context.filesDir, "modules")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun listFileNames(context: Context): List<String> =
        modulesDir(context).list()?.sorted()?.toList().orEmpty()

    private fun getDisplayName(context: Context, uri: Uri): String {
        var name = "modulo.txt"
        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val idx = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && idx >= 0) name = it.getString(idx)
        }
        return if (name.isBlank()) "modulo.txt" else name
    }

    fun importFromUri(context: Context, uri: Uri): Result<Module> = runCatching {
        val display = getDisplayName(context, uri)
        val target = File(modulesDir(context), display)
        context.contentResolver.openInputStream(uri).use { ins ->
            requireNotNull(ins) { "No se pudo abrir el archivo." }
            target.outputStream().use { outs -> ins.copyTo(outs) }
        }
        parseFile(target)
    }

    fun overwriteModule(context: Context, fileName: String, newText: String): Result<Module> =
        runCatching {
            val f = File(modulesDir(context), fileName)
            require(f.exists()) { "No existe el módulo $fileName" }
            f.writeText(newText)
            parseFile(f)
        }

    fun listModules(context: Context): List<Module> {
        val dir = modulesDir(context)
        val mods = mutableListOf<Module>()

        dir.listFiles { f -> f.isFile && (f.name.endsWith(".txt", true) || f.name.endsWith(".md", true)) }
            ?.sortedBy { it.name.lowercase() }
            ?.forEach { f -> runCatching { parseFile(f) }.onSuccess { mods += it } }

        return mods
    }

    private fun parseFile(file: File): Module {
        val text = file.readText()
        return PlainTextParser.parse(file.name, text)
    }

    fun createNew(context: Context, fileName: String, text: String): Result<Module> = runCatching {
        require(fileName.endsWith(".md", true) || fileName.endsWith(".txt", true)) {
            "Usa extensión .md o .txt"
        }
        val f = File(modulesDir(context), fileName)
        require(!f.exists()) { "Ya existe $fileName" }
        f.writeText(text)
        parseFile(f)
    }

    fun save(context: Context, fileName: String, text: String): Result<Module> = runCatching {
        val f = File(modulesDir(context), fileName)
        f.writeText(text)
        parseFile(f)
    }

    fun exportFileUri(context: Context, fileName: String): Uri {
        val f = File(modulesDir(context), fileName)
        return FileProvider.getUriForFile(context, context.packageName + ".files", f)
    }
}
