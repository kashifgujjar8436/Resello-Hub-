package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.model.AccountStatus
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    currentUser: UserProfile,
    allUsers: List<UserProfile>,
    onSwitchUser: (String) -> Unit,
    onOpenRegister: () -> Unit,
    onNavigateNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showUserMenu by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // App Logo & Tagline
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Resello Logo",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Resello Hub",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "PK",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Sell More. Earn More.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // User Role Switcher Dropdown & Notification Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onNavigateNotifications,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Box {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = when (currentUser.role) {
                                UserRole.RESELLER -> MaterialTheme.colorScheme.primaryContainer
                                UserRole.SELLER -> MaterialTheme.colorScheme.secondaryContainer
                                UserRole.ADMIN -> MaterialTheme.colorScheme.tertiaryContainer
                                UserRole.CUSTOMER -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            modifier = Modifier
                                .testTag("user_role_switcher_button")
                                .clickable { showUserMenu = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = when (currentUser.role) {
                                        UserRole.RESELLER -> Icons.Default.ShoppingBag
                                        UserRole.SELLER -> Icons.Default.Inventory2
                                        UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                        UserRole.CUSTOMER -> Icons.Default.Person
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(
                                        text = currentUser.name.take(10),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = currentUser.role.displayName,
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Switch profile",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showUserMenu,
                            onDismissRequest = { showUserMenu = false }
                        ) {
                            Text(
                                text = "Switch Demo Profile (Role):",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Divider()

                            allUsers.forEach { user ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text("${user.name} (${user.role.displayName})", fontWeight = FontWeight.SemiBold)
                                            Text(
                                                text = "Status: ${user.status.name} • ${user.city}",
                                                fontSize = 11.sp,
                                                color = if (user.status == AccountStatus.APPROVED) Color(0xFF16A34A) else Color(0xFFDC2626)
                                            )
                                        }
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = when (user.role) {
                                                UserRole.RESELLER -> Icons.Default.Person
                                                UserRole.SELLER -> Icons.Default.Store
                                                UserRole.ADMIN -> Icons.Default.VerifiedUser
                                                UserRole.CUSTOMER -> Icons.Default.ShoppingCart
                                            },
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        onSwitchUser(user.id)
                                        showUserMenu = false
                                    }
                                )
                            }

                            Divider()
                            DropdownMenuItem(
                                text = { Text("+ Register New Seller/Reseller", fontWeight = FontWeight.Bold) },
                                leadingIcon = { Icon(Icons.Default.PersonAdd, contentDescription = null) },
                                onClick = {
                                    showUserMenu = false
                                    onOpenRegister()
                                }
                            )
                        }
                    }
                }
            }

            // Approval Status Notice Banner if Pending
            if (currentUser.status == AccountStatus.PENDING_APPROVAL) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassEmpty,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Account Pending Admin Approval. Switch to Super Admin to approve your store.",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppBottomNav(
    navController: NavController,
    currentUserRole: UserRole,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == Routes.HOME,
            onClick = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.HOME) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_tab_home")
        )

        NavigationBarItem(
            selected = currentRoute == Routes.CATEGORIES,
            onClick = {
                navController.navigate(Routes.CATEGORIES) {
                    popUpTo(Routes.HOME) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Category, contentDescription = "Categories") },
            label = { Text("Categories", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_tab_categories")
        )

        NavigationBarItem(
            selected = currentRoute == Routes.ORDERS,
            onClick = {
                navController.navigate(Routes.ORDERS) {
                    popUpTo(Routes.HOME) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "Orders") },
            label = { Text("Orders", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_tab_orders")
        )

        NavigationBarItem(
            selected = currentRoute == Routes.WALLET,
            onClick = {
                navController.navigate(Routes.WALLET) {
                    popUpTo(Routes.HOME) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Wallet") },
            label = { Text("Wallet", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_tab_wallet")
        )

        NavigationBarItem(
            selected = currentRoute == Routes.PROFILE,
            onClick = {
                navController.navigate(Routes.PROFILE) {
                    popUpTo(Routes.HOME) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 11.sp) },
            modifier = Modifier.testTag("nav_tab_profile")
        )
    }
}
