package com.jogging.walking.weightloss.pedometer.steptracker.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import com.bumptech.glide.Glide
import com.jogging.walking.weightloss.pedometer.steptracker.MyApplication.Companion.userCurrent
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsActivity
import com.jogging.walking.weightloss.pedometer.steptracker.data.models.User
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.ActivityProfileBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.extension.hideKeyboard
import com.jogging.walking.weightloss.pedometer.steptracker.extension.onClickListener
import com.jogging.walking.weightloss.pedometer.steptracker.ui.main_v2.MainActivityV2
import com.jogging.walking.weightloss.pedometer.steptracker.ui.permission.PermissionAct
import com.jogging.walking.weightloss.pedometer.steptracker.ui.profile.viewmodel.ProfileViewModel
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ProfileAct @Inject constructor() : AbsActivity<ActivityProfileBinding>() {
    private val viewModel by viewModels<ProfileViewModel>()
    private var userUpdate: User? = null
    private var weightIcon = R.drawable.ic_kg_enable
    private var heightIcon = R.drawable.ic_cm_enable
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(intent.getIntExtra(TYPE_PROFILE, 0)==1){
                if (isValidate()) binding.btnSave.setCardBackgroundColor(Color.parseColor("#663993"))
                else binding.btnSave.setCardBackgroundColor(Color.parseColor("#C5C9D3"))
            }else{
                if (s.toString().isNotEmpty()) binding.imgEdit.beVisible() else binding.imgEdit.beGone()
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }


    override fun initView() {

        when (intent.getIntExtra(TYPE_PROFILE, 0)) {
            1 -> {
                viewModel.getUsers.observe(this@ProfileAct) { user ->
                    if (user != null) {
                        startActivity(MainActivityV2.getIntent(this,1))
                        finish()
                    }
                }
            }

            2 -> {
                binding.contentStarted.beGone()
                binding.progress.root.beVisible()
                binding.txtUsername.beVisible()
                binding.imgBack.beVisible()
                binding.layoutUsername.beGone()
                binding.layoutUnit.beGone()
                binding.btnSave.beGone()
                binding.tvTitle.text = getString(R.string.title_user)
                binding.imgBack.setOnClickListener { finish() }
                viewModel.getUsers.observe(this@ProfileAct) { user ->
                    userUpdate = user
                    if (user != null) binding.progress.root.beGone()
                    binding.txtUsername.text = user.username
                    binding.edWeight.setText(user.weight)
                    binding.edHeight.setText(user.height)
                    Glide.with(this).load(user.avatar).centerCrop().circleCrop().into(binding.imgAvt)
                    initSpinner2(user.gender ?: "", user.age ?: 0)
                    if (user.units == "km") {
                        weightIcon = R.drawable.ic_kg_enable
                        heightIcon = R.drawable.ic_cm_enable
                    } else {
                        weightIcon = R.drawable.ic_ib_enable
                        heightIcon = R.drawable.ic_inch_enable
                    }

                    binding.edWeight.setCompoundDrawablesWithIntrinsicBounds(0, 0, weightIcon, 0)
                    binding.edHeight.setCompoundDrawablesWithIntrinsicBounds(0, 0, heightIcon, 0)
                }
                binding.edWeight.addTextChangedListener(textWatcher)
                binding.edHeight.addTextChangedListener(textWatcher)
            }
        }
        initSpinner()
        initUnit()
        bindObserve()
    }

    override fun initAction() {
        binding.editAvt.onClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startResult.launch(intent)
        }

        binding.btnSave.onClickListener {
            if (isValidate()) {
                val username = binding.edUsername.text.toString()
                val urlAvt = viewModel.urlAvt.value ?: "https://i.ibb.co/tqtCWbq/User.png"
                val age = viewModel.age.value
                val height = binding.edHeight.text.toString()
                val weight = binding.edWeight.text.toString()
                sharePrefs.weight = weight
                val gender = viewModel.gender.value
                val units = viewModel.units.value ?: "km"
                if (binding.edUsername.text.toString().trim().isNotBlank()){
                    val user = User(null, username, urlAvt, age, height, weight, mutableListOf(), gender, units)
                    userCurrent = user
                    viewModel.insertUser(user)
                }
                startActivity(MainActivityV2.getIntent(this,1))
                finish()
            } else {
                Toast.makeText(this, getString(R.string.validate), Toast.LENGTH_SHORT).show()
            }
        }
        binding.imgEdit.onClickListener {
            val urlAvt = viewModel.urlAvt.value ?: userUpdate?.avatar
            val age = viewModel.age.value
            val height = binding.edHeight.text.toString()
            val weight = binding.edWeight.text.toString()
            val gender = viewModel.gender.value
            val user = User(userUpdate?.id ?: 0L, userUpdate?.username ?: "", urlAvt, age, height, weight, userUpdate?.listRun ?: mutableListOf(), gender, userUpdate?.units ?: "")
            viewModel.updateUser(user)
            finish()
        }
        binding.main.setOnClickListener {
            binding.main.hideKeyboard()
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_profile
    }

    override fun bindViewModel() {
    }

    private val startResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val selectedImageUri = data.data
                    val documentFile: DocumentFile? = selectedImageUri?.let { DocumentFile.fromSingleUri(this, it) }
                    val fileAvt = documentFile?.let { convertDocumentFileToFile(this, it) }
                    if(intent.getIntExtra(TYPE_PROFILE, 0)==1){
                    }else{
                        if (userUpdate?.avatar != fileAvt?.path) binding.imgEdit.beVisible() else binding.imgEdit.beGone()
                    }
                    viewModel.urlAvt.postValue(fileAvt?.path)
                    Glide.with(this)
                        .load(selectedImageUri)
                        .centerCrop()
                        .circleCrop()
                        .into(binding.imgAvt)
                }
            }
        }

    private fun initSpinner() {
        val genders = resources.getStringArray(R.array.gender_string_list)
        val ages = mutableListOf<Int>()
        for (i in 15..99) {
            ages.add(i)
        }
        val adapterGender = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapterGender
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedGender = genders[position]
                viewModel.gender.postValue(selectedGender)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val adapterAge = ArrayAdapter(this, android.R.layout.simple_spinner_item, ages)
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAge.adapter = adapterAge
        binding.spinnerAge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedAge = ages[position]
                viewModel.age.postValue(selectedAge)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initSpinner2(gender: String, age: Int) {
        val genders = resources.getStringArray(R.array.gender_string_list)
        val ages = mutableListOf<Int>()
        for (i in 15..99) {
            ages.add(i)
        }
        val adapterGender = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapterGender
        val positionGender = genders.indexOf(gender)
        if (positionGender >= 0) {
            binding.spinnerGender.setSelection(positionGender)
        }
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedGender = genders[position]
                if (gender != selectedGender) binding.imgEdit.beVisible() else binding.imgEdit.beGone()
                viewModel.gender.postValue(selectedGender)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val adapterAge = ArrayAdapter(this, android.R.layout.simple_spinner_item, ages)
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAge.adapter = adapterAge
        val positionAge = ages.indexOf(age)
        if (positionAge >= 0) {
            binding.spinnerAge.setSelection(positionAge)
        }
        binding.spinnerAge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedAge = ages[position]
                if (age != selectedAge) binding.imgEdit.beVisible() else binding.imgEdit.beGone()
                viewModel.age.postValue(selectedAge)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun initUnit() {
        val icKg = ContextCompat.getDrawable(this, R.drawable.ic_kg)
        val icCm = ContextCompat.getDrawable(this, R.drawable.ic_cm)
        val icIb = ContextCompat.getDrawable(this, R.drawable.ic_ib)
        val icInch = ContextCompat.getDrawable(this, R.drawable.ic_inch)
        binding.rdoKm.setOnClickListener {
            binding.edWeight.setCompoundDrawablesWithIntrinsicBounds(null, null, icKg, null)
            binding.edHeight.setCompoundDrawablesWithIntrinsicBounds(null, null, icCm, null)
            binding.rdoMi.isChecked = false
            viewModel.units.postValue("km")
        }
        binding.rdoMi.setOnClickListener {
            binding.edWeight.setCompoundDrawablesWithIntrinsicBounds(null, null, icIb, null)
            binding.edHeight.setCompoundDrawablesWithIntrinsicBounds(null, null, icInch, null)
            binding.rdoKm.isChecked = false
            viewModel.units.postValue("mi")
        }
    }

    private fun isValidate(): Boolean {
        val username = binding.edUsername.text.toString().trim()
        val weight = binding.edWeight.text.toString()
        val height = binding.edHeight.text.toString()
        if (username.isNotBlank() && weight.isNotEmpty() && height.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun bindObserve() {
        binding.edUsername.addTextChangedListener(textWatcher)
        binding.edWeight.addTextChangedListener(textWatcher)
        binding.edHeight.addTextChangedListener(textWatcher)
    }

    private fun convertDocumentFileToFile(context: Context, documentFile: DocumentFile): File? {
        val inputStream = context.contentResolver.openInputStream(documentFile.uri)

        if (inputStream != null) {
            val outputFile = File(context.cacheDir, documentFile.name ?: "converted_file")
            val outputStream = FileOutputStream(outputFile)

            val buffer = ByteArray(4 * 1024) // 4K buffer size
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            inputStream.close()
            outputStream.close()

            return outputFile
        }

        return null
    }

    companion object {
        private const val TYPE_PROFILE = "Type_Profile"
        fun getIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, ProfileAct::class.java)
            intent.putExtra(TYPE_PROFILE, type)
            return intent
        }
    }

}