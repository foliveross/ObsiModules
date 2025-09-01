package cl.tu.nombre.obsimod.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import cl.tu.nombre.obsimod.data.Submodule
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun SubmoduleScreen(
    moduleTitle: String,
    submodule: Submodule,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$moduleTitle · ${submodule.title}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(submodule.title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            MarkdownText(markdown = submodule.content.ifBlank { "_Sin contenido._" })
        }
    }
}
