package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CategoryItem(
    val name: String,
    val icon: ImageVector,
    val count: String
)

@Composable
fun CategoriesScreen(
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryList = listOf(
        CategoryItem("Electronics", Icons.Default.Devices, "240+ Products"),
        CategoryItem("Fashion", Icons.Default.Checkroom, "650+ Products"),
        CategoryItem("Mobile Accessories", Icons.Default.PhoneAndroid, "410+ Products"),
        CategoryItem("Home & Kitchen", Icons.Default.Kitchen, "180+ Products"),
        CategoryItem("Health & Personal Care", Icons.Default.Spa, "120+ Products"),
        CategoryItem("Beauty & Skincare", Icons.Default.Face, "310+ Products"),
        CategoryItem("Accessories", Icons.Default.Watch, "190+ Products"),
        CategoryItem("Baby Products", Icons.Default.ChildFriendly, "95+ Products"),
        CategoryItem("Grocery", Icons.Default.LocalGroceryStore, "150+ Products"),
        CategoryItem("Trending Products", Icons.Default.LocalFireDepartment, "80+ Products")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)) {
            Text(
                text = "Product Categories",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Browse wholesale Pakistani supplier catalogs by category",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(categoryList) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelected(item.name) }
                        .testTag("cat_card_${item.name}"),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = item.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                        Text(
                            text = item.count,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
