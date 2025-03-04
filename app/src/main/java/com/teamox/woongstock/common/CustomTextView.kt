package com.teamox.woongstock.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

class CustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    // Paint 객체를 생성해 원하는 스타일을 설정할 수 있습니다.
    private val paint = Paint().apply {
        color = Color.BLACK // * 기호의 색상 설정
        textSize = 40f // * 기호의 크기 설정
        isAntiAlias = true
    }

    // onDraw 메서드를 오버라이드하여 텍스트 뷰가 그려질 때 추가적으로 그릴 내용을 지정합니다.
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 텍스트를 얻어와서 그 길이를 계산합니다.
        val text = text.toString()
        val textWidth = paint.measureText(text)

        // * 기호를 텍스트 우측 상단에 그립니다.
        canvas?.drawText("******", textWidth, 0f, paint)
    }
}