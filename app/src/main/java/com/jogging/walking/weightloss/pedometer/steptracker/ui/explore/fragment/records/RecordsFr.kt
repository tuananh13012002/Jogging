package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.records

import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentRecordsBinding
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.ListenerShare
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.listener.TypeShare
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.records.adapter.RecordsAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.records.viewmodel.RecordViewModel
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil.getIntentSend
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class RecordsFr @Inject constructor() : AbsFragment<FragmentRecordsBinding>(),ListenerShare {
    private val viewModel by viewModels<RecordViewModel>()
    private var fileShare: File? = null

    override fun getLayoutRes() = R.layout.fragment_records

    override fun initAction() {
    }

    override fun initView() {
        viewModel.getRuns.observe(this){
            binding.rcvRecords.adapter = RecordsAdapter(RecordsAdapter.dummyData,it, requireContext(),this)
        }
    }

    override fun share(type: TypeShare, view: View) {
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        when (type) {
            TypeShare.DISTANCE -> {
                saveBitmapToFile(fileName, view)
            }

            TypeShare.DURATION -> {
                saveBitmapToFile(fileName, view)
            }

            TypeShare.CALO -> {
                saveBitmapToFile(fileName, view)
            }

            TypeShare.SPEED -> {
                saveBitmapToFile(fileName, view)
            }
            TypeShare.PACE -> {
                saveBitmapToFile(fileName, view)
            }
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