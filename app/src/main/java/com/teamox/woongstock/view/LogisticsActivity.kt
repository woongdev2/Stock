package com.teamox.woongstock.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.teamox.woongstock.R
import com.teamox.woongstock.common.DatabaseRepository
import com.teamox.woongstock.databinding.ActivityLogisticsBinding
import com.teamox.woongstock.viewmodel.LogisticsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class LogisticsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLogisticsBinding
    private lateinit var viewModel: LogisticsViewModel
    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_logistics)

        val intent = getIntent()
        productId = intent.getIntExtra("item_id", -1)
        viewModel = LogisticsViewModel(DatabaseRepository(this), productId)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        init()
    }

    fun init(){
        setObserve()
        setOnClickListener()
    }

    fun setOnClickListener(){
        binding.btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedCalendar.time)

                binding.btnDate.text = formattedDate
            }, year, month, day)
            datePickerDialog.show()
        }


        binding.btnMemo.setOnClickListener {
            showInputDialog(this, "제목", "텍스트를 입력하세요.", onTextEntered = { enteredText ->
                // 입력된 텍스트 처리
                binding.btnMemo.text = enteredText
            })
        }

        binding.btnConfirm.setOnClickListener {

        }

    }
    fun setObserve(){
        viewModel.increment.observe(this, Observer {
            if (binding.etQuantity.text.toString() != it) {
                binding.etQuantity.setText(it)
            }
        })

        viewModel.finishEvent.observe(this) {
            val resultData = it
            val intent = Intent().apply {
                putExtra("resultKey", resultData)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    fun showInputDialog(context: Context, title: String, message: String, onTextEntered: (String) -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT // 텍스트 입력 타입 설정
        builder.setView(input)

        builder.setPositiveButton("확인") { dialog, _ ->
            val text = input.text.toString()
            onTextEntered(text)
            dialog.dismiss()
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

}