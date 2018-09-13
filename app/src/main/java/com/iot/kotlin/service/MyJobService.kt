package com.iot.kotlin.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.iot.kotlin.controller.GetTemperatureController

/**
 * Service to handle callbacks from the JobScheduler. Requests scheduled with the JobScheduler
 * ultimately land on this service's "onStartJob" method. It runs jobs for a specific amount of time
 * and finishes them.
 */
class MyJobService : JobService() {

    private val TAG = "MyJobService"
    private var jobCancelled = false

    override fun onStartJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job started")
        doBackgroundWork(params)

        return true
    }

    private fun doBackgroundWork(params: JobParameters) {

        //get HKtemperature and update to shared preference
        GetTemperatureController.getCurrentTemp(applicationContext, "bedroom", "ac")

        // At the end inform job manager the status of the job.
        jobFinished(params, true)
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.d(TAG, "Job cancelled before completion")
        jobCancelled = true
        return true
    }
}