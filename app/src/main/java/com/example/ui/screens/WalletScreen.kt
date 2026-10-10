package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PaymentMethod
import com.example.model.PayoutTransaction
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.ReselloOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    currentUser: UserProfile,
    payouts: List<PayoutTransaction>,
    onTriggerWithdrawal: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    var showWithdrawDialog by remember { mutableStateOf(false) }
    var withdrawAmountInput by remember { mutableStateOf("5000") }
    var selectedWithdrawMethod by remember { mutableStateOf("Easypaisa Direct") }

    val userPayouts = payouts.filter {
        if (currentUser.role == UserRole.ADMIN) true
        else it.recipientId == currentUser.id || currentUser.id == "reseller-1"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Earnings & Wallet",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = "0% Platform Fee",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "Automated direct payouts to JazzCash, Easypaisa, Upaisa & Bank Account",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Live Balance Card in Resello Hub Signature Orange
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ReselloOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payout_wallet_card"),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Available For Payout (100% Free)",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Automated Payouts", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "PKR ${currentUser.walletAvailableBalance.toInt()}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Pending Settlement:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("PKR ${currentUser.walletPendingEarnings.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFEF08A))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total Earnings To Date:", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("PKR ${currentUser.totalEarnings.toInt()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Linked Account Strip
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.Black.copy(alpha = 0.15f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Linked Direct Payout Account:", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                                Text("${currentUser.bankTitle} (${currentUser.accountNumber})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF6EE7B7), modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { showWithdrawDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth().testTag("instant_transfer_now_btn")
                    ) {
                        Text("Instant Payout (JazzCash / Easypaisa / Upaisa)", color = ReselloOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Policy Note: Zero Reseller Fee + Seller 5%-20% Limit
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF00B14F), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Resello Hub Fair Commission & Profit Rules:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Payment Flow: Payment phela Admin k account ma aati hai, phir 7 sa 15 days k andar Reseller aur Seller k account ma transfer hoti hai.\n• 7 Days Return: Customer k pas 7 days easy return window hoti hai.\n• Zero Upfront Fee: Reseller se koi registration ya joining fees nahi li jati (5% platform commission strictly on order profit).\n• Wholesale Listings: Har nayi product listing Super Admin approve kare ga. Supplier profit margin strictly 5% se 20% limit hota hai.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        item {
            Text("Payout Transaction History:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        if (userPayouts.isEmpty()) {
            item {
                Text(
                    text = "No payout history yet. Deliver an order to see automated ledger entries.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(userPayouts, key = { it.id }) { payout ->
                PayoutLedgerItem(payout = payout)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showWithdrawDialog) {
        val methods = listOf("Easypaisa Direct", "JazzCash Mobile Account", "Upaisa Mobile Wallet", "Meezan Bank Ltd", "NayaPay / SadaPay")
        AlertDialog(
            onDismissRequest = { showWithdrawDialog = false },
            title = { Text("Instant Direct Payout") },
            text = {
                Column {
                    Text(
                        text = "Transfer channel muntakhab karein (0% Fee):",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    methods.forEach { m ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedWithdrawMethod = m }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(selected = selectedWithdrawMethod == m, onClick = { selectedWithdrawMethod = m })
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(m, fontSize = 12.sp, fontWeight = if (selectedWithdrawMethod == m) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = withdrawAmountInput,
                        onValueChange = { withdrawAmountInput = it },
                        label = { Text("Amount (PKR)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Available: PKR ${currentUser.walletAvailableBalance.toInt()} • Zero Deduction", fontSize = 11.sp, color = Color(0xFF00B14F), fontWeight = FontWeight.SemiBold)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = withdrawAmountInput.toDoubleOrNull() ?: 0.0
                        if (amount > 0) {
                            onTriggerWithdrawal(amount)
                            showWithdrawDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ReselloOrange)
                ) {
                    Text("Confirm Transfer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PayoutLedgerItem(payout: PayoutTransaction) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Transfer to ${payout.recipientName}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "${payout.channel} • ${payout.accountNumber}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = payout.note, fontSize = 10.sp, color = Color(0xFF047857))
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+PKR ${payout.amount.toInt()}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = Color(0xFF16A34A)
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = payout.status,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
