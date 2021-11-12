package com.calldorado.preonboarding

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.*
import androidx.work.impl.WorkManagerImpl
import androidx.work.testing.*
import com.calldorado.preonboarding.notification.NotificationWorker
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        val TAG = "setup()"

        Timber.plant(Timber.DebugTree())

        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        executor = Executors.newSingleThreadExecutor()
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        try {
            WorkManagerTestInitHelper.initializeTestWorkManager(instrumentationContext, config)
        } catch (e : java.lang.Exception){
            Timber.d("Work manager already initialized")
        }
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.calldorado.preonboarding.test", appContext.packageName)
    }

    @Test
    fun testUpdate(){
        FakeAppUpdateManager(InstrumentationRegistry.getInstrumentation().targetContext).isConfirmationDialogVisible
    }

    @Test
    @Throws(Exception::class)
    fun testPeriodicWork() {
        val TAG = "testPeriodicWork"

        val workManager = WorkManager.getInstance(instrumentationContext)
        val periodicNotificationWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                .addTag(NotificationWorker.NOTIFICATION_WORK_TAG)
                .setInitialDelay(Utils.getMinutesUntilHour(15), TimeUnit.MINUTES)
                .build()

        Timber.d("ID is ${periodicNotificationWorkRequest.id}")
        val testDriver: TestDriver = WorkManagerTestInitHelper.getTestDriver(instrumentationContext)!!

        workManager
            .enqueueUniquePeriodicWork(NotificationWorker.NOTIFICATION_WORK_TAG, ExistingPeriodicWorkPolicy.REPLACE, periodicNotificationWorkRequest)
            .result
            .get()

        val workInfo = workManager.getWorkInfoById(periodicNotificationWorkRequest.id).get()

        // Tells the testing framework the period delay is met, this will execute the code in worker
        Timber.d("Test driver is init'ed: ${testDriver!=null}")
        testDriver.setPeriodDelayMet(periodicNotificationWorkRequest.id)
        testDriver.setAllConstraintsMet(periodicNotificationWorkRequest.id)

        Timber.d( "Worker state: ${workInfo.state}")
    }

    private lateinit var executor: Executor
    @Test
    @Throws(Exception::class)
    fun testWorker() {
        /*val worker = TestWorkerBuilder<NotificationWorker>(
            context = instrumentationContext,
            executor = executor
        ).build()

        val result = worker.doWork()*/
//        assertThat(result, `is`(Result.success()))
    }

    @Test
    @Throws(Exception::class)
    fun testCoroutineWorker() {
        val worker =
            TestListenableWorkerBuilder<NotificationWorker>(instrumentationContext).build()
        // Run the worker synchronously
        val result = worker.startWork().get()
    }
}