package com.iot.kotlin.util


import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import android.support.v4.app.NotificationCompat
import com.iot.kotlin.R

/**
 * This is the UI helper class that facilitate the network alert dialog and progress dialog
 *
 */
object UiHelper {

    fun showNetworkAlertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.message_ask_turn_internet_on))
        builder.setPositiveButton(R.string.label_open_settings, DialogInterface.OnClickListener { dialog, which -> context.startActivity(Intent(Settings.ACTION_SETTINGS)) })
        builder.setNegativeButton(R.string.label_cancel, DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.create().show()
    }

    fun showNetworkAlertDialogforFixture(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(context.getString(R.string.message_ask_turn_internet_on))
        builder.setPositiveButton(R.string.label_open_settings, DialogInterface.OnClickListener { dialog, which -> context.startActivity(Intent(Settings.ACTION_SETTINGS)) })
        builder.setNegativeButton(R.string.label_cancel, DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.create().show()
    }



    fun showProgressDialog(context: Context, messageResId: Int, status: Boolean): ProgressDialog {
        val dialog = ProgressDialog(context)
        dialog.setMessage(context.getString(messageResId))
        //   if (!(context as Activity).isFinishing) {

        if (status) {
            dialog.show()
        } else {
            dialog.dismiss()
        }
        return dialog
    }



}
