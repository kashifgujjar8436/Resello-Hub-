package com.example.navigation

object Routes {
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val CATEGORY_PRODUCTS = "category_products/{categoryName}"
    const val PRODUCT_DETAILS = "product_details/{productId}"
    const val RESELL_PRODUCT = "resell_product/{productId}"
    const val CHECKOUT = "checkout/{productId}?quantity={quantity}&margin={margin}"
    const val ORDERS = "orders"
    const val ORDER_DETAILS = "order_details/{orderId}"
    const val TRACKING = "tracking?trackingId={trackingId}"
    const val WALLET = "wallet"
    const val WITHDRAW_MONEY = "withdraw_money"
    const val PROFILE = "profile"
    const val NOTIFICATIONS = "notifications"
    const val REFERRAL = "referral"
    const val HELP_SUPPORT = "help_support"
    const val ADMIN_PANEL = "admin_panel"
    const val SELLER_CATALOG = "seller_catalog"

    fun categoryProducts(categoryName: String) = "category_products/$categoryName"
    fun productDetails(productId: String) = "product_details/$productId"
    fun resellProduct(productId: String) = "resell_product/$productId"
    fun checkout(productId: String, quantity: Int, margin: Double) = "checkout/$productId?quantity=$quantity&margin=$margin"
    fun orderDetails(orderId: String) = "order_details/$orderId"
    fun tracking(trackingId: String = "") = "tracking?trackingId=$trackingId"
}
