package com.team1.simplebank.common.constants

sealed class ScreenRoute (
    var route: String,
){
    //Auth
    data object Login: ScreenRoute("login")
    data object ForgotPwInputCardData: ScreenRoute("forgot_pw_input_card_data")
    data object ForgotPwInputBirthDate: ScreenRoute("forgot_pw_input_birth_date")
    data object ForgotPwInputEmail: ScreenRoute("forgot_pw_input_email")
    data object ForgotPwInputOtpCode: ScreenRoute("forgot_pw_input_otp_code/{email}"){
        fun createRoute(email: String) = "forgot_pw_input_otp_code/${email}"
    }
    data object ForgotPwInputNewPassword: ScreenRoute("forgot_pw_input_new_password")
    data object ForgotPwConfirmationSuccess: ScreenRoute("forgot_pw_confirmation_success")

    data object QrisScan: ScreenRoute("qris_scan")
    data object ScanQrisConfrimTransaction: ScreenRoute("qris_scan/{qrValue}"){
        fun createRoute(qrValue: String) = "qris_scan/${qrValue}"
    }
    data object ShowQrisConfirmationScreen : ScreenRoute("qris_show_confirmation")
    data object ShowQrisCodeScreen : ScreenRoute("qris_show_confirmation/{isConfirmed}"){
        fun createRoute(isConfirmed: Boolean) = "qris_show_confirmation/${isConfirmed}"
    }
    data object QrisTransactionResult: ScreenRoute("qris_transacrion_result")

}