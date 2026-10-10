package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AppRepository = AppRepository()
) : ViewModel() {

    val currentUser: StateFlow<UserProfile> = repository.currentUser
    val allUsers: StateFlow<List<UserProfile>> = repository.users
    val banners: List<BannerItem> = repository.banners
    val products: StateFlow<List<Product>> = repository.products
    val orders: StateFlow<List<Order>> = repository.orders
    val payouts: StateFlow<List<PayoutTransaction>> = repository.payouts
    val notifications: StateFlow<List<AppNotification>> = repository.notifications

    // Tracking query search state
    private val _searchTrackingId = MutableStateFlow("")
    val searchTrackingId: StateFlow<String> = _searchTrackingId.asStateFlow()

    // Reseller profit margin calculator modal state
    private val _selectedProductForResell = MutableStateFlow<Product?>(null)
    val selectedProductForResell: StateFlow<Product?> = _selectedProductForResell.asStateFlow()

    private val _resellerProfitMarginInput = MutableStateFlow("700")
    val resellerProfitMarginInput: StateFlow<String> = _resellerProfitMarginInput.asStateFlow()

    // Notification toast / banner message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun switchUser(userId: String) {
        repository.switchUser(userId)
        _userMessage.value = "Switched active user profile"
    }

    fun registerNewAccount(
        name: String,
        email: String,
        phone: String,
        role: UserRole,
        businessName: String,
        city: String,
        bankTitle: String,
        accountNumber: String
    ) {
        val created = repository.registerUser(
            name = name,
            email = email,
            phone = phone,
            role = role,
            businessName = businessName,
            city = city,
            bankTitle = bankTitle,
            accountNumber = accountNumber
        )
        repository.switchUser(created.id)
        _userMessage.value = "Account created! Pending Admin approval before full access."
    }

    fun approveOrRejectUser(userId: String, isApproved: Boolean) {
        repository.updateAccountApproval(userId, isApproved)
        _userMessage.value = if (isApproved) "Account Approved Successfully!" else "Account Rejected."
    }

    fun approveOrRejectListing(productId: String, isApproved: Boolean) {
        repository.updateListingApproval(productId, isApproved)
        _userMessage.value = if (isApproved) "Product Listing Approved & Live in Wholesale Supply!" else "Product Listing Rejected."
    }

    fun requestOrderReturn(orderId: String, reason: String) {
        repository.requestOrderReturn(orderId, reason)
        _userMessage.value = "7-Day Return requested! Customer refund initiated, payment halted from Admin Account."
    }

    fun releaseEscrowPayout(orderId: String) {
        repository.releaseEscrowPayout(orderId)
        _userMessage.value = "Payment successfully transferred from Admin Account to Reseller & Seller (7-15 days settlement completed)!"
    }

    fun openResellerShareModal(product: Product) {
        _selectedProductForResell.value = product
        val defaultMargin = ((product.suggestedRetailPrice - product.baseWholesalePrice) * 0.75).toInt()
        _resellerProfitMarginInput.value = defaultMargin.toString()
    }

    fun closeResellerModal() {
        _selectedProductForResell.value = null
    }

    fun updateResellerProfitMargin(margin: String) {
        _resellerProfitMarginInput.value = margin
    }

    fun placeCustomerOrder(
        product: Product,
        quantity: Int,
        margin: Double,
        custName: String,
        custPhone: String,
        custAddress: String,
        custCity: String,
        custArea: String = "Main City",
        paymentMethod: PaymentMethod = PaymentMethod.COD,
        orderNotes: String = ""
    ) {
        viewModelScope.launch {
            val user = currentUser.value
            val (order, paymentResult) = repository.placeResellerOrder(
                reseller = user,
                product = product,
                quantity = quantity,
                resellerProfitPerItem = margin,
                customerName = custName,
                customerPhone = custPhone,
                customerAddress = custAddress,
                customerCity = custCity,
                customerArea = custArea,
                paymentMethod = paymentMethod,
                orderNotes = orderNotes
            )
            _selectedProductForResell.value = null
            _searchTrackingId.value = order.trackingId
            _userMessage.value = "Order placed! Tracking ID: ${order.trackingId}. Net Profit: PKR ${(order.totalResellerProfit).toInt()} (5% App Commission, Zero Registration Fee)"
        }
    }

    fun addSellerProduct(
        title: String,
        description: String,
        category: String,
        manufacturingCost: Double,
        sellerProfitPercentage: Double,
        suggestedRetailPrice: Double,
        stock: Int,
        deliveryDays: String,
        imageUri: String? = null,
        drawableRes: Int? = null
    ) {
        val clampedProfit = sellerProfitPercentage.coerceIn(5.0, 20.0)
        repository.addProduct(
            seller = currentUser.value,
            title = title,
            description = description,
            category = category,
            manufacturingCost = manufacturingCost,
            sellerProfitPercentage = clampedProfit,
            suggestedRetailPrice = suggestedRetailPrice,
            stock = stock,
            deliveryDays = deliveryDays,
            imageUri = imageUri,
            drawableRes = drawableRes
        )
        val wholesalePrice = (manufacturingCost * (1.0 + clampedProfit / 100.0)).toInt()
        _userMessage.value = "Product published with ${clampedProfit.toInt()}% profit! Wholesale Price: PKR $wholesalePrice"
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        repository.updateOrderStatus(orderId, status)
        if (status == OrderStatus.DELIVERED) {
            _userMessage.value = "Order delivered! 100% Full profit directly transferred to Reseller!"
        } else {
            _userMessage.value = "Order status updated to ${status.label}"
        }
    }

    fun setTrackingSearch(query: String) {
        _searchTrackingId.value = query
    }

    fun triggerDirectInstantWithdrawal(amount: Double) {
        val user = currentUser.value
        if (amount > user.walletAvailableBalance) {
            _userMessage.value = "Insufficient wallet available balance"
            return
        }
        repository.triggerInstantDirectPayout(
            userId = user.id,
            amount = amount,
            channel = user.bankTitle,
            account = user.accountNumber
        )
        _userMessage.value = "PKR $amount transferred directly to ${user.bankTitle} (${user.accountNumber})!"
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
