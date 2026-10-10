package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.model.PaymentMethod
import com.example.navigation.Routes
import com.example.ui.components.AccountRegistrationDialog
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppHeader
import com.example.ui.components.ResellerProfitMarginDialog
import com.example.ui.screens.AdminApprovalsScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SellerCatalogScreen
import com.example.ui.screens.TrackingScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.ReselloHubTheme
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReselloHubTheme {
                MainAppNavHost(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppNavHost(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val payouts by viewModel.payouts.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val searchTrackingId by viewModel.searchTrackingId.collectAsStateWithLifecycle()
    val selectedProductForResell by viewModel.selectedProductForResell.collectAsStateWithLifecycle()
    val profitMarginInput by viewModel.resellerProfitMarginInput.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showRegistrationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppHeader(
                currentUser = currentUser,
                allUsers = allUsers,
                onSwitchUser = { viewModel.switchUser(it) },
                onOpenRegister = { showRegistrationDialog = true },
                onNavigateNotifications = { navController.navigate(Routes.NOTIFICATIONS) }
            )
        },
        bottomBar = {
            AppBottomNav(
                navController = navController,
                currentUserRole = currentUser.role
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. HOME SCREEN (Catalog, Trending, Banners, Search)
                composable(Routes.HOME) {
                    HomeScreen(
                        currentUser = currentUser,
                        banners = viewModel.banners,
                        products = products,
                        onResellProductClick = { viewModel.openResellerShareModal(it) },
                        onCategoryClick = { categoryName ->
                            navController.navigate(Routes.categoryProducts(categoryName))
                        },
                        onNavigateOrders = { navController.navigate(Routes.ORDERS) },
                        onNavigateWallet = { navController.navigate(Routes.WALLET) }
                    )
                }

                // 2. CATEGORIES SCREEN
                composable(Routes.CATEGORIES) {
                    CategoriesScreen(
                        onCategorySelected = { categoryName ->
                            navController.navigate(Routes.categoryProducts(categoryName))
                        }
                    )
                }

                // CATEGORY FILTERED PRODUCTS
                composable(
                    route = Routes.CATEGORY_PRODUCTS,
                    arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                    HomeScreen(
                        currentUser = currentUser,
                        banners = viewModel.banners,
                        products = products.filter { it.category.equals(categoryName, ignoreCase = true) },
                        onResellProductClick = { viewModel.openResellerShareModal(it) },
                        onCategoryClick = { /* already filtered */ },
                        onNavigateOrders = { navController.navigate(Routes.ORDERS) },
                        onNavigateWallet = { navController.navigate(Routes.WALLET) }
                    )
                }

                // 3. ORDERS SCREEN
                composable(Routes.ORDERS) {
                    OrdersScreen(
                        currentUser = currentUser,
                        orders = orders,
                        onSelectOrderForTracking = { trackingId ->
                            viewModel.setTrackingSearch(trackingId)
                            navController.navigate(Routes.tracking(trackingId))
                        },
                        onUpdateOrderStatus = { orderId, status ->
                            viewModel.updateOrderStatus(orderId, status)
                        },
                        onRequestReturn = { orderId, reason ->
                            viewModel.requestOrderReturn(orderId, reason)
                        },
                        onReleaseEscrowPayout = { orderId ->
                            viewModel.releaseEscrowPayout(orderId)
                        }
                    )
                }

                // 4. LIVE COURIER TRACKING SCREEN
                composable(
                    route = Routes.TRACKING,
                    arguments = listOf(navArgument("trackingId") {
                        type = NavType.StringType
                        defaultValue = ""
                    })
                ) { backStackEntry ->
                    val trackingArg = backStackEntry.arguments?.getString("trackingId") ?: ""
                    TrackingScreen(
                        orders = orders,
                        searchTrackingId = if (trackingArg.isNotBlank()) trackingArg else searchTrackingId,
                        onSearchTrackingIdChange = { viewModel.setTrackingSearch(it) },
                        onUpdateOrderStatus = { orderId, status ->
                            viewModel.updateOrderStatus(orderId, status)
                        }
                    )
                }

                // 5. WALLET & PAYOUT LEDGER SCREEN
                composable(Routes.WALLET) {
                    WalletScreen(
                        currentUser = currentUser,
                        payouts = payouts,
                        onTriggerWithdrawal = { amount ->
                            viewModel.triggerDirectInstantWithdrawal(amount)
                        }
                    )
                }

                // 6. PROFILE SCREEN
                composable(Routes.PROFILE) {
                    ProfileScreen(
                        currentUser = currentUser,
                        onNavigateOrders = { navController.navigate(Routes.ORDERS) },
                        onNavigateWallet = { navController.navigate(Routes.WALLET) },
                        onNavigateAdmin = { navController.navigate(Routes.ADMIN_PANEL) },
                        onNavigateSellerCatalog = { navController.navigate(Routes.SELLER_CATALOG) }
                    )
                }

                // 7. NOTIFICATIONS SCREEN
                composable(Routes.NOTIFICATIONS) {
                    NotificationsScreen(notifications = notifications)
                }

                // 8. WHOLESALE SUPPLIER INVENTORY SCREEN (Strict 5% - 20% Seller Margin)
                composable(Routes.SELLER_CATALOG) {
                    SellerCatalogScreen(
                        currentUser = currentUser,
                        onAddProduct = { title, desc, cat, cost, profitPct, retail, stock, days, imageUri, drawableRes ->
                            viewModel.addSellerProduct(title, desc, cat, cost, profitPct, retail, stock, days, imageUri, drawableRes)
                            navController.navigate(Routes.ADMIN_PANEL)
                        }
                    )
                }

                // 9. SUPER ADMIN APPROVALS PANEL
                composable(Routes.ADMIN_PANEL) {
                    AdminApprovalsScreen(
                        users = allUsers,
                        products = products,
                        orders = orders,
                        onApproveRejectUser = { userId, isApproved ->
                            viewModel.approveOrRejectUser(userId, isApproved)
                        },
                        onApproveRejectListing = { productId, isApproved ->
                            viewModel.approveOrRejectListing(productId, isApproved)
                        },
                        onReleaseEscrowPayout = { orderId ->
                            viewModel.releaseEscrowPayout(orderId)
                        }
                    )
                }
            }
        }
    }

    // Profit margin customization & COD / JazzCash / Easypaisa / Upaisa booking modal
    selectedProductForResell?.let { product ->
        ResellerProfitMarginDialog(
            product = product,
            currentMarginInput = profitMarginInput,
            onMarginChange = { viewModel.updateResellerProfitMargin(it) },
            onDismiss = { viewModel.closeResellerModal() },
            onConfirmOrder = { quantity, margin, name, phone, address, city, area, method, notes ->
                viewModel.placeCustomerOrder(
                    product = product,
                    quantity = quantity,
                    margin = margin,
                    custName = name,
                    custPhone = phone,
                    custAddress = address,
                    custCity = city,
                    custArea = area,
                    paymentMethod = method,
                    orderNotes = notes
                )
                navController.navigate(Routes.TRACKING)
            }
        )
    }

    // Account Registration with Admin Approval Dialog
    if (showRegistrationDialog) {
        AccountRegistrationDialog(
            onDismiss = { showRegistrationDialog = false },
            onSubmitRegistration = { name, email, phone, role, biz, city, bank, acct ->
                viewModel.registerNewAccount(name, email, phone, role, biz, city, bank, acct)
            }
        )
    }
}
