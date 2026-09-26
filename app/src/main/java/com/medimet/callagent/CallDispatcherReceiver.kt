package com.medimet.callagent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.telephony.SmsManager

class CallDispatcherReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING && !incomingNumber.isNullOrEmpty()) {
                
                // تشخیص نوع شماره (موبایل یا ثابت)
                if (isMobileNumber(incomingNumber)) {
                    handleMobileCaller(incomingNumber)
                } else {
                    handleLandlineCaller(incomingNumber)
                }
            }
        }
    }

    private fun isMobileNumber(number: String): Boolean {
        val cleanNumber = number.replace("+98", "0")
        return cleanNumber.startsWith("09")
    }

    private fun handleMobileCaller(phoneNumber: String) {
        // ارسال مستقیم لینک نوبت‌دهی مدیمت به شماره موبایل تماس‌گیرنده
        sendSmsToUser(phoneNumber, "سلام، جهت دریافت نوبت از کلینیک از طریق لینک زیر اقدام کنید:\nhttps://www.medimet.ir/booking")
    }

    private fun handleLandlineCaller(phoneNumber: String) {
        // در خط ثابت، اینجا سیستم صوتی و دریافت شماره (DTMF) فعال می‌شود
        // در حال حاضر برای ثبت ساختار پایه، آماده‌سازی شده است
    }

    private fun sendSmsToUser(targetNumber: String, messageText: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(targetNumber, null, messageText, null, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
