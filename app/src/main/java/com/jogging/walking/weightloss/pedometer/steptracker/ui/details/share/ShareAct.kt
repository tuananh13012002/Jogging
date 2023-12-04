package com.jogging.walking.weightloss.pedometer.steptracker.ui.details.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.activity.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityShareBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.details.DetailsViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.getIntentSend
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.scanMedia
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.viewToBitmap
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ShareAct @Inject constructor() : AbsActivity<ActivityShareBinding>() {

    private val viewModel by viewModels<DetailsViewModel>()
    private var fileShare: File? = null

    override fun initView() {
        val id = intent.getLongExtra(SHARE_RUN, -1L)
        viewModel.getRunById(id).observe(this) {
            it?.let { running ->
                val duration = DeviceUtil.getFormattedStopWatchTime(running.timesInMillis ?: 0L)
                val distance = String.format("%.2f", running.distance?.div(1000f))
                val calo = String.format("%.2f", running.calo)
                val time = DeviceUtil.formatTime(duration)
                binding.txtDuration.text = running.duration
                binding.imgMap.setImageBitmap(running.imageMap)
                binding.txtDistance.text = distance
                binding.txtDuration.text = time
                binding.txtCalo.text = calo
                binding.txtContentShare.text = getString(R.string.content_share, distance, time, calo)
            }
        }
    }

    override fun initAction() {
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.btnShare.onClickListener {
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            saveBitmapToFile(fileName)
            startActivity(Intent.createChooser(getIntentSend(fileShare?.path!!,binding.txtContentShare.text.toString()), "Share with"))
        }
    }

    private fun saveBitmapToFile(fileName: String) {
        val directory = getOutputDirectory() // Lấy thư mục để lưu ảnh
        val file = File(directory, fileName)

//        val bitmap = inputBitmap.copy(
//            inputBitmap.config,
//            true
//        ) // Tạo bản sao có thể thay đổi của bitmap đầu vào
        val bitmap2 = viewToBitmap(binding.viewMap)
//        val combinedBitmap = combineBitmapsOnRightBottom(bitmap,bitmap2,50)

        val stream = FileOutputStream(file)
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        fileShare = file

        // Thêm ảnh đã lưu vào MediaStore của hệ thống để nó hiển thị trong các ứng dụng xem ảnh
        scanMedia(this,file.path)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { file ->
            File(file, "Images").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }



    override fun getContentView() = R.layout.activity_share

    override fun bindViewModel() {
    }

    companion object {
        private const val SHARE_RUN = "Share_Run"
        fun getIntent(context: Context, id: Long): Intent {
            val intent = Intent(context, ShareAct::class.java)
            intent.putExtra(SHARE_RUN, id)
            return intent
        }
    }
}