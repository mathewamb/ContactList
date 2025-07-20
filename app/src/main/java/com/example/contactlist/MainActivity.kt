package com.example.contactlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contactlist.model.Contact
import com.example.contactlist.model.utility.formatPhone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContent {
            ContactsApp()
        }
    }
}

@Composable
fun ContactsApp() {
    var contacts by remember { mutableStateOf(listOf<Contact>()) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var showError by remember { mutableStateOf(false) }

    // Load initial data with coroutine (LaunchedEffect)
    LaunchedEffect(Unit) {
        contacts = loadContacts()
        isLoading = false
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Input fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            showError = true
                        } else {
                            contacts = contacts + Contact(name, phone.takeIf { it.isNotBlank() })
                            name = ""
                            phone = ""
                            showError = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Contact")
                }
                if (showError) {
                    Text("Name is required", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.height(16.dp))

                // List of contacts
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn {
                        items(contacts) { contact ->
                            ContactRow(contact)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactRow(contact: Contact) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = contact.name, style = MaterialTheme.typography.titleMedium)
        Text(
            text = contact.phone?.formatPhone() ?: "No Phone",
            style = MaterialTheme.typography.bodyMedium
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}

// Simulate async loading (would be from a DB or network in real app)
suspend fun loadContacts(): List<Contact> = withContext(Dispatchers.IO) {
    delay(1000)
    listOf(
        Contact("A", "1234567890"),
        Contact("B", null)
    )
}

@Preview(showBackground = true)
@Composable
fun ContactsAppPreview() {
    ContactsApp()
}

@Preview(showBackground = true)
@Composable
fun ContactRowPreview() {
    val contact = Contact("A", "1234567890")
    ContactRow(contact)
}