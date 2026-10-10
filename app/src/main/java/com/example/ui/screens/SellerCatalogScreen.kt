package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.AccountStatus
import com.example.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerCatalogScreen(
    currentUser: UserProfile,
    onAddProduct: (title: String, desc: String, category: String, manufacturingCost: Double, sellerProfitPercentage: Double, suggestedRetail: Double, stock: Int, deliveryDays: String, imageUri: String?, drawableRes: Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Electronics") }
    var manufacturingCostInput by remember { mutableStateOf("1200") }
    var sellerProfitPercentageInput by remember { mutableStateOf("15") } // Strictly between 5% and 20%
    var suggestedRetailInput by remember { mutableStateOf("2200") }
    var stockInput by remember { mutableStateOf("50") }
    var deliveryDays by remember { mutableStateOf("2-3 Days") }

    // Selected picture state (Uri from Photo Picker or template preset)
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedPresetDrawable by remember { mutableStateOf<Int?>(R.drawable.img_prod_airbuds_1791424221642) }

    // Android Standard zero-permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            selectedPresetDrawable = null
        }
    }

    val categories = listOf("Electronics", "Fashion", "Mobile Accessories", "Home & Kitchen", "Accessories", "Health & Personal Care")
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    val manufacturingCost = manufacturingCostInput.toDoubleOrNull() ?: 0.0
    val rawProfitPct = sellerProfitPercentageInput.toDoubleOrNull() ?: 10.0
    val clampedProfitPct = rawProfitPct.coerceIn(5.0, 20.0)
    val calculatedWholesalePrice = manufacturingCost * (1.0 + (clampedProfitPct / 100.0))
    val sellerProfitAmount = calculatedWholesalePrice - manufacturingCost

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = "Supplier Wholesale Inventory",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Product photo, manufacturing cost aur 5% sa 20% profit margin set karein",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (currentUser.status == AccountStatus.PENDING_APPROVAL) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Account Approval Pending",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Apka Supplier store abhi Admin review ma hai. Super Admin profile switch kr k approval dekh sakte hain.",
                        fontSize = 12.sp,
                        color = Color(0xFF78350F)
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Naya Wholesale Product & Tasweer Upload",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Product Picture Upload Area
                Text(
                    text = "Cheez Ki Tasweer (Product Image):",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .border(
                            width = 1.5.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .testTag("upload_product_image_box"),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        selectedImageUri != null -> {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Uploaded Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.PhotoCamera, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Change Photo", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        selectedPresetDrawable != null -> {
                            Image(
                                painter = painterResource(id = selectedPresetDrawable!!),
                                contentDescription = "Preset Product Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Upload Custom Photo", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Gallery se Tasweer Muntakhab Karein",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Tap to choose product photo from device",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Sample preset chips
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sample Presets:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    AssistChip(
                        onClick = {
                            selectedPresetDrawable = R.drawable.img_prod_airbuds_1791424221642
                            selectedImageUri = null
                        },
                        label = { Text("AirBuds", fontSize = 11.sp) }
                    )
                    AssistChip(
                        onClick = {
                            selectedPresetDrawable = R.drawable.img_prod_lawnsuit_1791424236063
                            selectedImageUri = null
                        },
                        label = { Text("Lawn Suit", fontSize = 11.sp) }
                    )
                    AssistChip(
                        onClick = {
                            selectedPresetDrawable = R.drawable.img_hero_banner_1791423199027
                            selectedImageUri = null
                        },
                        label = { Text("Smartwatch", fontSize = 11.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Product Title / Item Name") },
                    placeholder = { Text("e.g. Wireless ANC Earbuds") },
                    modifier = Modifier.fillMaxWidth().testTag("seller_product_title"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = categoryDropdownExpanded,
                    onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Product Details & Features") },
                    placeholder = { Text("Material, size, warranty, battery etc.") },
                    modifier = Modifier.fillMaxWidth().testTag("seller_product_desc"),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.height(12.dp))

                // STRICT SELLER PROFIT RANGE (5% to 20%) & MANUFACTURING COST
                Text(
                    text = "Seller / Supplier Cost & Profit (5% to 20% limit):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = manufacturingCostInput,
                        onValueChange = { manufacturingCostInput = it },
                        label = { Text("Manufacturing Cost") },
                        leadingIcon = { Text("PKR", modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1.2f).testTag("seller_manufacturing_cost"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = sellerProfitPercentageInput,
                        onValueChange = { sellerProfitPercentageInput = it },
                        label = { Text("Profit (5% to 20%)") },
                        trailingIcon = { Text("%", modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Bold) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("seller_profit_percentage"),
                        singleLine = true,
                        isError = rawProfitPct < 5.0 || rawProfitPct > 20.0,
                        supportingText = {
                            if (rawProfitPct < 5.0 || rawProfitPct > 20.0) {
                                Text("Must be between 5% and 20%", color = MaterialTheme.colorScheme.error)
                            } else {
                                Text("Allowed range: 5% - 20%")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Live Wholesale Price & Seller Margin Summary Banner
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEFF6FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Calculated Wholesale Price:", fontSize = 11.sp, color = Color(0xFF1E40AF))
                            Text("PKR ${calculatedWholesalePrice.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1D4ED8))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Seller Profit Per Unit:", fontSize = 11.sp, color = Color(0xFF1E40AF))
                            Text("PKR ${sellerProfitAmount.toInt()} (${clampedProfitPct.toInt()}%)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = suggestedRetailInput,
                        onValueChange = { suggestedRetailInput = it },
                        label = { Text("Suggested Retail (PKR)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("seller_product_retail"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = stockInput,
                        onValueChange = { stockInput = it },
                        label = { Text("Stock (Pcs)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("seller_product_stock"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Admin approval notification notice
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFEF3C7),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin Approval Rule: Har nayi product listing submit hone k baad Super Admin approve kare ga. Approve hone k baad Reseller catalog ma show hogi.",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val cost = manufacturingCostInput.toDoubleOrNull() ?: 0.0
                        val retail = suggestedRetailInput.toDoubleOrNull() ?: (calculatedWholesalePrice * 1.4)
                        val stock = stockInput.toIntOrNull() ?: 50
                        if (title.isNotBlank() && cost > 0 && rawProfitPct in 5.0..20.0) {
                            onAddProduct(
                                title,
                                description.ifBlank { "High quality wholesale supply item from ${currentUser.businessName}" },
                                category,
                                cost,
                                clampedProfitPct,
                                retail,
                                stock,
                                deliveryDays,
                                selectedImageUri?.toString(),
                                selectedPresetDrawable
                            )
                            title = ""
                            description = ""
                            manufacturingCostInput = ""
                            suggestedRetailInput = ""
                            selectedImageUri = null
                        }
                    },
                    enabled = title.isNotBlank() && manufacturingCost > 0 && rawProfitPct in 5.0..20.0 && currentUser.status == AccountStatus.APPROVED,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("publish_product_btn")
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Admin Approval K Liye Bhejein (${clampedProfitPct.toInt()}% Profit)")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
