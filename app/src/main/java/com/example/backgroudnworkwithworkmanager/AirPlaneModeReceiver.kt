package com.example.backgroudnworkwithworkmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import kotlin.math.log


private const val TAG = "AirPlaneModeReceiver"
class AirPlaneModeReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)){
            val isTurnedOn = Settings.Global.getInt(
                context?.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) != 0
            println("Is Airplane mode turned on: $isTurnedOn")
            Toast.makeText(context, "Airplane mode turned on: $isTurnedOn", Toast.LENGTH_SHORT).show()
        }

        if (intent?.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Toast.makeText(context, "Boot completed!! From Background Manager", Toast.LENGTH_SHORT).show()
        }

        if (intent?.action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

            Log.d(TAG, "Outgoing call is made")
            Toast.makeText(context, "Screen is turned on!! From Background Manager", Toast.LENGTH_SHORT).show()
        }


    }

}