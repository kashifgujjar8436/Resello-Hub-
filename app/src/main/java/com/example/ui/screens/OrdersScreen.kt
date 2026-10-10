package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.model.*
import com.example.ui.theme.ReselloOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    currentUser: UserProfile,
    orders: List<Order>,
    onSelectOrderForTracking: (String) -> Unit,
    onUpdateOrderStatus: (String, OrderStatus) -> Unit,
    onRequestReturn: (orderId: String, reason: String) -> Unit = { _, _ -> },
    onReleaseEscrowPayout: (orderId: String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Pending", "Processing", "Shipped", "Delivered", "Cancelled", "Returned")

    var showReturnDialogForOrderId by remember { mutableStateOf<String?>(null) }
    var returnReasonInput by remember { mutableStateOf("Size/quality defect (7-day easy return)") }

    val visibleOrders = orders.filter { ord ->
        when (currentUser.role) {
            UserRole.RESELLER -> ord.resellerId == currentUser.id || currentUser.id == "reseller-1"
            UserRole.SELLER -> ord.sellerId == currentUser.id || currentUser.id == "seller-1"
            UserRole.ADMIN -> true
            UserRole.CUSTOMER -> true
        }
    }.filter { ord ->
        when (selectedFilter) {
            "Pending" -> ord.status == OrderStatus.PENDING || ord.status == OrderStatus.CONFIRMED
            "Processing" -> ord.status == OrderStatus.PROCESSING || ord.status == OrderStatus.PACKED
            "Shipped" -> ord.status == OrderStatus.SHIPPED || ord.status == OrderStatus.OUT_FOR_DELIVERY
            "Delivered" -> ord.status == OrderStatus.DELIVERED
            "Cancelled" -> ord.status == OrderStatus.CANCELLED
            "Returned" -> ord.status == OrderStatus.RETURNED || ord.returnRequested
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Orders & Bookings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFEFF6FF)
                    ) {
                        Text(
                            text = "Admin Escrow Custody",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D4ED8),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "Payment Phela Admin Account ma aie gi • 7-15 Days ma Reseller/Seller transfer • 7 Days Return",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Policy Banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFEF3C7),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Payment & 7-Day Return Guarantee", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF92400E))
                        Text(
                            text = "Customer payment admin account ma mehfooz hoti hai. 7 din ki return policy k baad 7-15 din k andar reseller profit & supplier wholesale transfer hoti hai.",
                            fontSize = 11.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        // Filter chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                filters.take(5).forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 11.sp) },
                        modifier = Modifier.testTag("order_filter_$filter")
                    )
                }
            }
        }

        if (visibleOrders.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No orders in $selectedFilter", fontWeight = FontWeight.Bold)
                        Text("Share Resello Hub products on WhatsApp to start earning.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        } else {
            items(visibleOrders, key = { it.orderId }) { order ->
                OrderItemCard(
                    order = order,
                    currentUserRole = currentUser.role,
                    onTrackClick = { onSelectOrderForTracking(order.trackingId) },
                    onAdvanceStatusClick = { newStatus -> onUpdateOrderStatus(order.orderId, newStatus) },
                    onRequestReturnClick = { showReturnDialogForOrderId = order.orderId },
                    onReleaseEscrowClick = { onReleaseEscrowPayout(order.orderId) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // 7-Day Easy Return Dialog
    showReturnDialogForOrderId?.let { orderId ->
        AlertDialog(
            onDismissRequest = { showReturnDialogForOrderId = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AssignmentReturn, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("7-Day Easy Return Sahulat")
                }
            },
            text = {
                Column {
                    Text(
                        text = "Resello Hub 7-Day Return Policy k mutabiq parcel receive hone k 7 din k andar return claim kiya ja sakta hai. Payment Admin account se customer ko refund hogi.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = returnReasonInput,
                        onValueChange = { returnReasonInput = it },
                        label = { Text("Return Ki Waja (Reason)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRequestReturn(orderId, returnReasonInput)
                        showReturnDialogForOrderId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Submit 7-Day Return")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReturnDialogForOrderId = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun OrderItemCard(
    order: Order,
    currentUserRole: UserRole,
    onTrackClick: () -> Unit,
    onAdvanceStatusClick: (OrderStatus) -> Unit,
    onRequestReturnClick: () -> Unit,
    onReleaseEscrowClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.orderId}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header with Order # and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = order.orderId, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(text = "Tracking: ${order.trackingId}", fontSize = 11.sp, color = ReselloOrange, fontWeight = FontWeight.SemiBold)
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (order.status) {
                        OrderStatus.DELIVERED -> Color(0xFFDCFCE7)
                        OrderStatus.SHIPPED, OrderStatus.OUT_FOR_DELIVERY -> Color(0xFFE0F2FE)
                        OrderStatus.CANCELLED, OrderStatus.RETURNED -> Color(0xFFFEE2E2)
                        else -> Color(0xFFFEF3C7)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    when (order.status) {
                                        OrderStatus.DELIVERED -> Color(0xFF16A34A)
                                        OrderStatus.SHIPPED, OrderStatus.OUT_FOR_DELIVERY -> Color(0xFF0284C7)
                                        OrderStatus.CANCELLED, OrderStatus.RETURNED -> Color(0xFFDC2626)
                                        else -> Color(0xFFD97706)
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = order.status.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (order.status) {
                                OrderStatus.DELIVERED -> Color(0xFF166534)
                                OrderStatus.SHIPPED, OrderStatus.OUT_FOR_DELIVERY -> Color(0xFF0369A1)
                                OrderStatus.CANCELLED, OrderStatus.RETURNED -> Color(0xFF991B1B)
                                else -> Color(0xFF92400E)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Customer summary & Payment Method Badge
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${order.customerName} (${order.customerPhone})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = order.paymentMethod.title.take(16),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Address: ${order.customerAddress}, ${order.customerArea}, ${order.customerCity}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Items breakdown
            order.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.quantity}x ${item.product.title.take(24)}...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "PKR ${item.totalCustomerPrice.toInt()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Admin Escrow & 7-15 Days Settlement Badge
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = when {
                    order.returnRequested -> Color(0xFFFEE2E2)
                    order.isProfitTransferred -> Color(0xFFF0FDF4)
                    else -> Color(0xFFFEF3C7)
                },
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    when {
                        order.returnRequested -> Color(0xFFFCA5A5)
                        order.isProfitTransferred -> Color(0xFFBBF7D0)
                        else -> Color(0xFFFDE68A)
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Reseller Net Profit:", fontSize = 11.sp, color = Color(0xFF166534))
                            Text(
                                text = "PKR ${order.totalResellerProfit.toInt()}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF15803D)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = order.escrowStatus.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    order.returnRequested -> Color(0xFFDC2626)
                                    order.isProfitTransferred -> Color(0xFF15803D)
                                    else -> Color(0xFFB45309)
                                }
                            )
                            Text(
                                text = "Admin Escrow (7-15 Days Transfer)",
                                fontSize = 10.sp,
                                color = Color(0xFF1E40AF),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (order.returnRequested) {
                            "Return reason: ${order.returnReason ?: "Customer requested return"}"
                        } else {
                            "Notice: ${order.transferScheduledDaysNotice}"
                        },
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onTrackClick,
                    modifier = Modifier.weight(1f).testTag("track_btn_${order.orderId}")
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live Tracking", fontSize = 11.sp)
                }

                // 7 Days Easy Return button available when Delivered or Shipped
                if (order.status == OrderStatus.DELIVERED && !order.returnRequested) {
                    OutlinedButton(
                        onClick = onRequestReturnClick,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0284C7)),
                        modifier = Modifier.weight(1.1f).testTag("return_btn_${order.orderId}")
                    ) {
                        Icon(Icons.Default.AssignmentReturn, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("7-Day Return", fontSize = 11.sp)
                    }
                }

                if (order.status != OrderStatus.DELIVERED && order.status != OrderStatus.RETURNED) {
                    val nextStatus = when (order.status) {
                        OrderStatus.PENDING -> OrderStatus.CONFIRMED
                        OrderStatus.CONFIRMED -> OrderStatus.PROCESSING
                        OrderStatus.PROCESSING -> OrderStatus.PACKED
                        OrderStatus.PACKED -> OrderStatus.SHIPPED
                        OrderStatus.SHIPPED -> OrderStatus.OUT_FOR_DELIVERY
                        OrderStatus.OUT_FOR_DELIVERY -> OrderStatus.DELIVERED
                        else -> OrderStatus.DELIVERED
                    }

                    FilledTonalButton(
                        onClick = { onAdvanceStatusClick(nextStatus) },
                        modifier = Modifier.weight(1.3f).testTag("advance_status_btn_${order.orderId}")
                    ) {
                        Text(
                            text = if (nextStatus == OrderStatus.DELIVERED) "Mark Delivered" else "Next -> ${nextStatus.name}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (order.status == OrderStatus.DELIVERED && currentUserRole == UserRole.ADMIN && !order.isProfitTransferred && !order.returnRequested) {
                    Button(
                        onClick = onReleaseEscrowClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        modifier = Modifier.weight(1.4f)
                    ) {
                        Text("Release (7-15 Days)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
