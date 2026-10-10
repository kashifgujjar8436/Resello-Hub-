package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.model.Order
import com.example.model.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(
    orders: List<Order>,
    searchTrackingId: String,
    onSearchTrackingIdChange: (String) -> Unit,
    onUpdateOrderStatus: (String, OrderStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputQuery by remember { mutableStateOf(searchTrackingId) }

    LaunchedEffect(searchTrackingId) {
        inputQuery = searchTrackingId
    }

    val matchedOrder = remember(inputQuery, orders) {
        if (inputQuery.isBlank()) orders.firstOrNull()
        else orders.find {
            it.trackingId.contains(inputQuery.trim(), ignoreCase = true) ||
            it.orderId.contains(inputQuery.trim(), ignoreCase = true) ||
            it.customerPhone.contains(inputQuery.trim(), ignoreCase = true)
        } ?: orders.firstOrNull()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = "Live Parcel Tracking (Customer Sahulat)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Track real-time shipment journey from warehouse to customer doorstep",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Search Input
        OutlinedTextField(
            value = inputQuery,
            onValueChange = {
                inputQuery = it
                onSearchTrackingIdChange(it)
            },
            label = { Text("Tracking #, Order # or Customer Phone") },
            placeholder = { Text("e.g. TRX-782910-PK") },
            leadingIcon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
            trailingIcon = {
                if (inputQuery.isNotBlank()) {
                    IconButton(onClick = {
                        inputQuery = ""
                        onSearchTrackingIdChange("")
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tracking_search_input"),
            singleLine = true
        )

        // Quick Tracking Pill Suggestions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            orders.take(3).forEach { ord ->
                SuggestionChip(
                    onClick = {
                        inputQuery = ord.trackingId
                        onSearchTrackingIdChange(ord.trackingId)
                    },
                    label = { Text(ord.trackingId.take(12), fontSize = 11.sp) }
                )
            }
        }

        if (matchedOrder != null) {
            // Main Tracking Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tracking_details_card"),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Courier and Tracking Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = matchedOrder.courierPartner,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = matchedOrder.trackingId,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (matchedOrder.status) {
                                OrderStatus.DELIVERED -> Color(0xFFDCFCE7)
                                OrderStatus.SHIPPED, OrderStatus.OUT_FOR_DELIVERY -> Color(0xFFE0F2FE)
                                else -> Color(0xFFFEF3C7)
                            }
                        ) {
                            Text(
                                text = matchedOrder.status.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (matchedOrder.status) {
                                    OrderStatus.DELIVERED -> Color(0xFF166534)
                                    OrderStatus.SHIPPED, OrderStatus.OUT_FOR_DELIVERY -> Color(0xFF0369A1)
                                    else -> Color(0xFF92400E)
                                },
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Destination Info
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Destination: ${matchedOrder.customerCity} (${matchedOrder.customerName})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Text(
                                text = matchedOrder.customerAddress,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(start = 22.dp, top = 2.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Customer COD Payable: Rs ${matchedOrder.finalCustomerPayable.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                Text("Reseller: ${matchedOrder.resellerName.take(15)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Shipment Milestone Timeline:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Milestones Timeline
                    val milestones = listOf(
                        Triple(OrderStatus.PENDING, "Order Placed & Confirmed", "Reseller booked order on Resello Hub system"),
                        Triple(OrderStatus.CONFIRMED, "Confirmed by Supplier", "Wholesale inventory reserved by ${matchedOrder.sellerName}"),
                        Triple(OrderStatus.PACKED, "Packed in Parcel", "Boxed with Reseller Brand tag and COD slip"),
                        Triple(OrderStatus.SHIPPED, "In Transit", "Dispatched via ${matchedOrder.courierPartner} Hub"),
                        Triple(OrderStatus.OUT_FOR_DELIVERY, "Out for Delivery", "Rider assigned for delivery in ${matchedOrder.customerCity}"),
                        Triple(OrderStatus.DELIVERED, "Delivered & Payment Received", "Customer paid. Net profit transferred to account!")
                    )

                    val currentStep = matchedOrder.status.stepIndex

                    milestones.forEachIndexed { index, milestone ->
                        val isReached = currentStep >= milestone.first.stepIndex
                        val isCurrent = currentStep == milestone.first.stepIndex

                        TimelineStepItem(
                            isReached = isReached,
                            isCurrent = isCurrent,
                            isLast = index == milestones.lastIndex,
                            title = milestone.second,
                            description = milestone.third
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated live update button for demo
                    if (matchedOrder.status != OrderStatus.DELIVERED) {
                        Button(
                            onClick = {
                                val nextStatus = when (matchedOrder.status) {
                                    OrderStatus.PENDING -> OrderStatus.CONFIRMED
                                    OrderStatus.CONFIRMED -> OrderStatus.PROCESSING
                                    OrderStatus.PROCESSING -> OrderStatus.PACKED
                                    OrderStatus.PACKED -> OrderStatus.SHIPPED
                                    OrderStatus.SHIPPED -> OrderStatus.OUT_FOR_DELIVERY
                                    OrderStatus.OUT_FOR_DELIVERY -> OrderStatus.DELIVERED
                                    else -> OrderStatus.DELIVERED
                                }
                                onUpdateOrderStatus(matchedOrder.orderId, nextStatus)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("simulate_tracking_progress_btn")
                        ) {
                            Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simulate Courier Movement to Next Hub")
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEFF6FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFF1D4ED8))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Parcel Delivered • Payment in Admin Escrow Account", fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF), fontSize = 13.sp)
                                    Text("Customer has 7 Days Easy Return window. Payout of PKR ${matchedOrder.totalResellerProfit.toInt()} will be transferred to Reseller & Seller in 7 to 15 days.", fontSize = 11.sp, color = Color(0xFF1E3A8A))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun TimelineStepItem(
    isReached: Boolean,
    isCurrent: Boolean,
    isLast: Boolean,
    title: String,
    description: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCurrent -> MaterialTheme.colorScheme.primary
                            isReached -> Color(0xFF16A34A)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isReached) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(if (isReached) Color(0xFF16A34A) else MaterialTheme.colorScheme.surfaceVariant)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 12.dp)) {
            Text(
                text = title,
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 13.sp,
                color = if (isReached) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
