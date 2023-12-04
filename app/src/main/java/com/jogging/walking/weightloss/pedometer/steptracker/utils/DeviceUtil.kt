package com.jogging.walking.weightloss.pedometer.steptracker.utils

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.text.format.DateFormat
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.Running
import com.jogging.walking.weightloss.pedometer.steptracker.data.sharePrefs.SharePrefs
import com.jogging.walking.weightloss.pedometer.steptracker.ui.tracking.service.PolyLine
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.ln
import kotlin.math.pow

object DeviceUtil {
    fun hasStoragePermission(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        else
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun checkTime(inputPath: String): String {
        val lastModifiedTime = getFileLastModifiedTime(inputPath)
        val formattedTime = DateFormat.format("yyyy/MM/dd hh:mm a", lastModifiedTime)
        return formattedTime.toString()
    }

    private fun getFileLastModifiedTime(filePath: String): Long {
        val file = File(filePath)
        return file.lastModified()
    }

    fun formatFileSize(fileSize: Long): String {
        val unit = 1024
        if (fileSize < unit) return "$fileSize B"
        val exp = (ln(fileSize.toDouble()) / ln(unit.toDouble())).toInt()
        val pre = "KMGTPE"[exp - 1]
        return String.format("%.1f %sB", fileSize / unit.toDouble().pow(exp.toDouble()), pre)
    }

    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
        var milliSeconds = ms
        val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds)
        milliSeconds -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
        if (!includeMillis) {
            return "${if (hours < 10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds"
        }
        milliSeconds -= TimeUnit.SECONDS.toMillis(seconds)
        milliSeconds /= 10
        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds "

    }

//    fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
//        var milliSeconds = ms
//        val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds)
//        milliSeconds -= TimeUnit.HOURS.toMillis(hours)
//        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds)
//        milliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
//        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds)
//        if (!includeMillis) {
//            return "${if (hours < 10) "0" else ""}$hours:" +
//                    "${if (minutes < 10) "0" else ""}$minutes:" +
//                    "${if (seconds < 10) "0" else ""}$seconds"
//        }
//        milliSeconds -= TimeUnit.SECONDS.toMillis(seconds)
//        milliSeconds /= 10
//        return "${if (hours < 10) "0" else ""}$hours:" +
//                "${if (minutes < 10) "0" else ""}$minutes:" +
//                "${if (seconds < 10) "0" else ""}$seconds:" +
//                "${if (milliSeconds < 10) "0" else ""}$milliSeconds"
//
//    }

    fun calculatePolyLineLength(polyLine: PolyLine): Float {
        var distance = 0f
        for (i in 0..polyLine.size - 2) {
            val pos1 = polyLine[i]
            val pos2 = polyLine[i + 1]

            val result = FloatArray(1)

            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    val arrayStoragePermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        else
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

    val arrayLocationPermission = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun calculateTotalCalories(runList: MutableList<Running>): Float {
        var totalCalories = 0f

        for (run in runList) {
            run.calo?.let {
                totalCalories += it
            }
        }

        return totalCalories
    }

    fun calculateTotalDistance(runList: MutableList<Running>): Float {
        var totalDistance = 0f

        for (run in runList) {
            run.distance?.let {
                totalDistance += it
            }
        }

        return totalDistance
    }

    fun calculateTotalDuration(runList: MutableList<Running>): Long {
        var totalDistance = 0L

        for (run in runList) {
            run.timesInMillis?.let {
                totalDistance += it
            }
        }

        return totalDistance
    }

    fun formatTime(timeString: String): String {
        val parts = timeString.split(":")
        if (parts.size != 3) {
            return timeString // Trả về nguyên mẫu nếu định dạng không đúng
        }

        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        val seconds = parts[2].toInt()

        val totalMinutes =
            hours * 60 + minutes + seconds / 60f // Tính tổng số phút dưới dạng số thập phân

        return String.format("%.2f", totalMinutes)
    }

    fun calculateAvgSpeeds(runList: MutableList<Running>): Float {
        var avgSpeed = 0f

        for (run in runList) {
            run.speeds?.let {
                avgSpeed += it
            }
        }

        return avgSpeed / runList.size
    }

    fun calculateAvgDistance(runList: MutableList<Running>): Float {
        var avgDistance = 0f

        for (run in runList) {
            run.distance?.let {
                avgDistance += it
            }
        }

        return avgDistance / runList.size
    }

    fun calculateAvgCalo(runList: MutableList<Running>): Float {
        var avgCalo = 0f

        for (run in runList) {
            run.calo?.let {
                avgCalo += it
            }
        }

        return avgCalo / runList.size
    }


    fun calculateAvgTimeInMillis(runningList: MutableList<Running>): Long {
        if (runningList.isEmpty()) {
            return 0L // Return 0 if the list is empty to avoid division by zero
        }

        var totalTimesInMillis = 0L

        for (running in runningList) {
            val timeInMillis = running.timesInMillis ?: 0L
            totalTimesInMillis += timeInMillis
        }

        return totalTimesInMillis / runningList.size
    }

    fun convertMillisToMinutes(millis: Long): Float {
        return millis / 1000f / 60f
    }


    fun calculatePaceFromSpeed(speedInKmPerHour: Float): Float? {
        return if (speedInKmPerHour > 0) {
            60f / speedInKmPerHour
        } else {
            null // Tránh chia cho 0 nếu tốc độ là 0 hoặc âm
        }
    }

    fun animationProgress(progressBar: ProgressBar): ObjectAnimator {
        val progressAnimator =
            ObjectAnimator.ofInt(progressBar, "progress", 0, progressBar.progress)
        progressAnimator.duration = 1000
        return progressAnimator
    }

    fun getCurrentTime(): String {
        val currentTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return currentTime.format(formatter)
    }

    fun getCurrentDateAsString(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val currentDate = Date()
        return sdf.format(currentDate)
    }

    fun Context.getIntentSend(uri: String, messageText: String): Intent {
        val fileUri = FileProvider.getUriForFile(this, "${packageName}.provider", File(uri))
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, messageText)
            putExtra(Intent.EXTRA_STREAM, fileUri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun convertDateFormat(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.US)
        val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US)

        val date = LocalDate.parse(inputDate, inputFormatter)
        return date.format(outputFormatter)
    }

    fun convertDateFormat2(inputDate: String, languageCode: String): String {
        val locale = Locale(languageCode)
        val possiblePatterns = listOf(
            "dd MMM yyyy", // Pattern 1: "02 Nov 2023"
            "dd MMMM yyyy", // Pattern 2: "02 November 2023"
            "dd-MM-yyyy" // Pattern 3: "02-11-2023"
            // Add more patterns as needed
        )

        var outputDate = ""
        var index = 0

        while (outputDate.isEmpty() && index < possiblePatterns.size) {
            try {
                val formatter = DateTimeFormatter.ofPattern(possiblePatterns[index], locale)
                val date = LocalDate.parse(inputDate, formatter)
                outputDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US))
            } catch (e: Exception) {
                index++
            }
        }

        return outputDate
    }


    fun bindChart(
        distanceForMonday: Float,
        distanceForTuesday: Float,
        distanceForWednesday: Float,
        distanceForThursday: Float,
        distanceForFriday: Float,
        distanceForSaturday: Float,
        distanceForSunday: Float,
        title: String,
        barChart: BarChart,
        codeColor: String
    ) {
        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(0f, distanceForMonday))
        entries.add(BarEntry(1f, distanceForTuesday))
        entries.add(BarEntry(2f, distanceForWednesday))
        entries.add(BarEntry(3f, distanceForThursday))
        entries.add(BarEntry(4f, distanceForFriday))
        entries.add(BarEntry(5f, distanceForSaturday))
        entries.add(BarEntry(6f, distanceForSunday))
        val set = BarDataSet(entries, title)
        // 1 màu
        set.color = Color.parseColor(codeColor)
        // Tạo danh sách các màu cho từng cột
//        val colors = mutableListOf<Int>()
//        colors.add(Color.parseColor("#FF5733")) // Màu cho cột 1
//        colors.add(Color.parseColor("#FFD700")) // Màu cho cột 2
//        colors.add(Color.parseColor("#00FF00")) // Màu cho cột 3
//        colors.add(Color.parseColor("#0000FF")) // Màu cho cột 4
//        colors.add(Color.parseColor("#FF00FF")) // Màu cho cột 5
//        colors.add(Color.parseColor("#00FFFF")) // Màu cho cột 6
//        colors.add(Color.parseColor("#FF4500")) // Màu cho cột 7
//        set.colors=colors
        val data = BarData(set)
        data.barWidth = 0.9f // set custom bar width
        barChart.setFitBars(true) // make the x-axis fit exactly all bars
        barChart.description.isEnabled = false
        barChart.animateY(2000) // animate vertical 2000 milliseconds

        val xAxis = barChart.xAxis
        val leftYAxis = barChart.axisLeft
        val rightYAxis = barChart.axisRight

        // tắt đường line gióng từ trục x và y
        xAxis.setDrawGridLines(false)
        leftYAxis.setDrawGridLines(false)
        rightYAxis.setDrawGridLines(false)
        // tắt data cột bên phải
        rightYAxis.setDrawLabels(false)
        // tắt cột bên phải
        rightYAxis.isEnabled = false
        // cho data cột bên phải list rỗng
        // rightYAxis.valueFormatter = IndexAxisValueFormatter(emptyArray()))
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
        leftYAxis.axisMinimum = 0f
        barChart.data = data
        barChart.invalidate() // refresh
    }


    fun formattedValue(format: Float): String {
        return if (format.isFinite()) {
            String.format("%.2f", format)
        } else {
            "0.00" // or any default value you prefer if calo is null or infinite
        }
    }

    fun scanMedia(context: Context, path: String) {
        val file = File(path)
        val uri = Uri.fromFile(file)
        val scanFileIntent = Intent(
            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri
        )
        context.sendBroadcast(scanFileIntent)
    }

    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


     fun isPlan(context: Context):Boolean{
        val sharePrefs=SharePrefs(context)
        return sharePrefs.isPlan || sharePrefs.isPlan1 || sharePrefs.isPlan2 || sharePrefs.isPlan3
    }
}