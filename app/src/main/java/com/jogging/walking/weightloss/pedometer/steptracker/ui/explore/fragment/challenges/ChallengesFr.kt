package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.challenges

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.fragment.app.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentChallengesBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.challenges.viewmodel.ChallengesViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.ListenerShare
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.TypeShare
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.calculateTotalCalories
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.calculateTotalDistance
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.calculateTotalDuration
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.getIntentSend
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class ChallengesFr @Inject constructor() : AbsFragment<FragmentChallengesBinding>(), ListenerShare {
    private val viewModel by viewModels<ChallengesViewModel>()
    private var fileShare: File? = null

    override fun getLayoutRes() = R.layout.fragment_challenges

    override fun initAction() {
    }

    override fun initView() {
        initProgress()
    }

    private fun initProgress() {
        viewModel.getRuns.observe(viewLifecycleOwner) { runs ->
            val totalDistance = calculateTotalDistance(runs)
            val minutes = calculateTotalDuration(runs) / (1000 * 60)
            val calo = calculateTotalCalories(runs)
            val maxSpeedRun = runs.maxByOrNull { it.speeds ?: 0f }
            binding.progressDistance.bindData(totalDistance.toInt())
            binding.progressDuration.bindData(minutes.toInt())
            binding.progressCalo.bindData(calo.toInt())
            binding.progressSpeed.bindData(maxSpeedRun?.speeds?.toInt() ?: 0)
            binding.progressDistance.setListener(this)
            binding.progressDuration.setListener(this)
            binding.progressCalo.setListener(this)
            binding.progressSpeed.setListener(this)
        }
    }

    override fun share(type: TypeShare, view: View) {
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        when (type) {
            TypeShare.DISTANCE -> {
                saveBitmapToFile(fileName, binding.progressDistance)
            }

            TypeShare.DURATION -> {
                saveBitmapToFile(fileName, binding.progressDuration)
            }

            TypeShare.CALO -> {
                saveBitmapToFile(fileName, binding.progressCalo)
            }

            TypeShare.SPEED -> {
                saveBitmapToFile(fileName, binding.progressSpeed)
            }

            else -> {}
        }
        startActivity(Intent.createChooser(requireContext().getIntentSend(fileShare?.path!!,""), "Share with"))
    }

    private fun saveBitmapToFile(fileName: String, view: View) {
        val directory = getOutputDirectory() // Lấy thư mục để lưu ảnh
        val file = File(directory, fileName)

//        val bitmap = inputBitmap.copy(
//            inputBitmap.config,
//            true
//        ) // Tạo bản sao có thể thay đổi của bitmap đầu vào
        val bitmap2 = DeviceUtil.viewToBitmap(view)
//        val combinedBitmap = combineBitmapsOnRightBottom(bitmap,bitmap2,50)

        val stream = FileOutputStream(file)
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        fileShare = file

        // Thêm ảnh đã lưu vào MediaStore của hệ thống để nó hiển thị trong các ứng dụng xem ảnh
        DeviceUtil.scanMedia(requireContext(), file.path)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let { file ->
            File(file, "Images").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }
}