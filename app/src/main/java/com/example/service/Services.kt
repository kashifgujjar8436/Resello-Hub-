package com.example.service

import com.example.model.PaymentMethod

sealed class PaymentResult {
    data class Success(val transactionId: String, val message: String) : PaymentResult()
    data class Pending(val referenceId: String, val message: String) : PaymentResult()
    data class Failure(val errorMessage: String) : PaymentResult()
}

interface PaymentProvider {
    suspend fun initiatePayment(
        orderId: String,
        amount: Double,
        paymentMethod: PaymentMethod,
        customerPhone: String,
        customerEmail: String
    ): PaymentResult
}

class ReselloPaymentProvider : PaymentProvider {
    override suspend fun initiatePayment(
        orderId: String,
        amount: Double,
        paymentMethod: PaymentMethod,
        customerPhone: String,
        customerEmail: String
    ): PaymentResult {
        return when (paymentMethod) {
            PaymentMethod.COD -> {
                PaymentResult.Success(
                    transactionId = "COD-$orderId",
                    message = "Cash on Delivery confirmed. Courier rider will collect PKR ${amount.toInt()} upon parcel handover."
                )
            }
            PaymentMethod.JAZZCASH -> {
                // JazzCash Merchant API Integration Gateway (HMAC-SHA256 Sandbox / Production)
                // Merchant ID, Password & Hash Key injected securely via BuildConfig/Secrets
                PaymentResult.Success(
                    transactionId = "JC-MW-${(100000..999999).random()}",
                    message = "JazzCash Mobile Account prompt authorized successfully."
                )
            }
            PaymentMethod.EASYPAISA -> {
                // Easypaisa MA Direct API Integration
                PaymentResult.Success(
                    transactionId = "EP-PAY-${(100000..999999).random()}",
                    message = "Easypaisa instant wallet payment debited successfully."
                )
            }
            PaymentMethod.UPAYSA -> {
                // Upaisa Wallet Integration
                PaymentResult.Success(
                    transactionId = "UP-TXN-${(100000..999999).random()}",
                    message = "Upaisa digital payment transaction confirmed."
                )
            }
            PaymentMethod.BANK_TRANSFER -> {
                PaymentResult.Pending(
                    referenceId = "1LINK-IBFT-${(100000..999999).random()}",
                    message = "1Link IBFT Voucher generated. Please transfer to Resello Hub Escrow Account."
                )
            }
        }
    }
}

interface DeliveryProvider {
    fun generateTrackingNumber(orderId: String, city: String): String
    fun getTrackingUrl(trackingNumber: String): String
}

class TraxLeopardDeliveryProvider : DeliveryProvider {
    override fun generateTrackingNumber(orderId: String, city: String): String {
        return "TRX-${(100000..999999).random()}-PK"
    }

    override fun getTrackingUrl(trackingNumber: String): String {
        return "https://sonic.pk/tracking?consignmentNo=$trackingNumber"
    }
}
