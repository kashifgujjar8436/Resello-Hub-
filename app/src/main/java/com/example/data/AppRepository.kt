package com.example.data

import com.example.model.*
import com.example.service.PaymentResult
import com.example.service.ReselloPaymentProvider
import com.example.service.TraxLeopardDeliveryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class AppRepository {

    private val paymentProvider = ReselloPaymentProvider()
    private val deliveryProvider = TraxLeopardDeliveryProvider()

    // Registered Users
    private val _users = MutableStateFlow<List<UserProfile>>(listOf(
        UserProfile(
            id = "admin-1",
            name = "Resello HQ Operations",
            email = "admin@resellohub.pk",
            phone = "0300-0000000",
            role = UserRole.ADMIN,
            status = AccountStatus.APPROVED,
            businessName = "Resello Hub Central Management",
            city = "Lahore",
            walletAvailableBalance = 125000.0,
            walletPendingEarnings = 18000.0,
            totalEarnings = 560000.0,
            withdrawnAmount = 435000.0
        ),
        UserProfile(
            id = "seller-1",
            name = "Al-Madina Flagship Store",
            email = "madina.trade@gmail.com",
            phone = "0301-8877665",
            role = UserRole.SELLER,
            status = AccountStatus.APPROVED,
            businessName = "Al-Madina Verified Garments & Tech Hub",
            city = "Faisalabad",
            bankTitle = "Meezan Bank Ltd",
            accountNumber = "0102-0100989201",
            walletAvailableBalance = 42500.0,
            walletPendingEarnings = 8400.0,
            totalEarnings = 184000.0,
            withdrawnAmount = 133100.0,
            positiveSellerRating = "99%",
            responseRate = "100%",
            shipOnTime = "99%"
        ),
        UserProfile(
            id = "reseller-1",
            name = "Zainab Boutique Store",
            email = "zainab.shop@gmail.com",
            phone = "0321-4567890",
            role = UserRole.RESELLER,
            status = AccountStatus.APPROVED,
            businessName = "Zainab Official Reseller Hub",
            city = "Karachi",
            bankTitle = "Easypaisa Direct",
            accountNumber = "0321-4567890",
            walletAvailableBalance = 16800.0,
            walletPendingEarnings = 4200.0,
            totalEarnings = 62000.0,
            withdrawnAmount = 45200.0
        ),
        UserProfile(
            id = "reseller-pending-1",
            name = "Hamza Modern Hub",
            email = "hamza.resell@yahoo.com",
            phone = "0333-1122334",
            role = UserRole.RESELLER,
            status = AccountStatus.PENDING_APPROVAL,
            businessName = "Hamza Collection",
            city = "Rawalpindi",
            bankTitle = "JazzCash Wallet",
            accountNumber = "0333-1122334",
            walletAvailableBalance = 0.0,
            walletPendingEarnings = 0.0,
            totalEarnings = 0.0,
            withdrawnAmount = 0.0
        ),
        UserProfile(
            id = "seller-pending-1",
            name = "Kashmir Fabric Mill Outlet",
            email = "kashmir.textiles@outlook.com",
            phone = "0345-9988776",
            role = UserRole.SELLER,
            status = AccountStatus.PENDING_APPROVAL,
            businessName = "Kashmir Shawls Flagship",
            city = "Peshawar",
            bankTitle = "Bank Alfalah",
            accountNumber = "5544-22110099",
            walletAvailableBalance = 0.0,
            walletPendingEarnings = 0.0,
            totalEarnings = 0.0,
            withdrawnAmount = 0.0
        )
    ))
    val users: StateFlow<List<UserProfile>> = _users.asStateFlow()

    // Resello Hub promotional banners
    val banners: List<BannerItem> = listOf(
        BannerItem(
            id = "b1",
            title = "Zero Joining Fee • 5% App Commission",
            subtitle = "Reseller se koi registration fees nahi li jati! 5% app commission par munafa aapka",
            promoBadge = "Zero Joining Fee",
            actionText = "Start Earning"
        ),
        BannerItem(
            id = "b2",
            title = "Resello Hub Wholesale Supply",
            subtitle = "Supplier profit strict 5% to 20% limit par verified",
            promoBadge = "100% Authentic",
            actionText = "View Wholesale Supply"
        ),
        BannerItem(
            id = "b3",
            title = "COD & Instant Mobile Wallet",
            subtitle = "JazzCash, Easypaisa, Upaisa & Cash on Delivery",
            promoBadge = "Fast Payout",
            actionText = "Explore Deals"
        )
    )

    // Products
    private val _products = MutableStateFlow<List<Product>>(listOf(
        Product(
            id = "prod-1",
            title = "Wireless AirBuds Pro ANC with Digital Battery Display",
            description = "High fidelity audio with Active Noise Cancellation, 35h battery life, touch control, instant pairing with Android & iOS. Resello Hub verified authentic.",
            category = "Electronics",
            manufacturingCost = 1260.0,
            sellerProfitPercentage = 15.0, // 15% Seller Profit (strictly 5% - 20%)
            baseWholesalePrice = 1450.0,
            suggestedRetailPrice = 2499.0,
            stockQuantity = 120,
            sellerId = "seller-1",
            sellerName = "Al-Madina Flagship Store",
            rating = 4.9,
            reviewsCount = 428,
            soldCount = "2.8k+ Sold",
            sku = "RSH-BUDS-PRO",
            deliveryDays = "2-3 Days Delivery",
            drawableRes = com.example.R.drawable.img_prod_airbuds_1791424221642,
            tags = listOf("Resello Mall", "Zero Reseller Fee", "High Profit", "COD Available")
        ),
        Product(
            id = "prod-2",
            title = "Embroidered 3-Piece Lawn Suit (Summer Collection)",
            description = "Pure breathable lawn fabric with heavy thread embroidery neckline, printed dupatta, and dyed trousers. Guaranteed 100% fast selling customer choice.",
            category = "Fashion",
            manufacturingCost = 1850.0,
            sellerProfitPercentage = 13.5, // 13.5% Seller Profit (strictly 5% - 20%)
            baseWholesalePrice = 2100.0,
            suggestedRetailPrice = 3600.0,
            stockQuantity = 85,
            sellerId = "seller-1",
            sellerName = "Al-Madina Flagship Store",
            rating = 4.8,
            reviewsCount = 312,
            soldCount = "1.5k+ Sold",
            sku = "RSH-LAWN-3PC",
            deliveryDays = "2-4 Days Delivery",
            drawableRes = com.example.R.drawable.img_prod_lawnsuit_1791424236063,
            tags = listOf("Resello Mall", "Best Seller", "Women Clothing", "Free Shipping")
        ),
        Product(
            id = "prod-3",
            title = "Smart Fitness Smartwatch Series 9 (Heart & Sleep Monitor)",
            description = "Full HD curve display, Bluetooth calling, IP68 water resistant, 100+ sports modes, official Pakistan brand warranty.",
            category = "Mobile Accessories",
            manufacturingCost = 2450.0,
            sellerProfitPercentage = 14.3, // 14.3% Seller Profit (strictly 5% - 20%)
            baseWholesalePrice = 2800.0,
            suggestedRetailPrice = 4500.0,
            stockQuantity = 64,
            sellerId = "seller-1",
            sellerName = "Al-Madina Flagship Store",
            rating = 4.7,
            reviewsCount = 189,
            soldCount = "950+ Sold",
            sku = "RSH-WATCH-S9",
            deliveryDays = "2-3 Days Delivery",
            drawableRes = com.example.R.drawable.img_hero_banner_1791423199027,
            tags = listOf("Resello Mall", "Flash Sale", "Gadgets", "Trending")
        ),
        Product(
            id = "prod-4",
            title = "Premium Leather Minimalist Men's RFID Wallet",
            description = "Genuine grain cowhide leather, 8 card slots, dual currency compartments, slim fit front pocket luxury design.",
            category = "Accessories",
            manufacturingCost = 650.0,
            sellerProfitPercentage = 15.4, // 15.4% Seller Profit (strictly 5% - 20%)
            baseWholesalePrice = 750.0,
            suggestedRetailPrice = 1499.0,
            stockQuantity = 140,
            sellerId = "seller-1",
            sellerName = "Al-Madina Flagship Store",
            rating = 4.9,
            reviewsCount = 240,
            soldCount = "1.1k+ Sold",
            sku = "RSH-WAL-LEATH",
            deliveryDays = "1-3 Days Delivery",
            tags = listOf("Resello Mall", "Zero Reseller Fee", "Pure Leather", "Gift Box"),
            listingStatus = ListingStatus.APPROVED
        ),
        Product(
            id = "prod-5",
            title = "Kitchen Quick Electric Multi-Chopper & Meat Mincer (2L)",
            description = "Stainless steel 4 bi-level blades, 300W pure copper motor, shockproof glass bowl, chops meat, onion and herbs in seconds.",
            category = "Home & Kitchen",
            manufacturingCost = 1700.0,
            sellerProfitPercentage = 14.7, // 14.7% Seller Profit (strictly 5% - 20%)
            baseWholesalePrice = 1950.0,
            suggestedRetailPrice = 3200.0,
            stockQuantity = 55,
            sellerId = "seller-1",
            sellerName = "Al-Madina Flagship Store",
            rating = 4.6,
            reviewsCount = 98,
            soldCount = "620+ Sold",
            sku = "RSH-CHOP-2L",
            deliveryDays = "2-3 Days Delivery",
            tags = listOf("Resello Mall", "Kitchen Essential", "Household"),
            listingStatus = ListingStatus.APPROVED
        ),
        Product(
            id = "prod-pending-1",
            title = "Kashmir Hand-Woven Pashmina Wool Shawl",
            description = "100% fine spun Pashmina wool, traditional floral embroidery, winter warm luxury shawl direct from artisan weavers.",
            category = "Fashion",
            manufacturingCost = 2800.0,
            sellerProfitPercentage = 16.0,
            baseWholesalePrice = 3248.0,
            suggestedRetailPrice = 5400.0,
            stockQuantity = 40,
            sellerId = "seller-pending-1",
            sellerName = "Kashmir Fabric Mill Outlet",
            sku = "RSH-SHAWL-PASH",
            deliveryDays = "3-4 Days Delivery",
            tags = listOf("New Listing", "Pending Admin Approval", "Winter Special"),
            listingStatus = ListingStatus.PENDING_APPROVAL
        )
    ))
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Orders with ZERO reseller fee (Reseller retains 100% of profit)
    private val _orders = MutableStateFlow<List<Order>>(listOf(
        Order(
            orderId = "ORD-94821",
            trackingId = "TRX-782910-PK",
            resellerId = "reseller-1",
            resellerName = "Zainab Boutique Store",
            sellerId = "seller-1",
            sellerName = "Al-Madina Flagship Store",
            customerName = "Muhammad Asad",
            customerPhone = "0300-7654321",
            customerAddress = "House 42-B, Sector G-11/2",
            customerCity = "Islamabad",
            customerArea = "Sector G-11",
            customerProvince = "Federal Capital",
            courierPartner = "Trax Express",
            paymentMethod = PaymentMethod.COD,
            isPaymentPaid = false,
            items = listOf(
                OrderItem(
                    product = _products.value[0],
                    quantity = 1,
                    wholesalePrice = 1450.0,
                    resellerMarginPerItem = 750.0,
                    customerPricePerItem = 2200.0
                )
            ),
            subtotalWholesale = 1450.0,
            totalResellerProfit = 750.0, // 100% full profit (Rs 0 fee deducted!)
            deliveryFee = 150.0,
            finalCustomerPayable = 2350.0,
            status = OrderStatus.SHIPPED,
            isProfitTransferred = false,
            escrowStatus = EscrowTransferStatus.HELD_IN_ADMIN_ACCOUNT,
            adminAccountReceived = true,
            returnWindowDays = 7,
            transferScheduledDaysNotice = "Payment held in Admin account. Transfers in 7-15 days after delivery."
        ),
        Order(
            orderId = "ORD-89302",
            trackingId = "LEO-991024-PK",
            resellerId = "reseller-1",
            resellerName = "Zainab Boutique Store",
            sellerId = "seller-1",
            sellerName = "Al-Madina Flagship Store",
            customerName = "Ayesha Malik",
            customerPhone = "0331-5544332",
            customerAddress = "Flat 12, Gulshan-e-Iqbal Block 5",
            customerCity = "Karachi",
            customerArea = "Gulshan-e-Iqbal",
            customerProvince = "Sindh",
            courierPartner = "Leopard Courier",
            paymentMethod = PaymentMethod.JAZZCASH,
            isPaymentPaid = true,
            items = listOf(
                OrderItem(
                    product = _products.value[1],
                    quantity = 2,
                    wholesalePrice = 2100.0,
                    resellerMarginPerItem = 900.0,
                    customerPricePerItem = 3000.0
                )
            ),
            subtotalWholesale = 4200.0,
            totalResellerProfit = 1800.0, // 100% full profit (Rs 0 fee deducted!)
            deliveryFee = 150.0,
            finalCustomerPayable = 6150.0,
            status = OrderStatus.DELIVERED,
            isProfitTransferred = true,
            payoutReference = "AUTO-JC-994821",
            escrowStatus = EscrowTransferStatus.TRANSFERRED_TO_RESELLER_SELLER,
            adminAccountReceived = true,
            returnWindowDays = 7,
            transferScheduledDaysNotice = "Transferred after 7-15 days escrow clearance"
        )
    ))
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Automatic Direct Payout Transactions Ledger
    private val _payouts = MutableStateFlow<List<PayoutTransaction>>(listOf(
        PayoutTransaction(
            id = "PAY-1001",
            recipientId = "reseller-1",
            recipientName = "Zainab Boutique Store",
            role = UserRole.RESELLER,
            amount = 1800.0, // 100% full profit without any reseller fee!
            channel = "Easypaisa Direct Instant Transfer",
            accountNumber = "0321-4567890",
            status = "COMPLETED",
            orderId = "ORD-89302",
            note = "100% Direct profit credit (Zero fee deduction for reseller)"
        ),
        PayoutTransaction(
            id = "PAY-1002",
            recipientId = "seller-1",
            recipientName = "Al-Madina Flagship Store",
            role = UserRole.SELLER,
            amount = 4200.0,
            channel = "Meezan Bank Ltd",
            accountNumber = "0102-0100989201",
            status = "COMPLETED",
            orderId = "ORD-89302",
            note = "Automated wholesale price release after customer delivered"
        )
    ))
    val payouts: StateFlow<List<PayoutTransaction>> = _payouts.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<AppNotification>>(listOf(
        AppNotification(
            id = "notif-1",
            title = "Order ORD-89302 Delivered!",
            message = "Your 100% full profit PKR 1,800 (Zero fee deduction) was automatically credited to your Easypaisa account."
        ),
        AppNotification(
            id = "notif-2",
            title = "Welcome to Resello Hub!",
            message = "Discover wholesale products and earn 100% of your profit margin with zero fees!"
        )
    ))
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    // Current Session User
    private val _currentUser = MutableStateFlow<UserProfile>(_users.value[2]) // Default to Reseller Zainab for rich demo
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    fun switchUser(userId: String) {
        val found = _users.value.find { it.id == userId }
        if (found != null) {
            _currentUser.value = found
        }
    }

    fun registerUser(
        name: String,
        email: String,
        phone: String,
        role: UserRole,
        businessName: String,
        city: String,
        bankTitle: String,
        accountNumber: String
    ): UserProfile {
        val newUser = UserProfile(
            id = "usr-${UUID.randomUUID().toString().take(6)}",
            name = name,
            email = email,
            phone = phone,
            role = role,
            status = AccountStatus.PENDING_APPROVAL,
            businessName = businessName,
            city = city,
            bankTitle = bankTitle,
            accountNumber = accountNumber,
            referralCode = "REF-${(1000..9999).random()}",
            walletAvailableBalance = 0.0,
            walletPendingEarnings = 0.0,
            totalEarnings = 0.0,
            withdrawnAmount = 0.0
        )
        _users.value = _users.value + newUser
        return newUser
    }

    fun updateAccountApproval(userId: String, isApproved: Boolean) {
        _users.value = _users.value.map { user ->
            if (user.id == userId) {
                user.copy(status = if (isApproved) AccountStatus.APPROVED else AccountStatus.REJECTED)
            } else {
                user
            }
        }
        if (_currentUser.value.id == userId) {
            _users.value.find { it.id == userId }?.let { _currentUser.value = it }
        }
    }

    fun addProduct(
        seller: UserProfile,
        title: String,
        description: String,
        category: String,
        manufacturingCost: Double,
        sellerProfitPercentage: Double, // Strictly clamped between 5% and 20%
        suggestedRetailPrice: Double,
        stock: Int,
        deliveryDays: String,
        imageUri: String? = null,
        drawableRes: Int? = null
    ) {
        val clampedSellerProfitPct = sellerProfitPercentage.coerceIn(5.0, 20.0)
        val calculatedWholesalePrice = manufacturingCost * (1.0 + (clampedSellerProfitPct / 100.0))

        val newProd = Product(
            id = "prod-${UUID.randomUUID().toString().take(6)}",
            title = title,
            description = description,
            category = category,
            manufacturingCost = manufacturingCost,
            sellerProfitPercentage = clampedSellerProfitPct,
            baseWholesalePrice = calculatedWholesalePrice,
            suggestedRetailPrice = suggestedRetailPrice,
            stockQuantity = stock,
            sellerId = seller.id,
            sellerName = seller.name,
            sku = "RSH-${(1000..9999).random()}",
            deliveryDays = deliveryDays,
            imageUri = imageUri,
            drawableRes = drawableRes,
            listingStatus = ListingStatus.PENDING_APPROVAL // Listing approved by Admin before going live
        )
        _products.value = listOf(newProd) + _products.value
    }

    // Admin approves or rejects wholesale product listings
    fun updateListingApproval(productId: String, isApproved: Boolean) {
        _products.value = _products.value.map { prod ->
            if (prod.id == productId) {
                prod.copy(listingStatus = if (isApproved) ListingStatus.APPROVED else ListingStatus.REJECTED)
            } else {
                prod
            }
        }
    }

    // 7 Days Easy Return workflow
    fun requestOrderReturn(orderId: String, reason: String) {
        val order = _orders.value.find { it.orderId == orderId } ?: return
        val updatedOrder = order.copy(
            status = OrderStatus.RETURNED,
            returnRequested = true,
            returnReason = reason,
            escrowStatus = EscrowTransferStatus.REFUNDED_RETURN_7_DAYS
        )
        _orders.value = _orders.value.map { if (it.orderId == orderId) updatedOrder else it }
    }

    // Release escrow funds after 7-15 days or admin trigger
    fun releaseEscrowPayout(orderId: String) {
        val order = _orders.value.find { it.orderId == orderId } ?: return
        if (order.isProfitTransferred) return

        val payoutRef = "ESCROW-REL-${(100000..999999).random()}"
        val updatedOrder = order.copy(
            isProfitTransferred = true,
            payoutReference = payoutRef,
            isPaymentPaid = true,
            escrowStatus = EscrowTransferStatus.TRANSFERRED_TO_RESELLER_SELLER
        )

        // Reseller Profit Payout (95% net profit after 5% app commission, zero registration fee)
        val resellerUser = _users.value.find { it.id == order.resellerId }
        val resellerPayout = PayoutTransaction(
            id = "PAY-${(1000..9999).random()}",
            recipientId = order.resellerId,
            recipientName = order.resellerName,
            role = UserRole.RESELLER,
            amount = order.totalResellerProfit,
            channel = resellerUser?.bankTitle ?: "Easypaisa Instant Transfer",
            accountNumber = resellerUser?.accountNumber ?: "0300-DirectAccount",
            status = "COMPLETED",
            orderId = order.orderId,
            note = "Payment released from Admin Account after 7-15 days (Net Profit for Order #${order.orderId})"
        )

        // Seller Wholesale Payout
        val sellerUser = _users.value.find { it.id == order.sellerId }
        val sellerPayout = PayoutTransaction(
            id = "PAY-${(1000..9999).random()}",
            recipientId = order.sellerId,
            recipientName = order.sellerName,
            role = UserRole.SELLER,
            amount = order.subtotalWholesale,
            channel = sellerUser?.bankTitle ?: "Bank Transfer",
            accountNumber = sellerUser?.accountNumber ?: "Seller-Account",
            status = "COMPLETED",
            orderId = order.orderId,
            note = "Wholesale payment released from Admin Account after 7-15 days for Order #${order.orderId}"
        )

        _payouts.value = listOf(resellerPayout, sellerPayout) + _payouts.value

        // Credit wallet balances
        _users.value = _users.value.map { u ->
            if (u.id == order.resellerId) {
                u.copy(
                    walletAvailableBalance = u.walletAvailableBalance + order.totalResellerProfit,
                    walletPendingEarnings = maxOf(0.0, u.walletPendingEarnings - order.totalResellerProfit),
                    totalEarnings = u.totalEarnings + order.totalResellerProfit
                )
            } else if (u.id == order.sellerId) {
                u.copy(
                    walletAvailableBalance = u.walletAvailableBalance + order.subtotalWholesale,
                    walletPendingEarnings = maxOf(0.0, u.walletPendingEarnings - order.subtotalWholesale),
                    totalEarnings = u.totalEarnings + order.subtotalWholesale
                )
            } else {
                u
            }
        }

        _users.value.find { it.id == _currentUser.value.id }?.let { _currentUser.value = it }

        _orders.value = _orders.value.map {
            if (it.orderId == orderId) updatedOrder else it
        }
    }

    suspend fun placeResellerOrder(
        reseller: UserProfile,
        product: Product,
        quantity: Int,
        resellerProfitPerItem: Double,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        customerCity: String,
        customerArea: String,
        paymentMethod: PaymentMethod,
        orderNotes: String
    ): Pair<Order, PaymentResult> {
        val customerPricePerItem = product.baseWholesalePrice + resellerProfitPerItem
        val subtotalWholesale = product.baseWholesalePrice * quantity
        val grossProfit = resellerProfitPerItem * quantity
        val appCommission = grossProfit * 0.05 // 5% App Commission strictly for reseller only
        val netProfit = grossProfit - appCommission
        val deliveryFee = 150.0
        val finalPayable = (customerPricePerItem * quantity) + deliveryFee

        val orderNum = (10000..99999).random()
        val trackingNum = deliveryProvider.generateTrackingNumber("ORD-$orderNum", customerCity)

        val paymentResult = paymentProvider.initiatePayment(
            orderId = "ORD-$orderNum",
            amount = finalPayable,
            paymentMethod = paymentMethod,
            customerPhone = customerPhone,
            customerEmail = "$customerPhone@resellohub.pk"
        )

        val order = Order(
            orderId = "ORD-$orderNum",
            trackingId = trackingNum,
            resellerId = reseller.id,
            resellerName = reseller.name,
            sellerId = product.sellerId,
            sellerName = product.sellerName,
            customerName = customerName,
            customerPhone = customerPhone,
            customerAddress = customerAddress,
            customerCity = customerCity,
            customerArea = customerArea,
            courierPartner = "Trax / TCS Express",
            paymentMethod = paymentMethod,
            isPaymentPaid = paymentMethod != PaymentMethod.COD,
            items = listOf(
                OrderItem(
                    product = product,
                    quantity = quantity,
                    wholesalePrice = product.baseWholesalePrice,
                    resellerMarginPerItem = resellerProfitPerItem,
                    customerPricePerItem = customerPricePerItem
                )
            ),
            subtotalWholesale = subtotalWholesale,
            grossResellerProfit = grossProfit,
            appCommission = appCommission,
            totalResellerProfit = netProfit,
            deliveryFee = deliveryFee,
            finalCustomerPayable = finalPayable,
            status = OrderStatus.PENDING,
            isProfitTransferred = false,
            orderNotes = orderNotes
        )

        // Decrement product stock
        _products.value = _products.value.map {
            if (it.id == product.id) it.copy(stockQuantity = maxOf(0, it.stockQuantity - quantity)) else it
        }

        _orders.value = listOf(order) + _orders.value
        return Pair(order, paymentResult)
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val order = _orders.value.find { it.orderId == orderId } ?: return

        var updatedOrder = order.copy(status = newStatus)

        // When status becomes DELIVERED: Payment is collected into Admin central escrow account.
        // It enters 7-Day Easy Return window and 7-to-15 Days payout schedule for Reseller & Seller.
        if (newStatus == OrderStatus.DELIVERED && !order.isProfitTransferred) {
            updatedOrder = updatedOrder.copy(
                isPaymentPaid = true,
                adminAccountReceived = true,
                escrowStatus = EscrowTransferStatus.TRANSFER_SCHEDULED_7_TO_15_DAYS,
                transferScheduledDaysNotice = "Payment held in Admin account. Transferring to Reseller & Seller in 7-15 days after 7-day return period."
            )

            // Update pending wallet balances for Reseller and Seller so they see pending clearance
            _users.value = _users.value.map { u ->
                if (u.id == order.resellerId) {
                    u.copy(
                        walletPendingEarnings = u.walletPendingEarnings + order.totalResellerProfit,
                        totalEarnings = u.totalEarnings + order.totalResellerProfit
                    )
                } else if (u.id == order.sellerId) {
                    u.copy(
                        walletPendingEarnings = u.walletPendingEarnings + order.subtotalWholesale,
                        totalEarnings = u.totalEarnings + order.subtotalWholesale
                    )
                } else {
                    u
                }
            }
            _users.value.find { it.id == _currentUser.value.id }?.let { _currentUser.value = it }
        }

        _orders.value = _orders.value.map {
            if (it.orderId == orderId) updatedOrder else it
        }
    }

    // Direct manual payout withdrawal request or simulation
    fun triggerInstantDirectPayout(userId: String, amount: Double, channel: String, account: String): PayoutTransaction {
        val user = _users.value.find { it.id == userId }
        val payout = PayoutTransaction(
            id = "INST-${(1000..9999).random()}",
            recipientId = userId,
            recipientName = user?.name ?: "User",
            role = user?.role ?: UserRole.RESELLER,
            amount = amount,
            channel = channel,
            accountNumber = account,
            status = "COMPLETED",
            note = "Direct transfer dispatched to $channel ($account)"
        )
        _payouts.value = listOf(payout) + _payouts.value

        _users.value = _users.value.map {
            if (it.id == userId) it.copy(
                walletAvailableBalance = maxOf(0.0, it.walletAvailableBalance - amount),
                withdrawnAmount = it.withdrawnAmount + amount
            ) else it
        }
        _users.value.find { it.id == _currentUser.value.id }?.let { _currentUser.value = it }

        return payout
    }
}
