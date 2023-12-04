package com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.MusicModel
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User
import com.jogging.walking.weightloss.pedometer.steptracker.data.service.MusicService
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityTrackingBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.ui.details.DetailsAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.activity.MusicActivity
import com.jogging.walking.weightloss.pedometer.steptracker.ui.music.activity.MusicPlayerActivity
import com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.service.PolyLine
import com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.service.TrackingService
import com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.viewmodel.TrackingViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.ACTION_PAUSE_SERVICE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.ACTION_START_OR_RESUME_SERVICE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.ACTION_STOP_SERVICE
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.POLYLINE_COLOR
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Constants.POLYLINE_WIDTH
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.Strings
import com.jogging.walking.weightloss.pedometer.steptracker.utils.SystemUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.math.round


class TrackingAct @Inject constructor() : AbsActivity<ActivityTrackingBinding>(),
    OnMapReadyCallback, TextToSpeech.OnInitListener {

    private var googleMap: GoogleMap? = null
    private var mapFragment: SupportMapFragment? = null
    private var isTracking = false
    private var isStatus = false
    private var currentTimeInMillis = 0L
    private var pathPoints = mutableListOf<PolyLine>()
    private var zoomDefault = 16f
    private var textToSpeech: TextToSpeech? = null
    private var isVoice = true
    private val viewModel by viewModels<TrackingViewModel>()
    private var wasRegistered = false

    override fun initView() {
        checkPermissionNotification()
        setUpMap()
        subscribeToObservers()
        textToSpeech = TextToSpeech(this, this)
    }

    private fun checkServiceIsRunning() {
        if (MusicService.isRunning){
            startService(Intent(this, MusicService::class.java).apply {
                putExtra(Constants.EXTRA_DETECT_SERVICE_EXIST, MusicService.isRunning)
            })
        }else{
            startActivity(Intent(this, MusicActivity::class.java))
        }
    }

    private val mReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            wasRegistered = true
            val musicPlaying = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.extras?.getParcelable(Constants.EXTRA_DATA_SERVICE_TO_ACTIVITY, MusicModel.Music::class.java)
            else
                intent.extras?.getParcelable(Constants.EXTRA_DATA_SERVICE_TO_ACTIVITY)

            val typeOfMusic = intent.extras?.getString(Constants.EXTRA_TYPE_MUSIC_TO_ACTIVITY)
            val actionCurrent = intent.extras?.getBoolean(Constants.EXTRA_ACTION_MUSIC_TO_ACTIVITY)

            startActivity(Intent(this@TrackingAct, MusicPlayerActivity::class.java).apply {
                putExtra(Constants.EXTRA_MUSIC_TO_PLAY, musicPlaying)
                putExtra(Constants.EXTRA_TYPE_MUSIC, typeOfMusic)
                putExtra(Constants.EXTRA_FROM_TRACKING, Constants.FROM_TRACKING)
                putExtra(Constants.ACTION_MUSIC_CURRENT, actionCurrent)
            })
        }
    }

    private fun speak(text: String) {
        if (text.isNotEmpty()) {
            val countryCode = SystemUtil.getPreLanguage(this) ?: ""
            val locale = Locale(countryCode)
            textToSpeech?.language = locale
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
//            speak("")
        }

    }

    override fun initAction() {
        binding.imgBack.setOnClickListener { finish() }
        binding.btnStart.setOnClickListener {
            showCurrentLocation(zoomDefault)
            toggleRun()
        }
        binding.ivStatus.setOnClickListener {
            isStatus = !isStatus
            if (isStatus) {
                binding.ivStatus.setImageResource(R.drawable.ic_resume)
                toggleRun()
            } else {
                toggleRun()
                binding.ivStatus.setImageResource(R.drawable.ic_pause)
            }
        }
        binding.btnFinish.setOnClickListener {
            binding.btnStart.beVisible()
            binding.layoutStatus.beGone()
            binding.ivStatus.setImageResource(R.drawable.ic_pause)
            isStatus = false
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()
            lifecycleScope.launch {
                delay(1000)
                binding.txtDistance.text = "0.0"
                binding.txtSpeed.text = "0.0"
                binding.txtCalo.text = "0.0"
            }
        }
        binding.ivCurrentLocation.setOnClickListener {
            zoomDefault++
            showCurrentLocation(zoomDefault)
        }
        binding.txtSelectMusic.setOnClickListener {
//            val intent = Intent(Intent.ACTION_MAIN)
//            intent.addCategory(Intent.CATEGORY_APP_MUSIC)
//            startActivity(intent)
            checkServiceIsRunning()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_tracking
    }

    override fun bindViewModel() {
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(this) {
            updateTracking(it)
        }

        TrackingService.pathPoints.observe(this) { polyLine ->
            polyLine.takeIf { it.isNotEmpty() }?.let {
                pathPoints = it
            } ?: kotlin.run {
//                Toast.makeText(this, "Tuấn Anh 12222", Toast.LENGTH_SHORT).show()
            }
            addPolyline()
        }

        TrackingService.timeRunInMillis.observe(this) {
            currentTimeInMillis = it
            val formattedTime = DeviceUtil.getFormattedStopWatchTime(currentTimeInMillis, true)
            Strings.log("TuanAnh forrmat time",formattedTime)
            binDataWhenRun()
            binding.txtTime.text = formattedTime
        }
    }

    private fun binDataWhenRun() {
        var distanceInMeters = 0
        for (poly in pathPoints) {
            distanceInMeters += DeviceUtil.calculatePolyLineLength(poly).toInt()
        }
        val distanceInKM = distanceInMeters.div(1000f)
        var avgSpeed = round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 60 / 60) * 10) / 10f
        if (avgSpeed.isNaN()) {
            avgSpeed = 0.0f
        }
        val caloriesBurned = (((distanceInMeters / 1000f) * sharePrefs.weight?.toFloat()!!) * 0.8)
        binding.txtDistance.text = distanceInKM.toString()
        binding.txtSpeed.text = DeviceUtil.formattedValue(avgSpeed)
        binding.txtCalo.text = caloriesBurned.toString()
        viewModel.getRuns.observe(this) { runs ->
            if (runs.isNotEmpty() && sharePrefs.isVoice) {
                val maxDistance = runs.maxByOrNull { it.distance ?: 0f }?.distance?.div(1000f) ?: 0f
                if (distanceInKM > maxDistance) {
                    if (isVoice) {
                        speak(getString(R.string.enable_noti))
                        isVoice = false
                    }
                }
            }
        }
    }

    private fun addAllPolyLines() {
        for (polyline in pathPoints) {
            val polyLineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            googleMap?.addPolyline(polyLineOptions)
        }
    }

    private fun addPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polyLineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            googleMap?.addPolyline(polyLineOptions)
        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (poluline in pathPoints) {
            for (pos in poluline) {
                bounds.include(pos)
            }
        }
        googleMap?.setPadding(0, 0, 0, 0)
        val padding = resources.getDimensionPixelSize(R.dimen.my_dimen) // Adjust padding as needed
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds.build(), padding)
        googleMap?.moveCamera(cameraUpdate)
    }

    private fun endRunAndSaveToDb() {
        googleMap?.snapshot { bmp ->
            var distanceInMeters = 0
            for (polyLine in pathPoints) {
                distanceInMeters += DeviceUtil.calculatePolyLineLength(polyLine).toInt()
            }
            val avgSpeed =
                round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurned =
                (((distanceInMeters / 1000f) * sharePrefs.weight?.toFloat()!!) * 0.8).toFloat()

            val calendar = Calendar.getInstance().apply {
                timeInMillis = dateTimeStamp
            }
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
            val duration = dateFormat.format(calendar.time)

            val run = Running(
                null,
                duration,
                bmp,
                distanceInMeters.toFloat(),
                avgSpeed,
                caloriesBurned,
                0f,
                mutableListOf(),
                null,
                currentTimeInMillis,
                sharePrefs.timeStart,
                DeviceUtil.getCurrentTime()
            )
            viewModel.insertRun(run)
            viewModel.getRuns.observe(this) {
                viewModel.getUsers.observe(this) { user ->
                    val userUpdate = User(
                        user.id,
                        user.username,
                        user.avatar,
                        user.age,
                        user.height,
                        user.weight,
                        it,
                        user.gender,
                        user.units
                    )
                    viewModel.updateUser(userUpdate)
                }
            }
            showCurrentLocation(zoomDefault)
            stopRun()
        }
    }


    private fun toggleRun() {
        sharePrefs.timeStart = DeviceUtil.getCurrentTime()
        if (isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        Log.i("TuanAnh ", isTracking.toString())
        if (!isTracking && currentTimeInMillis > 0L) {
            binding.txtStart.text = getString(R.string.start)
        } else if (isTracking) {
            binding.btnStart.beGone()
            binding.layoutStatus.beVisible()
        }
    }

    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
        startActivity(DetailsAct.getIntent(this@TrackingAct, 0L,2))
        finish()
    }

    private fun sendCommandToService(action: String) =
        Intent(this, TrackingService::class.java).also {
            it.action = action
            this.startService(it)
        }


    private fun setUpMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        addAllPolyLines()
//        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        // disable button current location default
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        if (SystemUtil.haveNetworkConnection(this)) showCurrentLocation(zoomDefault) else Toast.makeText(
            this,
            "Bạn cần bật Internet",
            Toast.LENGTH_SHORT
        ).show()
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentLocation(zoom: Float) {
        googleMap?.setPadding(0, 0, 0, 700)
        googleMap?.isMyLocationEnabled = true
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, zoom))
                }
            }
    }

    private fun checkPermissionNotification() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED -> {
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                Snackbar.make(
                    findViewById(R.id.map),
                    "The user denied the notifications ):",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Settings") {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val uri: Uri = Uri.fromParts("com.jogging.walking.weightloss.pedometer.steptracker", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .show()
            }

            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. You can proceed with using notifications here.
        } else {
            // Permission is denied by the user.
            // Handle the denial as needed.
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result: Int? = textToSpeech?.setLanguage(Locale("hi")) // Hindi Locale
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                println("Language not supported")
            } else {
            }
        }
    }

    override fun onDestroy() {
        if (textToSpeech?.isSpeaking == true) {
            textToSpeech?.stop()
        }
        unregisterReceiver(mReceiver)
        textToSpeech?.shutdown()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mReceiver, IntentFilter("${this.packageName}.GET_MUSIC"))
        mapFragment?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapFragment?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapFragment?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapFragment?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapFragment?.onLowMemory()
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, TrackingAct::class.java)
        }
    }

}