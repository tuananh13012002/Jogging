package com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.bmi_calculator

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.jogging.walking.weightloss.pedometer.steptracker.R
import com.jogging.walking.weightloss.pedometer.steptracker.base.AbsFragment
import com.jogging.walking.weightloss.pedometer.steptracker.databinding.FragmentBmiBinding
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beGone
import com.jogging.walking.weightloss.pedometer.steptracker.extension.beVisible
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.bmi_calculator.adapter.BmiAdapter
import com.jogging.walking.weightloss.pedometer.steptracker.ui.explore.fragment.bmi_calculator.viewmodel.BmiViewModel
import java.util.Locale
import javax.inject.Inject

class BmiFr @Inject constructor() : AbsFragment<FragmentBmiBinding>() {
    private val viewModel by viewModels<BmiViewModel>()

    private var height: Float = 0.0f
    private var weight: Float = 0.0f
    private var bmi: Float = 0.0f
    private var ageSelect: Int = 0
    private var genderSelect: String = ""
    private var unitSelect: Boolean = true
    private var weightIcon = R.drawable.ic_kg
    private var heightIcon = R.drawable.ic_cm
    private var isUpdatingEditText = false

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateIcons(unitSelect, isValidate())
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_bmi
    }

    override fun initView() {

        viewModel.getUsers.observe(this@BmiFr) { user ->
            initSpinner2(user.gender ?: "", user.age ?: 0)
            user.units?.let { checkUnit(it) }
            binding.etWeight.setText(user.weight)
            binding.etHeight.setText(user.height)
        }
        binding.rcvBmi.adapter = BmiAdapter(
            BmiAdapter.dummyData(requireContext())
        )

        initSpinner()
        bindObserve()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initAction() {
        binding.scrollView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isAcceptingText) {
                    imm.hideSoftInputFromWindow(binding.scrollView.windowToken, 0)
                }
            }
            binding.etHeight.clearFocus()
            binding.etWeight.clearFocus()
            false
        }
        binding.etHeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                // Tắt bàn phím
                hideVirtualKeyboard(binding.etHeight)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.etWeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                // Tắt bàn phím
                hideVirtualKeyboard(binding.etWeight)
                return@setOnEditorActionListener true
            }
            false
        }

        binding.btnCalculatorDisable.setOnClickListener {
            Toast.makeText(requireContext(),
                getString(R.string.please_enter_all_information), Toast.LENGTH_SHORT)
                .show()
        }

        binding.btnCalculatorEnable.setOnClickListener {
            hideVirtualKeyboard(it)
            binding.etHeight.clearFocus()
            binding.etWeight.clearFocus()
            height = binding.etHeight.text.toString().toFloat()
            weight = binding.etWeight.text.toString().toFloat()
            if (!unitSelect) {
                height *= 2.54f
                weight *= 0.45359236f
            }
            calculateBMR()
            calculateBMI()
        }
    }

    fun updateIcons(unitSelect: Boolean, isValidate: Boolean) {
        if (!unitSelect && isValidate) {
            if (!isUpdatingEditText) {
                isUpdatingEditText = true
                height = binding.etHeight.text.toString().toFloat() / 2.54f
                weight = binding.etWeight.text.toString().toFloat() * 2.2046225f
                binding.etHeight.setText(String.format(Locale.US, "%.1f", height))
                binding.etWeight.setText(String.format(Locale.US, "%.1f", weight))
            }
        }
        if (unitSelect) {
            weightIcon = if (isValidate) R.drawable.ic_kg_enable else R.drawable.ic_kg
            heightIcon = if (isValidate) R.drawable.ic_cm_enable else R.drawable.ic_cm
        } else {
            weightIcon = if (isValidate) R.drawable.ic_ib_enable else R.drawable.ic_ib
            heightIcon = if (isValidate) R.drawable.ic_inch_enable else R.drawable.ic_inch
        }

        if (isValidate) {
            binding.btnCalculatorEnable.beVisible()
        } else {
            binding.btnCalculatorEnable.beGone()
        }

        binding.etWeight.setCompoundDrawablesWithIntrinsicBounds(0, 0, weightIcon, 0)
        binding.etHeight.setCompoundDrawablesWithIntrinsicBounds(0, 0, heightIcon, 0)
    }


    private fun hideVirtualKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun bindObserve() {
        binding.etWeight.addTextChangedListener(textWatcher)
        binding.etHeight.addTextChangedListener(textWatcher)
    }

    private fun isValidate(): Boolean {
        if (binding.etWeight.text.toString().isNotEmpty() &&
            binding.etHeight.text.toString().isNotEmpty()
        ) {
            return true
        }
        return false
    }

    private fun calculateBMI() {
        bmi = calculateBMIValue(height, weight)
        binding.resultBmi.text = String.format("%.1f", bmi)
        rotateAnimation(bmiToRotationAngle(bmi), binding.bmiNeedle)
    }

    private fun calculateBMIValue(height: Float, weight: Float): Float {
        return weight / ((height / 100) * (height / 100))
    }

    private fun calculateBMR() {

        val bmr = if (genderSelect == "Male") {
            (88.362f + (13.397f * weight) + (4.799f * height) - (5.677f * ageSelect.toFloat()))
        } else {
            (447.593f + (9.247f * weight) + (3.098f * height) - (4.330f * ageSelect.toFloat()))
        }
        binding.resultBrm.text = String.format("%.1f", bmr)

    }

    private fun bmiToRotationAngle(bmi: Float): Float {
        val bmiRanges = arrayOf(17f, 18.5f, 25f, 30f, 35f, 40f)
        val angleRanges = arrayOf(0f, 36f, 72f, 108f, 144f, 180f)

        for (i in 0 until bmiRanges.size - 1) {
            val lowerBmi = bmiRanges[i]
            val upperBmi = bmiRanges[i + 1]
            val lowerAngle = angleRanges[i]
            val upperAngle = angleRanges[i + 1]

            if (bmi >= lowerBmi && bmi < upperBmi) {
                val bmiRange = upperBmi - lowerBmi
                val angleRange = upperAngle - lowerAngle
                val normalizedBmi = (bmi - lowerBmi) / bmiRange
                return normalizedBmi * angleRange + lowerAngle
            }
            if (bmi < 17f){
                return 0f
            }
        }

        return 180f
    }


    private var currentRotation = 0f // Biến lưu trữ góc quay hiện tại của ImageView

    private fun rotateAnimation(degree: Float?, arrowView: ImageView?) {
        val animator = ValueAnimator.ofFloat(currentRotation, degree ?: 0f)
        animator.duration = 500 // Thời gian chuyển đổi (ms)
        animator.interpolator =
            LinearInterpolator() // Sử dụng LinearInterpolator cho animation mượt hơn

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            arrowView?.rotation = animatedValue // Cập nhật góc quay của ImageView
            currentRotation = animatedValue // Cập nhật góc quay hiện tại
        }
        animator.start()
    }

    private fun initSpinner() {
        val genders = resources.getStringArray(R.array.gender_string_list)
        val ages = mutableListOf<Int>()
        for (i in 15..99) {
            ages.add(i)
        }
        val adapterGender =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, genders) }
        adapterGender?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapterGender
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGender = genders[position]
                genderSelect = selectedGender
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val adapterAge =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, ages) }
        adapterAge?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAge.adapter = adapterAge
        binding.spinnerAge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedAge = ages[position]
                ageSelect = selectedAge
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
        val adapterGender =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, genders) }
        adapterGender?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGender.adapter = adapterGender
        val positionGender = genders.indexOf(gender)
        if (positionGender >= 0) {
            binding.spinnerGender.setSelection(positionGender)
        }
        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGender = genders[position]
                viewModel.gender.postValue(selectedGender)
                genderSelect =  selectedGender
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        val adapterAge = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, ages) }
        adapterAge?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAge.adapter = adapterAge
        val positionAge = ages.indexOf(age)
        if (positionAge >= 0) {
            binding.spinnerAge.setSelection(positionAge)
        }
        binding.spinnerAge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedAge = ages[position]
                viewModel.age.postValue(selectedAge)
                ageSelect = selectedAge
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun checkUnit(unit: String) {
        unitSelect = (unit == "km")
        updateIcons(unitSelect, isValidate())

    }


}