package com.jogging.walking.weightloss.pedometer.steptracker.ui.permission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityPermissionBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.hasPermission
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.profile.ProfileAct
import com.jogging.walking.weightloss.pedometer.steptracker.utils.DeviceUtil
import javax.inject.Inject

class PermissionAct @Inject constructor() : AbsActivity<ActivityPermissionBinding>() {

    override fun initView() {
        if(!sharePrefs.isProfile){
            startActivity(ProfileAct.getIntent(this, 1))
            finish()
        }
    }

    override fun initAction() {
        setDefaultSwitch()
        binding.btnGo.onClickListener {
            if (binding.switch1.isChecked && binding.switch2.isChecked) {
                sharePrefs.isProfile = false
                startActivity(ProfileAct.getIntent(this, 1))
                finish()
            }
        }
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkStorePermission()
            }
        }
        binding.switch2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkLocationPermission()
            }
        }
        maxPermission()
    }


    override fun getContentView(): Int {
        return R.layout.activity_permission
    }

    override fun bindViewModel() {
    }

    private fun setDefaultSwitch() {
        binding.switch1.isChecked = hasPermission(DeviceUtil.arrayStoragePermission)
        if (binding.switch1.isChecked) {
            binding.switch1.isEnabled = false
        }
        binding.switch2.isChecked = hasPermission(DeviceUtil.arrayLocationPermission)
        if (binding.switch2.isChecked) {
            binding.switch2.isEnabled = false
        }
    }

    private fun checkStorePermission() {
        if (!hasPermission(DeviceUtil.arrayStoragePermission)) {
            ActivityCompat.requestPermissions(
                this,
                DeviceUtil.arrayStoragePermission,
                REQUEST_CODE_STORAGE_PERMISSION
            )
        }
    }

    private fun checkLocationPermission() {
        if (!hasPermission(DeviceUtil.arrayLocationPermission)) {
            ActivityCompat.requestPermissions(
                this,
                DeviceUtil.arrayLocationPermission,
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun maxPermission() {
        if (!binding.switch1.isChecked) {
            binding.switch1.setOnClickListener {
                indexClickPermissionStore++
                if (indexClickPermissionStore > 2) {
                    startSettingApp()
                }
            }
        }

        if (!binding.switch2.isChecked) {
            binding.switch2.setOnClickListener {
                indexClickPermissionLocation++
                if (indexClickPermissionLocation > 2) {
                    startSettingApp()
                }
            }
        }
    }

    private fun startSettingApp() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        requestAppSettingsLauncher.launch(intent)
    }

    private val requestAppSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (indexClickPermissionStore > 2) {
                    checkStorePermission()
                }
                if (indexClickPermissionLocation > 2) {
                    checkLocationPermission()
                }
            }
        }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (binding.switch1.isChecked && binding.switch2.isChecked) {
            binding.btnGo.visibility = View.VISIBLE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMISSION -> {
                if (permissionWasAllowed(grantResults)) {
                    binding.switch1.isChecked = true
                    binding.switch1.isEnabled = false
                } else {
                    binding.switch1.isChecked = false
                }
            }

            PERMISSION_REQUEST_CODE -> {
                if (permissionWasAllowed(grantResults)) {
                    if (!DeviceUtil.isLocationEnabled(this)) {
                        val locationSettingsIntent =
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(locationSettingsIntent)
                    }
                    binding.switch2.isChecked = true
                    binding.switch2.isEnabled = false
                } else {
                    binding.switch2.isChecked = false
                }
            }
        }
    }

    private fun permissionWasAllowed(arr: IntArray): Boolean {
        var granted = 0
        arr.forEach { it ->
            if (it == PackageManager.PERMISSION_DENIED) {
                granted++
            }
        }
        return granted == 0
    }

    override fun onResume() {
        super.onResume()
        binding.switch1.isChecked = DeviceUtil.hasStoragePermission(this)
        binding.switch2.isChecked = DeviceUtil.hasLocationPermission(this)
    }


    companion object {
        const val REQUEST_CODE_STORAGE_PERMISSION = 101
        const val PERMISSION_REQUEST_CODE = 123
        var indexClickPermissionStore = 0
        var indexClickPermissionLocation = 0
    }
}