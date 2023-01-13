package com.vyw.tflite

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.AdapterView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import com.vyw.tflite.databinding.ActivityCameraStarterBinding
import kotlin.properties.Delegates

class CameraStarter : Activity(), SurfaceHolder.Callback, LifecycleObserver {

    private val REQUEST_CAMERA = 100

    private lateinit var binding : ActivityCameraStarterBinding
    private var blazefacecnn = BlazeFaceNcnn()
    private val facing = 0
    private var currentModel = 0
    private var currentCPUGPU = 0
    private var isCameraOpen : Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraStarterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraview!!.holder.setFormat(PixelFormat.RGBA_8888)
        binding.cameraview!!.holder.addCallback(this)

        binding.spinnerModel!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>? ,
                arg1: View ,
                position: Int ,
                id: Long
            ) {
                if (position != currentModel) {
                    currentModel = position
                    reload()
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
        reload()
    }

    private fun reload() {
        val ret_init: Boolean = blazefacecnn.loadModel(assets , currentModel , currentCPUGPU)
        if (!ret_init) {
            Log.e("MainActivity" , "blazefacecnn loaodModel failed")
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {}
    override fun surfaceChanged(holder: SurfaceHolder , format: Int , width: Int , height: Int) {
        blazefacecnn.setOutputWindow(holder.surface)
    }
    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    //Overrides Activity() function
    override fun onResume() {
        super.onResume()

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        }
        Log.d("Onresume", "Sample")
        blazefacecnn.openCamera(facing)
        val alert = Thread {
            while (isCameraOpen) {
                Log.d("Thread", "Something")
            }
        }
        startThread(alert)
    }
    override fun onPause() {
        super.onPause()

        blazefacecnn.closeCamera()
    }

    fun startThread(alert: Thread) {
        alert.start()
    }
}