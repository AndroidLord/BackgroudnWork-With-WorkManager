package com.example.backgroudnworkwithworkmanager

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil.compose.AsyncImage
import com.example.backgroudnworkwithworkmanager.ui.theme.BackgroudnWorkWithWorkManagerTheme

class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager

    private val viewModel by viewModels<MainViewModel>()

//    private val airPlaneModeReceiver = AirPlaneModeReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val emails = arrayOf("shubhambisht965@gmail.com", "deepak@gmail.com")
        val subject = "This is a test subject"
        val message = "This is a test message"

        workManager = WorkManager.getInstance(applicationContext)

//        registerReceiver(
//            airPlaneModeReceiver,
//            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
//        )


        setContent {
            BackgroudnWorkWithWorkManagerTheme {

                MainScreen(emails, subject, message, context = this, viewModel = viewModel,workManager)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val data: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        } ?: return

        viewModel.updateUri(data)

        val request = OneTimeWorkRequestBuilder<PhotoCompressionWorker>()
            .setInputData(
                workDataOf(
                    PhotoCompressionWorker.KEY_CONTENT_URI to data.toString(),
                    PhotoCompressionWorker.KEY_COMPRESSION_THRESHOLD to 1024 * 20L
                )
            )
            .setConstraints(Constraints(
                    requiresStorageNotLow = true
                )
            )
            .build()
            viewModel.updateWorkId(request.id)
            workManager.enqueue(request)



    }

//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(airPlaneModeReceiver)
//    }
    
}


@Composable
private fun MainScreen(
    emails: Array<String>,
    subject: String,
    message: String,
    context: Context,
    viewModel: MainViewModel,
    workManager: WorkManager
    ) {

    val workerResult = viewModel.workId?.let { id->
        workManager.getWorkInfoByIdLiveData(id).observeAsState().value
    }

    LaunchedEffect(key1 = workerResult?.outputData) {
        if(workerResult?.outputData != null){
            val filePath = workerResult.outputData.getString(PhotoCompressionWorker.KEY_RESULT_PATH)
            filePath?.let {
                val bitmap = BitmapFactory.decodeFile(it)
                viewModel.updateCompressedBitmap(bitmap)
            }

        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(viewModel.uri != null){

                Text(text = "Uncompressed Image")
                AsyncImage(
                    model = viewModel.uri, contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

            }

            if(viewModel.compressedBitmap != null){

                Text(text = "Compressed Image")
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    bitmap = viewModel.compressedBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            ButtonMade(text_name = "You Tube") {

                Intent(Intent.ACTION_MAIN).also {
                    it.`package` = "com.google.android.youtube"
                    try {
                        startActivity(context, it, Bundle())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            ButtonMade() {

                val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                    type = "plain/text"
                    putExtra(Intent.EXTRA_EMAIL, emails)
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, message)
                }

                if (emailIntent.resolveActivity(context.packageManager) != null) {
                    startActivity(context, emailIntent, Bundle())
                }
            }
        }

    }
}


@Composable
private fun ButtonMade(
    text_name: String = "Click Me",
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        }) {

        Text(text = text_name)
    }
}
