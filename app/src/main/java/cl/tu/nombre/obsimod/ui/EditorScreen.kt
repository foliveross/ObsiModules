package cl.tu.nombre.obsimod.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun EditorScreen(
    initialFileName: String,
    initialText: String,
    isNew: Boolean,
    onBack: () -> Unit,
    onSave: (fileName: String, text: String) -> Unit
) {
    var fileName by remember { mutableStateOf(initialFileName) }
    var text by remember { mutableStateOf(initialText) }
    val scroll = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNew) "Nuevo módulo" else "Editar módulo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = { TextButton(onClick = { onSave(fileName, text) }) { Text("Guardar") } }
            )
        }
    ) { inner ->
        Column(
            Modifier.padding(inner).padding(16.dp).fillMaxSize()
        ) {
            OutlinedTextField(
                value = fileName,
                onValueChange = { fileName = it },
                label = { Text("Nombre de archivo (.md)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            Text("Contenido (Markdown):", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.weight(1f).fillMaxWidth().verticalScroll(scroll)
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("Vista previa:", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            MarkdownText(markdown = text.ifBlank { "_(vacío)_" })
        }
    }
}
