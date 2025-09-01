package cl.tu.nombre.obsimod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import cl.tu.nombre.obsimod.data.Module
import cl.tu.nombre.obsimod.data.ModuleRepository
import cl.tu.nombre.obsimod.data.Submodule
import cl.tu.nombre.obsimod.ui.LibraryScreen
import cl.tu.nombre.obsimod.ui.ModuleDetailScreen
import cl.tu.nombre.obsimod.ui.SubmoduleScreen
import cl.tu.nombre.obsimod.ui.EditorScreen

sealed interface Screen {
    data object Library : Screen
    data class ModuleDetail(val module: Module) : Screen
    data class SubmoduleView(val module: Module, val sub: Submodule) : Screen
    data class Editor(val fileName: String, val initialText: String, val isNew: Boolean) : Screen
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { AppRoot() } }
    }

    @Composable
    private fun AppRoot() {
        var screen by remember { mutableStateOf<Screen>(Screen.Library) }
        var modules by remember { mutableStateOf(ModuleRepository.listModules(this)) }

        fun refresh() { modules = ModuleRepository.listModules(this) }

        val importLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            if (uri != null) {
                try {
                    contentResolver.takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {}
                ModuleRepository.importFromUri(this, uri)
                    .onSuccess {
                        refresh()
                        screen = Screen.Library
                    }
                    .onFailure { }
            }
        }

        when (val s = screen) {
            Screen.Library -> LibraryScreen(
                modules = modules,
                onImportClick = { importLauncher.launch(arrayOf("text/plain", "text/markdown")) },
                onModuleClick = { m -> screen = Screen.ModuleDetail(m) },
                onNewClick = {
                    val template = """
                        # Nuevo módulo

                        ## Sección
                        Contenido inicial…
                    """.trimIndent()
                    screen = Screen.Editor("nuevo.md", template, isNew = true)
                }
            )
            is Screen.ModuleDetail -> ModuleDetailScreen(
                module = s.module,
                onBack = { screen = Screen.Library },
                onOpenSubmodule = { sub -> screen = Screen.SubmoduleView(s.module, sub) },
                onEdit = {
                    val txt = runCatching {
                        val dir = java.io.File(filesDir, "modules")
                        java.io.File(dir, s.module.fileName).readText()
                    }.getOrElse { s.module.submodules.joinToString("\n\n") { "## ${it.title}\n${it.content}" } }
                    screen = Screen.Editor(s.module.fileName, txt, isNew = false)
                },
                onExport = {
                    val uri = ModuleRepository.exportFileUri(this, s.module.fileName)
                    val share = Intent(Intent.ACTION_SEND).apply {
                        type = if (s.module.fileName.endsWith(".md", true)) "text/markdown" else "text/plain"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startActivity(Intent.createChooser(share, "Compartir módulo"))
                }
            )
            is Screen.SubmoduleView -> SubmoduleScreen(
                moduleTitle = s.module.title,
                submodule = s.sub,
                onBack = { screen = Screen.ModuleDetail(s.module) }
            )
            is Screen.Editor -> EditorScreen(
                initialFileName = s.fileName,
                initialText = s.initialText,
                isNew = s.isNew,
                onBack = { screen = Screen.Library },
                onSave = { fileName, text ->
                    val op = if (s.isNew)
                        ModuleRepository.createNew(this, fileName, text)
                    else
                        ModuleRepository.save(this, fileName, text)
                    op.onSuccess {
                        refresh()
                        screen = Screen.ModuleDetail(it)
                    }.onFailure { }
                }
            )
        }
    }
}
