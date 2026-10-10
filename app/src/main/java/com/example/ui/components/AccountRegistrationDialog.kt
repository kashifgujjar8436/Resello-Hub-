package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountRegistrationDialog(
    onDismiss: () -> Unit,
    onSubmitRegistration: (name: String, email: String, phone: String, role: UserRole, businessName: String, city: String, bankTitle: String, accountNum: String) -> Unit
) {
    var selectedRole by remember { mutableStateOf(UserRole.RESELLER) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Lahore") }
    var bankTitle by remember { mutableStateOf("Easypaisa Direct") }
    var accountNumber by remember { mutableStateOf("") }

    val bankOptions = listOf(
        "Easypaisa Direct",
        "JazzCash Instant",
        "Meezan Bank Ltd",
        "Habib Bank Limited (HBL)",
        "Bank Alfalah",
        "SadaPay",
        "NayaPay"
    )
    var bankDropdownExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Naya Account Banayein",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Seller / Reseller Registration (Admin Approval Required)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Role Selector (Reseller vs Seller)
                Text("Role Muntakhab Karein:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedRole == UserRole.RESELLER,
                        onClick = { selectedRole = UserRole.RESELLER },
                        label = { Text("Reseller (Zero Investment)") },
                        modifier = Modifier.testTag("role_chip_reseller")
                    )
                    FilterChip(
                        selected = selectedRole == UserRole.SELLER,
                        onClick = { selectedRole = UserRole.SELLER },
                        label = { Text("Supplier / Seller") },
                        modifier = Modifier.testTag("role_chip_seller")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Aapka Naam (Full Name)") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = { Text(if (selectedRole == UserRole.RESELLER) "Reseller Brand / Page Name" else "Wholesale Factory / Store Name") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_business_input"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("WhatsApp / Mobile Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth().testTag("reg_phone_input"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth().testTag("reg_email_input"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City (Shehar)") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_city_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Direct Payout Account Information (Easypaisa/JazzCash/Bank)
                Text(
                    text = "Direct Profit Transfer Account Details:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Customer ko parcel pohanchte he profit direct is account ma chala jaye ga.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Bank Selector
                ExposedDropdownMenuBox(
                    expanded = bankDropdownExpanded,
                    onExpandedChange = { bankDropdownExpanded = !bankDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = bankTitle,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Payment Method / Bank") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bankDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = bankDropdownExpanded,
                        onDismissRequest = { bankDropdownExpanded = false }
                    ) {
                        bankOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    bankTitle = option
                                    bankDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = accountNumber,
                    onValueChange = { accountNumber = it },
                    label = { Text("Account / Mobile Number (IBAN / Wallet)") },
                    placeholder = { Text("0300-1234567 ya Bank Account #") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_account_number_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank() && accountNumber.isNotBlank()) {
                            onSubmitRegistration(
                                name,
                                email.ifBlank { "$phone@markazhub.pk" },
                                phone,
                                selectedRole,
                                businessName.ifBlank { "$name Store" },
                                city,
                                bankTitle,
                                accountNumber
                            )
                            onDismiss()
                        }
                    },
                    enabled = name.isNotBlank() && phone.isNotBlank() && accountNumber.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_registration_btn")
                ) {
                    Text("Register Account (Admin Approval)")
                }
            }
        }
    }
}
