package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.PaymentMethod
import com.example.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResellerProfitMarginDialog(
    product: Product,
    currentMarginInput: String,
    onMarginChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirmOrder: (quantity: Int, margin: Double, name: String, phone: String, address: String, city: String, area: String, method: PaymentMethod, notes: String) -> Unit
) {
    val context = LocalContext.current
    var quantity by remember { mutableStateOf(1) }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var customerAddress by remember { mutableStateOf("") }
    var customerCity by remember { mutableStateOf("Lahore") }
    var customerArea by remember { mutableStateOf("Gulberg") }
    var selectedPaymentMethod by remember { mutableStateOf(PaymentMethod.COD) }
    var orderNotes by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) } // 1: Margin & WhatsApp Share, 2: Customer Checkout & Payment Method

    val marginValue = currentMarginInput.toDoubleOrNull() ?: 0.0
    val customerItemPrice = product.baseWholesalePrice + marginValue
    val grossResellerProfit = marginValue * quantity
    val appCommission = grossResellerProfit * 0.05 // 5% App Commission just for reseller
    val netResellerProfit = grossResellerProfit - appCommission // 95% net profit to reseller
    val deliveryFee = 150.0
    val totalCustomerPayable = (customerItemPrice * quantity) + deliveryFee

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
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (step == 1) "Apna Profit Tay Karein" else "Customer Checkout",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Text(
                                    text = "5% App Commission",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = if (step == 1) "Zero upfront fee! App ka 5% commission just reseller k liye" else "COD Parcel 📦, JazzCash, Easypaisa, Upaisa Support",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Product summary banner with thumbnail
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                product.imageUri != null -> {
                                    AsyncImage(
                                        model = android.net.Uri.parse(product.imageUri),
                                        contentDescription = product.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                product.drawableRes != null -> {
                                    Image(
                                        painter = painterResource(id = product.drawableRes),
                                        contentDescription = product.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                else -> {
                                    Icon(
                                        imageVector = Icons.Default.Sell,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.title,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Wholesale Supplier: PKR ${product.baseWholesalePrice.toInt()} (Supplier Profit: ${product.sellerProfitPercentage.toInt()}%)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (step == 1) {
                    // Margin input
                    Text(
                        text = "1. Reseller Profit Margin (Per Item):",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = currentMarginInput,
                        onValueChange = { onMarginChange(it) },
                        label = { Text("Your Margin (PKR)") },
                        leadingIcon = {
                            Text("PKR", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp), fontSize = 12.sp)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reseller_margin_input_field"),
                        singleLine = true,
                        supportingText = {
                            Text("Suggested Retail: PKR ${product.suggestedRetailPrice.toInt()} • 100% Free: No deduction")
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quantity selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Order Quantity:", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalIconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(
                                text = "$quantity",
                                modifier = Modifier.padding(horizontal = 14.dp),
                                fontWeight = FontWeight.Bold
                            )
                            FilledTonalIconButton(
                                onClick = { if (quantity < product.stockQuantity) quantity++ },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Profit & Reseller Fee Breakdown
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF0FDF4),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Customer Item Price:", fontSize = 12.sp, color = Color(0xFF065F46))
                                Text("PKR ${customerItemPrice.toInt()} / unit", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Delivery Courier Charges:", fontSize = 12.sp, color = Color(0xFF065F46))
                                Text("PKR ${deliveryFee.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF065F46))
                            }
                            Divider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFA7F3D0))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Customer Total Bill:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF064E3B))
                                Text("PKR ${totalCustomerPayable.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF064E3B))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Reseller Registration Fee:", fontSize = 11.sp, color = Color(0xFF047857))
                                Text("PKR 0 (Free)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Gross Profit Margin:", fontSize = 11.sp, color = Color(0xFF047857))
                                Text("PKR ${grossResellerProfit.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF047857))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("App Commission (5% just for reseller):", fontSize = 11.sp, color = Color(0xFFB45309))
                                Text("-PKR ${appCommission.toInt()} (5%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Aapka Net Munafa (Direct Wallet):", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Color(0xFF047857))
                                Text("PKR ${netResellerProfit.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF15803D))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Social Media & WhatsApp Share Button
                    OutlinedButton(
                        onClick = {
                            val shareText = """
                                🛍️ *${product.title}*
                                🏷️ Qemat: PKR ${customerItemPrice.toInt()}
                                🚚 Cash on Delivery (COD) & Online Payment available!
                                📦 Delivery: ${product.deliveryDays}
                                
                                Order book karwane k liye apna Naam, Number aur Pata send karein!
                            """.trimIndent()

                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share product via WhatsApp / Social Media")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp / Social Media Par Share Karein")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { step = 2 },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("continue_to_customer_address_btn")
                    ) {
                        Text("Customer Order Book Karein (Next)")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                } else {
                    // Step 2: Customer Address, Notes & Payment Method (COD, JazzCash, Easypaisa, Upaisa)
                    Text("Payment Method Muntakhab Karein:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        PaymentMethod.values().forEach { method ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedPaymentMethod == method) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedPaymentMethod = method }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedPaymentMethod == method,
                                        onClick = { selectedPaymentMethod = method }
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(text = method.title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(text = method.subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Customer Ka Naam") },
                        placeholder = { Text("e.g. Asad Khan") },
                        modifier = Modifier.fillMaxWidth().testTag("customer_name_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Customer Mobile (03xx-xxxxxxx)") },
                        placeholder = { Text("0300-1234567") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("customer_phone_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = customerCity,
                            onValueChange = { customerCity = it },
                            label = { Text("City (Shehar)") },
                            modifier = Modifier.weight(1f).testTag("customer_city_input"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = customerArea,
                            onValueChange = { customerArea = it },
                            label = { Text("Area / Mohalla") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = customerAddress,
                        onValueChange = { customerAddress = it },
                        label = { Text("Complete Delivery Address") },
                        placeholder = { Text("House #, Street #, Landmark") },
                        modifier = Modifier.fillMaxWidth().testTag("customer_address_input"),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = orderNotes,
                        onValueChange = { orderNotes = it },
                        label = { Text("Order Notes (Optional)") },
                        placeholder = { Text("e.g. Call before delivery") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { step = 1 },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Back")
                        }

                        Button(
                            onClick = {
                                if (customerName.isNotBlank() && customerPhone.isNotBlank() && customerAddress.isNotBlank()) {
                                    onConfirmOrder(
                                        quantity,
                                        marginValue,
                                        customerName,
                                        customerPhone,
                                        customerAddress,
                                        customerCity,
                                        customerArea,
                                        selectedPaymentMethod,
                                        orderNotes
                                    )
                                }
                            },
                            enabled = customerName.isNotBlank() && customerPhone.isNotBlank() && customerAddress.isNotBlank(),
                            modifier = Modifier
                                .weight(1.8f)
                                .testTag("confirm_place_order_btn")
                        ) {
                            Text(if (selectedPaymentMethod == PaymentMethod.COD) "Book COD Order" else "Pay & Book Order")
                        }
                    }
                }
            }
        }
    }
}
