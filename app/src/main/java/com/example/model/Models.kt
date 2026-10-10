package com.example.model

enum class UserRole(val displayName: String, val badge: String) {
    CUSTOMER("Customer", "Resello Buyer"),
    RESELLER("Reseller", "Verified Reseller"),
    SELLER("Wholesale Supplier", "Official Store"),
    ADMIN("Super Admin", "Operations HQ")
}

enum class AccountStatus {
    PENDING_APPROVAL,
    APPROVED,
    REJECTED
}

enum class ListingStatus(val label: String) {
    PENDING_APPROVAL("Pending Admin Review"),
    APPROVED("Approved & Live"),
    REJECTED("Rejected")
}

enum class EscrowTransferStatus(val label: String) {
    HELD_IN_ADMIN_ACCOUNT("Held in Admin Escrow Account"),
    TRANSFER_SCHEDULED_7_TO_15_DAYS("Transfer Scheduled (7-15 Days)"),
    TRANSFERRED_TO_RESELLER_SELLER("Transferred to Reseller & Seller"),
    REFUNDED_RETURN_7_DAYS("Refunded to Customer (7-Day Return)")
}

enum class PaymentMethod(val title: String, val subtitle: String, val isOnline: Boolean) {
    COD("COD Parcel 📦 (Cash on Delivery)", "Pay cash upon parcel delivery by courier rider", false),
    JAZZCASH("JazzCash Mobile Account", "Instant wallet or voucher payment", true),
    EASYPAISA("Easypaisa Direct", "Instant digital wallet transfer", true),
    UPAYSA("Upaisa Mobile Wallet", "Quick microfinance wallet payment", true),
    BANK_TRANSFER("1Link Direct Bank / IBAN", "1Link instant online transfer", true)
}

enum class OrderStatus(val label: String, val stepIndex: Int) {
    PENDING("Pending", 0),
    CONFIRMED("Confirmed by Seller", 1),
    PROCESSING("Processing at Warehouse", 2),
    PACKED("Packed with Bubble Wrap", 3),
    SHIPPED("Dispatched (TCS/Trax)", 4),
    OUT_FOR_DELIVERY("Rider Out for Delivery", 5),
    DELIVERED("Delivered & Paid", 6),
    CANCELLED("Cancelled", -1),
    RETURNED("Returned", -2)
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val status: AccountStatus,
    val businessName: String,
    val city: String,
    val province: String = "Punjab",
    val area: String = "Main Market",
    val streetAddress: String = "House 12, Street 4",
    val postalCode: String = "54000",
    val bankTitle: String = "Easypaisa Direct",
    val accountNumber: String = "0321-4567890",
    val referralCode: String = "RESELL-948",
    val referralEarnings: Double = 3500.0,
    val walletAvailableBalance: Double = 14250.0,
    val walletPendingEarnings: Double = 2850.0,
    val totalEarnings: Double = 48500.0,
    val withdrawnAmount: Double = 31400.0,
    val positiveSellerRating: String = "98%",
    val responseRate: String = "100%",
    val shipOnTime: String = "99%"
)

data class Product(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val manufacturingCost: Double, // Cost incurred by seller/supplier to produce item
    val sellerProfitPercentage: Double, // Strict seller profit range: 5% to 20%
    val baseWholesalePrice: Double, // Final wholesale price = manufacturingCost * (1 + sellerProfitPercentage / 100)
    val suggestedRetailPrice: Double,
    val stockQuantity: Int,
    val sellerId: String,
    val sellerName: String,
    val isVerifiedStore: Boolean = true,
    val rating: Double = 4.9,
    val reviewsCount: Int = 184,
    val soldCount: String = "1.2k+ Sold",
    val sku: String = "RSH-PROD-101",
    val deliveryDays: String = "Standard 2-3 Days",
    val tags: List<String> = listOf("Resello Mall", "Zero Reseller Fee", "Free Delivery eligible", "100% Authentic"),
    val drawableRes: Int? = null,
    val imageUri: String? = null,
    val isCodAvailable: Boolean = true,
    val returnPolicyDays: Int = 7, // 7 Days Easy Return Policy
    val listingStatus: ListingStatus = ListingStatus.APPROVED
) {
    val sellerProfitAmount: Double get() = baseWholesalePrice - manufacturingCost
}

data class OrderItem(
    val product: Product,
    val quantity: Int,
    val wholesalePrice: Double,
    val resellerMarginPerItem: Double,
    val customerPricePerItem: Double
) {
    val totalCustomerPrice: Double get() = customerPricePerItem * quantity
    val totalWholesaleCost: Double get() = wholesalePrice * quantity
    val grossResellerProfit: Double get() = resellerMarginPerItem * quantity
    // App platform commission is 5% strictly for reseller only; 0 upfront registration fee
    val appCommissionAmount: Double get() = grossResellerProfit * 0.05
    val resellerNetProfit: Double get() = grossResellerProfit - appCommissionAmount
}

data class Order(
    val orderId: String,
    val trackingId: String,
    val resellerId: String,
    val resellerName: String,
    val sellerId: String,
    val sellerName: String,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val customerCity: String,
    val customerArea: String = "Gulberg / Model Town",
    val customerProvince: String = "Punjab",
    val courierPartner: String = "Trax / TCS Express",
    val paymentMethod: PaymentMethod = PaymentMethod.COD,
    val isPaymentPaid: Boolean = false,
    val items: List<OrderItem>,
    val subtotalWholesale: Double,
    val grossResellerProfit: Double = 0.0,
    val appCommission: Double = 0.0, // 5% App commission for reseller only
    val totalResellerProfit: Double, // Net earnings credited to reseller
    val deliveryFee: Double = 150.0,
    val finalCustomerPayable: Double,
    val status: OrderStatus,
    val createdAtMillis: Long = System.currentTimeMillis(),
    val isProfitTransferred: Boolean = false,
    val payoutReference: String? = null,
    val orderNotes: String = "Deliver between 10am to 5pm.",
    // Escrow payment & return tracking
    val escrowStatus: EscrowTransferStatus = EscrowTransferStatus.HELD_IN_ADMIN_ACCOUNT,
    val adminAccountReceived: Boolean = true, // Payment first lands in Admin central account
    val returnWindowDays: Int = 7, // 7 days easy return window
    val returnRequested: Boolean = false,
    val returnReason: String? = null,
    val transferScheduledDaysNotice: String = "7 to 15 Days after delivery"
)

data class PayoutTransaction(
    val id: String,
    val recipientId: String,
    val recipientName: String,
    val role: UserRole,
    val amount: Double,
    val channel: String, // e.g. "Easypaisa Instant Transfer", "JazzCash", "Upaisa", "Bank Account"
    val accountNumber: String,
    val status: String = "COMPLETED",
    val timestampMillis: Long = System.currentTimeMillis(),
    val orderId: String? = null,
    val note: String = "100% Direct profit credit (Zero fee deduction)"
)

data class BannerItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val promoBadge: String,
    val actionText: String
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestampMillis: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
