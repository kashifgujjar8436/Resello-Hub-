package com.example.ui.screens

import android.net.Uri
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.*
import com.example.ui.theme.ReselloOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminApprovalsScreen(
    users: List<UserProfile>,
    products: List<Product>,
    orders: List<Order>,
    onApproveRejectUser: (userId: String, isApproved: Boolean) -> Unit,
    onApproveRejectListing: (productId: String, isApproved: Boolean) -> Unit,
    onReleaseEscrowPayout: (orderId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf("Listings") } // "Listings", "Escrow", "Users"
    var selectedListingFilter by remember { mutableStateOf("Pending") }
    var selectedUserFilter by remember { mutableStateOf("Pending") }

    val pendingListings = products.filter { it.listingStatus == ListingStatus.PENDING_APPROVAL }
    val approvedListings = products.filter { it.listingStatus == ListingStatus.APPROVED }
    val rejectedListings = products.filter { it.listingStatus == ListingStatus.REJECTED }

    val pendingUsers = users.filter { it.status == AccountStatus.PENDING_APPROVAL }
    val approvedUsers = users.filter { it.status == AccountStatus.APPROVED }
    val rejectedUsers = users.filter { it.status == AccountStatus.REJECTED }

    // Orders in escrow: Payment first landed in Admin Account
    val escrowOrders = orders.filter { it.adminAccountReceived }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Admin Control & Approvals",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "Admin Custody",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "Payment Phela Admin Account ma aie gi • 7-15 days transfer • 7 days return • Listings Admin approved",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Summary Metric Tiles
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFEFF6FF),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Pending Listings", fontSize = 10.sp, color = Color(0xFF1E40AF), fontWeight = FontWeight.Bold)
                        Text("${pendingListings.size} Items", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1D4ED8))
                        Text("Admin approved", fontSize = 9.sp, color = Color(0xFF1E40AF))
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Admin Escrow", fontSize = 10.sp, color = Color(0xFF92400E), fontWeight = FontWeight.Bold)
                        val totalAdminHolding = escrowOrders.sumOf { it.finalCustomerPayable }
                        Text("PKR ${totalAdminHolding.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFB45309))
                        Text("7-15 days release", fontSize = 9.sp, color = Color(0xFF92400E))
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Users Pending", fontSize = 10.sp, color = Color(0xFF166534), fontWeight = FontWeight.Bold)
                        Text("${pendingUsers.size} Users", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF15803D))
                        Text("Store KYC", fontSize = 9.sp, color = Color(0xFF166534))
                    }
                }
            }
        }

        // 3 Primary Section Switch Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedSection == "Listings",
                    onClick = { selectedSection = "Listings" },
                    label = { Text("Product Listings (${pendingListings.size})") },
                    leadingIcon = { Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.weight(1f).testTag("admin_tab_listings")
                )
                FilterChip(
                    selected = selectedSection == "Escrow",
                    onClick = { selectedSection = "Escrow" },
                    label = { Text("Admin Account & Returns") },
                    leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.weight(1.3f).testTag("admin_tab_escrow")
                )
                FilterChip(
                    selected = selectedSection == "Users",
                    onClick = { selectedSection = "Users" },
                    label = { Text("Users (${pendingUsers.size})") },
                    leadingIcon = { Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    modifier = Modifier.weight(1f).testTag("admin_tab_users")
                )
            }
        }

        when (selectedSection) {
            "Listings" -> {
                // Listing sub-filter
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedListingFilter == "Pending",
                            onClick = { selectedListingFilter = "Pending" },
                            label = { Text("Pending Review (${pendingListings.size})") },
                            modifier = Modifier.testTag("filter_listing_pending")
                        )
                        FilterChip(
                            selected = selectedListingFilter == "Approved",
                            onClick = { selectedListingFilter = "Approved" },
                            label = { Text("Live (${approvedListings.size})") },
                            modifier = Modifier.testTag("filter_listing_approved")
                        )
                        FilterChip(
                            selected = selectedListingFilter == "Rejected",
                            onClick = { selectedListingFilter = "Rejected" },
                            label = { Text("Rejected (${rejectedListings.size})") },
                            modifier = Modifier.testTag("filter_listing_rejected")
                        )
                    }
                }

                val currentListings = when (selectedListingFilter) {
                    "Pending" -> pendingListings
                    "Approved" -> approvedListings
                    else -> rejectedListings
                }

                if (currentListings.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.CheckCircleOutline, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No $selectedListingFilter Listings", fontWeight = FontWeight.Bold)
                                Text("Supplier uploads will appear here for Admin approval before going live.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                } else {
                    items(currentListings, key = { it.id }) { product ->
                        AdminProductListingCard(
                            product = product,
                            onApprove = { onApproveRejectListing(product.id, true) },
                            onReject = { onApproveRejectListing(product.id, false) }
                        )
                    }
                }
            }

            "Escrow" -> {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF3C7),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Admin Central Escrow & 7-15 Days Settlement Policy", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF78350F))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "1. Customer payment phela Admin k account ma aati hai.\n2. Customer k pas 7 days Easy Return window hoti hai.\n3. Delivery k 7 sa 15 days k baad Reseller ka net profit aur Seller ka wholesale account ma release hota hai.\n4. Agar customer return kare tou admin refund approve karta hai.",
                                fontSize = 11.sp,
                                color = Color(0xFF92400E),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                if (escrowOrders.isEmpty()) {
                    item {
                        Text("No orders currently under Admin escrow holding.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    items(escrowOrders, key = { it.orderId }) { order ->
                        AdminEscrowOrderCard(
                            order = order,
                            onReleasePayout = { onReleaseEscrowPayout(order.orderId) }
                        )
                    }
                }
            }

            "Users" -> {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedUserFilter == "Pending",
                            onClick = { selectedUserFilter = "Pending" },
                            label = { Text("Pending (${pendingUsers.size})") },
                            modifier = Modifier.testTag("admin_filter_pending")
                        )
                        FilterChip(
                            selected = selectedUserFilter == "Approved",
                            onClick = { selectedUserFilter = "Approved" },
                            label = { Text("Approved (${approvedUsers.size})") },
                            modifier = Modifier.testTag("admin_filter_approved")
                        )
                        FilterChip(
                            selected = selectedUserFilter == "Rejected",
                            onClick = { selectedUserFilter = "Rejected" },
                            label = { Text("Rejected (${rejectedUsers.size})") },
                            modifier = Modifier.testTag("admin_filter_rejected")
                        )
                    }
                }

                val currentUsers = when (selectedUserFilter) {
                    "Pending" -> pendingUsers
                    "Approved" -> approvedUsers
                    else -> rejectedUsers
                }

                if (currentUsers.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No $selectedUserFilter accounts", fontWeight = FontWeight.Bold)
                                Text("All new registrations will appear here for verification.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                } else {
                    items(currentUsers, key = { it.id }) { user ->
                        UserApprovalCard(
                            user = user,
                            onApprove = { onApproveRejectUser(user.id, true) },
                            onReject = { onApproveRejectUser(user.id, false) }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AdminProductListingCard(
    product: Product,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("listing_card_${product.id}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            product.imageUri != null -> {
                                AsyncImage(
                                    model = Uri.parse(product.imageUri),
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
                                Icon(Icons.Default.Inventory2, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Supplier: ${product.sellerName}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "Category: ${product.category} • SKU: ${product.sku}", fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (product.listingStatus) {
                        ListingStatus.APPROVED -> Color(0xFFDCFCE7)
                        ListingStatus.REJECTED -> Color(0xFFFEE2E2)
                        ListingStatus.PENDING_APPROVAL -> Color(0xFFFEF3C7)
                    }
                ) {
                    Text(
                        text = product.listingStatus.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (product.listingStatus) {
                            ListingStatus.APPROVED -> Color(0xFF166534)
                            ListingStatus.REJECTED -> Color(0xFF991B1B)
                            ListingStatus.PENDING_APPROVAL -> Color(0xFF92400E)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing & Strict Profit Compliance Row
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Manufacturing Cost:", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("PKR ${product.manufacturingCost.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Column {
                        Text("Seller Profit Margin:", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${product.sellerProfitPercentage.toInt()}% (Strict 5-20%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Wholesale Price:", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("PKR ${product.baseWholesalePrice.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = ReselloOrange)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Policy highlight: Return policy 7 days & Admin approval
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Return Policy: ${product.returnPolicyDays} Days Easy Return", fontSize = 11.sp, color = Color(0xFF0284C7), fontWeight = FontWeight.SemiBold)
                Text("Stock: ${product.stockQuantity} pcs", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (product.listingStatus == ListingStatus.PENDING_APPROVAL) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f).testTag("reject_listing_${product.id}")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reject Listing")
                    }

                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        modifier = Modifier.weight(1.4f).testTag("approve_listing_${product.id}")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Admin Approve Listing")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: ${product.listingStatus.label}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (product.listingStatus == ListingStatus.APPROVED) Color(0xFF16A34A) else Color(0xFFDC2626)
                    )

                    TextButton(onClick = { if (product.listingStatus == ListingStatus.APPROVED) onReject() else onApprove() }) {
                        Text(if (product.listingStatus == ListingStatus.APPROVED) "Revoke Listing" else "Re-Approve")
                    }
                }
            }
        }
    }
}

@Composable
fun AdminEscrowOrderCard(
    order: Order,
    onReleasePayout: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("escrow_order_${order.orderId}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Order #${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = "Tracking: ${order.trackingId}", fontSize = 11.sp, color = ReselloOrange, fontWeight = FontWeight.SemiBold)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (order.escrowStatus) {
                        EscrowTransferStatus.TRANSFERRED_TO_RESELLER_SELLER -> Color(0xFFDCFCE7)
                        EscrowTransferStatus.REFUNDED_RETURN_7_DAYS -> Color(0xFFFEE2E2)
                        else -> Color(0xFFFEF3C7)
                    }
                ) {
                    Text(
                        text = order.escrowStatus.label,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (order.escrowStatus) {
                            EscrowTransferStatus.TRANSFERRED_TO_RESELLER_SELLER -> Color(0xFF166534)
                            EscrowTransferStatus.REFUNDED_RETURN_7_DAYS -> Color(0xFF991B1B)
                            else -> Color(0xFF92400E)
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Escrow Payment Breakdown Card
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Customer Payment In Admin Account:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("PKR ${order.finalCustomerPayable.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Wholesale to Seller (${order.sellerName}):", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("PKR ${order.subtotalWholesale.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Reseller Net Profit (${order.resellerName}):", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("PKR ${order.totalResellerProfit.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Admin Platform 5% Commission:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("PKR ${order.appCommission.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ReselloOrange)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 7-15 Days Settlement & 7 Days Return Status Banner
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (order.returnRequested) Color(0xFFFEE2E2) else Color(0xFFEFF6FF),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (order.returnRequested) Icons.Default.AssignmentReturn else Icons.Default.Schedule,
                        contentDescription = null,
                        tint = if (order.returnRequested) Color(0xFFDC2626) else Color(0xFF1D4ED8),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (order.returnRequested) {
                            "7-Day Return Active: Refunded to Customer (${order.returnReason ?: "Customer request"})"
                        } else {
                            "Settlement Notice: ${order.transferScheduledDaysNotice}"
                        },
                        fontSize = 10.sp,
                        color = if (order.returnRequested) Color(0xFF991B1B) else Color(0xFF1E40AF),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!order.isProfitTransferred && !order.returnRequested) {
                Button(
                    onClick = onReleasePayout,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    modifier = Modifier.fillMaxWidth().testTag("release_escrow_${order.orderId}")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Release Funds to Reseller & Seller (7-15 Days Settlement)")
                }
            } else if (order.isProfitTransferred) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Funds Transferred from Admin Account (Ref: ${order.payoutReference ?: "ESCROW-CLEAR"})", fontSize = 11.sp, color = Color(0xFF166534), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun UserApprovalCard(
    user: UserProfile,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("user_approval_card_${user.id}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (user.role == UserRole.SELLER) MaterialTheme.colorScheme.secondaryContainer
                                else MaterialTheme.colorScheme.primaryContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (user.role == UserRole.SELLER) Icons.Default.Store else Icons.Default.Person,
                            contentDescription = null,
                            tint = if (user.role == UserRole.SELLER) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = user.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(text = user.businessName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (user.role == UserRole.SELLER) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = user.role.displayName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contact & Bank Details
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Phone / WhatsApp:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(user.phone, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Email & City:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${user.email} • ${user.city}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Direct Transfer Account:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("${user.bankTitle} - ${user.accountNumber}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (user.status == AccountStatus.PENDING_APPROVAL) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f).testTag("reject_btn_${user.id}")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reject")
                    }

                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        modifier = Modifier.weight(1.4f).testTag("approve_btn_${user.id}")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Approve Account")
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Current Status: ${user.status.name}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (user.status == AccountStatus.APPROVED) Color(0xFF16A34A) else Color(0xFFDC2626)
                    )

                    TextButton(onClick = { if (user.status == AccountStatus.APPROVED) onReject() else onApprove() }) {
                        Text(if (user.status == AccountStatus.APPROVED) "Revoke Access" else "Re-Approve")
                    }
                }
            }
        }
    }
}
