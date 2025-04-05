package com.example.guest.ui.screens

import GuestDatabaseHelper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guest.data.Guest
import com.example.guest.data.GuestRepositorySQLiteImpl
import com.example.guest.viewmodel.GuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestListScreen(navController: NavController) {
    val dbHelper = GuestDatabaseHelper(LocalContext.current)
    val repository = GuestRepositorySQLiteImpl(dbHelper)
    val viewmodel = GuestViewModel(repository)
    val guestList by viewmodel.guests.collectAsState()
    var guestToEdit by remember { mutableStateOf<Guest?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Convidados") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile")
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                guestToEdit = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(guestList.size) { index ->
                GuestCard(
                    guest = guestList[index],
                    onDelete = { viewmodel.deleteGuest(guestList[index]) },
                    onEdit = {
                        guestToEdit = guestList[index]
                        showDialog = true
                    },
                    onConfirm = { viewmodel.updateGuest(it) }
                )
            }
        }
    }

    GuestDialogForm(
        isOpen = showDialog,
        onDismiss = { showDialog = false },
        onSave = { guest ->
            if (guestToEdit == null) {
                viewmodel.insertGuest(guest)
            } else {
                viewmodel.updateGuest(guest)
            }
            showDialog = false
        },
        guest = guestToEdit
    )
}

@Composable
fun GuestCard(
    guest: Guest,
    onDelete: (Guest) -> Unit,
    onEdit: (Guest) -> Unit,
    onConfirm: (Guest) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(guest.name, style = MaterialTheme.typography.titleMedium)
                Text(guest.email, style = MaterialTheme.typography.bodyMedium)
                Text(guest.phone, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(4.dp))


            }

            Row {
                IconButton(onClick = {
                    val updatedGuest = guest.copy(confirmed = !guest.confirmed)
                    onConfirm(updatedGuest)
                }) {
                    Icon(
                        imageVector = if (guest.confirmed) Icons.Filled.CheckCircle else Icons.Filled.Check,
                        contentDescription = if (guest.confirmed) "Confirmado" else "Confirmar"
                    )
                }

                IconButton(onClick = { onEdit(guest) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Alterar")
                }

                IconButton(onClick = { onDelete(guest) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        }
    }
}
