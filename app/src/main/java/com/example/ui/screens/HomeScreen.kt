package com.example.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.BannerItem
import com.example.model.ListingStatus
import com.example.model.Product
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.ReselloOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentUser: UserProfile,
    banners: List<BannerItem>,
    products: List<Product>,
    onResellProductClick: (Product) -> Unit,
    onCategoryClick: (String) -> Unit,
    onNavigateOrders: () -> Unit,
    onNavigateWallet: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Electronics", "Fashion", "Mobile Accessories", "Home & Kitchen", "Accessories", "Health & Personal Care")

    val filteredProducts = products.filter { prod ->
        // Only show Admin approved listings in public catalog (or allow seller/admin to preview)
        (prod.listingStatus == ListingStatus.APPROVED || currentUser.role == UserRole.ADMIN || (currentUser.role == UserRole.SELLER && prod.sellerId == currentUser.id)) &&
        (selectedCategory == "All" || prod.category == selectedCategory) &&
        (searchQuery.isBlank() || prod.title.contains(searchQuery, ignoreCase = true) || prod.category.contains(searchQuery, ignoreCase = true))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // High Quality Resello Hub Top Search & Greeting Bar
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Resello Hub",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = ReselloOrange
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Text(
                                    text = "0% Reseller Fee",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Sell More. Earn More. • Zero Stock Dropshipping Pakistan",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.clickable { onNavigateWallet() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PKR ${currentUser.walletAvailableBalance.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }

        // Resello Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search products, electronics, brand lawn...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ReselloOrange) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    } else {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Search with image", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("catalog_search_bar"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ReselloOrange,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )
        }

        // Hero Mega Sale Carousel Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_resello_mega_banner_1791538537491),
                        contentDescription = "Resello Hub Mega Resell Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xCC0F1E36)),
                                    startY = 40f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ReselloOrange
                        ) {
                            Text(
                                text = "Zero Joining Fee • 5% App Commission",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Resello Hub Wholesale Supply",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Admin Account Escrow • 7-15 Days Payouts • 7 Days Return",
                            color = Color(0xFFFFDBCD),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // 4 Resello Quick Shortcut Feature Pills
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReselloFeatureBadge(
                    title = "7 Days Return",
                    subtitle = "Easy Return Sahulat",
                    icon = Icons.Default.AssignmentReturn,
                    color = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f)
                )
                ReselloFeatureBadge(
                    title = "Admin Escrow",
                    subtitle = "7-15 Days Transfer",
                    icon = Icons.Default.AccountBalance,
                    color = Color(0xFF16A34A),
                    modifier = Modifier.weight(1f)
                )
                ReselloFeatureBadge(
                    title = "Admin Approved",
                    subtitle = "Verified Listings",
                    icon = Icons.Default.Verified,
                    color = ReselloOrange,
                    modifier = Modifier.weight(1f)
                )
                ReselloFeatureBadge(
                    title = "COD Parcel",
                    subtitle = "Pay upon delivery",
                    icon = Icons.Default.LocalShipping,
                    color = Color(0xFF1E3A8A),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Category Pills
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = {
                            selectedCategory = cat
                            if (cat != "All") onCategoryClick(cat)
                        },
                        label = { Text(cat, fontSize = 12.sp) },
                        modifier = Modifier.testTag("cat_chip_$cat")
                    )
                }
            }
        }

        // Section Title: Flash Sale & Trending Mall Products
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = ReselloOrange, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Trending Wholesale Supply", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Text("View All", fontSize = 12.sp, color = ReselloOrange, fontWeight = FontWeight.Bold)
            }
        }

        // High Quality Resello Hub Product Cards
        items(filteredProducts, key = { it.id }) { product ->
            ReselloProductCard(
                product = product,
                onResellClick = { onResellProductClick(product) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ReselloFeatureBadge(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = subtitle, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
        }
    }
}

@Composable
fun ReselloProductCard(
    product: Product,
    onResellClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
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
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Official Resello Mall Badge
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    color = Color(0xFFD03F00),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = "Resello Mall",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Supplier Verified Profit (Strict 5% to 20%)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Supplier: ${product.sellerProfitPercentage.toInt()}% Profit",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6EE7B7),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                // Sold Count Badge at Bottom Left
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = product.soldCount,
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                // Store Name & Ratings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.sellerName,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB800), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${product.rating} (${product.reviewsCount})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Price and Reseller Earning Highlight
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFF7ED),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFEDD5)),
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
                            Text("Wholesale Qemat:", fontSize = 10.sp, color = Color(0xFF9A3412))
                            Text(
                                text = "PKR ${product.baseWholesalePrice.toInt()}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ReselloOrange
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Retail Market Price:", fontSize = 10.sp, color = Color(0xFF9A3412))
                            Text(
                                text = "PKR ${product.suggestedRetailPrice.toInt()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val estMargin = (product.suggestedRetailPrice - product.baseWholesalePrice).toInt()
                            Text(
                                text = "Est Profit: PKR $estMargin (100% Free)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00B14F)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF00B14F))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "0% Platform Fee", fontSize = 11.sp, color = Color(0xFF00B14F), fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onResellClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ReselloOrange),
                        modifier = Modifier.testTag("resell_btn_${product.id}")
                    ) {
                        Icon(Icons.Default.Sell, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Apna Profit Lagayein", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
