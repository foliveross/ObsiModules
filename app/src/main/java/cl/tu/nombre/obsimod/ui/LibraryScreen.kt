package cl.tu.nombre.obsimod.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.tu.nombre.obsimod.data.Module

@Composable
fun LibraryScreen(
    modules: List<Module>,
    onImportClick: () -> Unit,
    onModuleClick: (Module) -> Unit,
    onNewClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val filtered = remember(modules, query) {
        if (query.isBlank()) modules
        else modules.filter {
            it.title.contains(query, true) ||
            it.submodules.any { s -> s.title.contains(query, true) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Biblioteca") },
                actions = {
                    TextButton(onClick = onImportClick) { Text("Importar") }
                    Spacer(Modifier.width(4.dp))
                    TextButton(onClick = onNewClick) { Text("Nuevo") }
                }
            )
        }
    ) { inner ->
        Column(Modifier.padding(inner).padding(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Buscar módulos…") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            if (filtered.isEmpty()) {
                Text("No hay módulos. Toca “Importar” o “Nuevo”.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtered) { m -> ModuleCard(m) { onModuleClick(m) } }
                }
            }
        }
    }
}

@Composable
private fun ModuleCard(module: Module, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp)) {
            Text(module.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text("${module.submodules.size} submódulo(s)")
            Spacer(Modifier.height(4.dp))
            Text("Archivo: ${module.fileName}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
