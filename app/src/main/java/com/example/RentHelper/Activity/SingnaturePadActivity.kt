package com.example.RentHelper.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import com.example.RentHelper.R
import com.github.gcacace.signaturepad.views.SignaturePad


class SingnaturePadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singnature_pad)
        val mySignaturPad :SignaturePad =findViewById(R.id.signature_pad)
        val btnClear:Button = findViewById(R.id.btnClear)
        val rgcolor:RadioGroup = findViewById(R.id.rgColor)
        rgcolor.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbtnBlue -> {
                    mySignaturPad.setPenColorRes(
                        R.color.blue
                    )
                }
                R.id.rbtnRed -> {
                    mySignaturPad.setPenColorRes(
                        R.color.red
                    )
                }
                R.id.rbtnBlack -> {
                    mySignaturPad.setPenColorRes(
                        R.color.black
                    )
                }
            }
        }
        btnClear.setOnClickListener { mySignaturPad.clear() }
        mySignaturPad.setOnSignedListener(object: SignaturePad.OnSignedListener{
            override fun onStartSigning() {
            }

            override fun onClear() {
            }
            override fun onSigned() {
            }
        })
    }
}