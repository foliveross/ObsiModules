package cl.tu.nombre.obsimod.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.tu.nombre.obsimod.data.Module
import cl.tu.nombre.obsimod.data.Submodule

@Composable
fun ModuleDetailScreen(
    module: Module,
    onBack: () -> Unit,
    onOpenSubmodule: (Submodule) -> Unit,
    onEdit: () -> Unit,
    onExport: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(module.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = onExport) { Text("Exportar") }
                    TextButton(onClick = onEdit) { Text("Editar") }
                }
            )
        }
    ) { inner ->
        LazyColumn(
            contentPadding = inner + PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(module.submodules) { s ->
                Card(Modifier.fillMaxWidth().clickable { onOpenSubmodule(s) }) {
                    Column(Modifier.padding(16.dp)) {
                        Text(s.title, style = MaterialTheme.typography.titleMedium)
                        val preview = s.content.lines().firstOrNull()?.take(120).orEmpty()
                        if (preview.isNotBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text(preview)
                        }
                    }
                }
            }
        }
    }
}
