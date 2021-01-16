package com.example.RentHelper.Activity

import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.RentHelper.Controller.AnimatorController
import com.example.RentHelper.R
import com.example.RentHelper.Tool.Observed
import com.example.RentHelper.Tool.Observer
import com.example.RentHelper.ViewModels.LoginActivityViewModel
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity(), Observer<LoginActivityViewModel> {
    private var clLogin: ConstraintLayout? = null
    private var btnConfirm: Button? = null
    private var btnCancel: Button? = null
    private var btnLogin: Button? = null
    private var btnRigester: Button? = null
    private var btnVerityCode: Button? = null
    private var tvForgot: TextView? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var etPassworkCheck: EditText? = null
    private var etVerityCode: EditText? = null
    private var etName: EditText? = null
    private var etPhone: EditText? = null
    private var tvErrorMessage: TextView? = null
    private var loginSet: ConstraintSet? = null
    private var forgotSet: ConstraintSet? = null
    private var rigesterSet: ConstraintSet? = null

    enum class Stage {
        LOGIN,
        REGISTER,
        FORGOT
    }

    private var stage: Stage =
        Stage.LOGIN
    private var loginActivityViewModel: LoginActivityViewModel? = LoginActivityViewModel()
    private fun registerToLogin() {
        loginSet?.applyTo(clLogin)
        val animatorSet = AnimatorSet()
        animatorSet.play(
            AnimatorController.transColor(
                btnRigester!!,
                700,
                0xffDFB064.toInt(),
                0x00000000
            ).build()
        )
            .with(
                AnimatorController.transColor(
                    btnLogin!!,
                    700,
                    0x00000000,
                    0xffDFB064.toInt()
                ).build()
            )
        animatorSet.start()
    }

    private fun loginToRegister() {
        rigesterSet?.applyTo(clLogin)
        val animatorSet = AnimatorSet()
        animatorSet.play(
            AnimatorController.transColor(
                btnRigester!!,
                700,
                0x00000000,
                0xffDFB064.toInt()
            ).build()
        )
            .with(
                AnimatorController.transColor(
                    btnLogin!!,
                    700,
                    0xffDFB064.toInt(),
                    0x00000000
                ).build()
            )
        animatorSet.start()
    }

    private fun loginToForgot() {
        forgotSet?.applyTo(clLogin)
        val animatorSet = AnimatorSet()
        animatorSet.play(
            AnimatorController.transColor(
                btnConfirm!!,
                700,
                0x00000000,
                0xffDFB064.toInt()
            ).build()
        )
            .with(AnimatorController.despir(btnConfirm!!, 700, 0f, 1f).build())

        animatorSet.start()
    }

    private fun forgotToLogin() {
        loginSet?.applyTo(clLogin)
        val animatorSet = AnimatorSet()
        animatorSet.play(
            AnimatorController.transColor(
                btnRigester!!,
                700,
                0xffDFB064.toInt(),
                0x00000000
            ).build()
        )
            .with(
                AnimatorController.transColor(
                    btnConfirm!!,
                    700,
                    0xffDFB064.toInt(),
                    0x00000000
                ).build()
            )
            .with(AnimatorController.despir(btnConfirm!!, 700, 1f, 0f).build())
        animatorSet.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        clLogin = findViewById(R.id.clLogin)
        btnConfirm = findViewById(R.id.btnConfirm)
        btnCancel = findViewById(R.id.btnCancel)
        btnLogin = findViewById(R.id.btnLogin)
        btnRigester = findViewById(R.id.btnRegister)
        btnVerityCode = findViewById(R.id.btnVerityCode)
        tvForgot = findViewById(R.id.tvForgot)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPassworkCheck = findViewById(R.id.etPassworkCheck)
        etVerityCode = findViewById(R.id.etVerityCode)
        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        loginSet = ConstraintSet().also { it.clone(clLogin) }
        forgotSet = ConstraintSet().also { it.clone(this, R.layout.detail_forgot) }
        rigesterSet = ConstraintSet().also { it.clone(this, R.layout.detail_register) }

        etEmail?.isHovered

        onBind(loginActivityViewModel!!)
        val transition = AutoTransition()
        transition.duration = 700
        TransitionManager.beginDelayedTransition(clLogin, transition)
        loginActivityViewModel?.onLoginSuccess = {
            getSharedPreferences("User", MODE_PRIVATE)
                .edit()
                .putString("email", etEmail?.text?.toString())
                .putString("password", etPassword?.text?.toString())
                .apply()
            finish()
            Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show()
        }
        loginActivityViewModel?.onRegisterSuccess = {
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, "註冊成功", Toast.LENGTH_SHORT).show()
        }
        loginActivityViewModel?.onModifySuccess = {
            forgotToLogin()
        }

        val pswSubs = object : Observed<String>() {
            override fun onRegister() {
            }

            override fun onUnRegister() {
            }

            override fun onCloseObserved() {
            }
        }
        val checkListener = object : Observer<String> {
            override fun onBind(observed: Observed<String>) {
                observed.register(this)
            }

            override fun update(data: String) {
                when {
                    etPassworkCheck?.text.toString() != etPassword?.text.toString() -> {
                        btnConfirm?.isEnabled = false
                        if (stage == Stage.REGISTER) {
                            btnRigester?.isEnabled = false
                        }
                    }
                    etPassworkCheck?.text.toString() == etPassword?.text.toString() -> {
                        btnConfirm?.isEnabled = true
                        if (stage == Stage.REGISTER) {
                            btnRigester?.isEnabled = true
                        }
                    }
                }
            }
        }
        checkListener.onBind(pswSubs)
        etPassworkCheck?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { pswSubs.notify(it.toString()) }
            }
        })
        etPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { pswSubs.notify(it.toString()) }
            }
        })
        FirebaseMessaging.getInstance().token.addOnCompleteListener { token->
            btnLogin?.setOnClickListener {
                token.result?.let {
                    loginActivityViewModel?.login(
                        etEmail?.text.toString(),
                        etPassword?.text.toString(),
                        it
                    )
                }
            }
        }

        btnConfirm?.setOnClickListener {
            loginActivityViewModel?.modify(
                etEmail?.text.toString(),
                etVerityCode?.text.toString(),
                etPassword?.text.toString()
            )
        }
        btnRigester?.setOnClickListener {
            transition.duration = 700
            TransitionManager.beginDelayedTransition(clLogin, transition)
            when (stage) {
                Stage.LOGIN -> {
                    tvErrorMessage?.text = ""
                    loginToRegister()
                    stage = Stage.REGISTER
                }
                Stage.REGISTER -> {
                    loginActivityViewModel?.register(
                        etEmail?.text.toString(),
                        etVerityCode?.text.toString(),
                        etPassword?.text.toString(),
                        etName?.text.toString(),
                        etPhone?.text.toString()
                    )
                }
                else -> {
                }
            }
        }
        btnCancel?.setOnClickListener {
            transition.duration = 700
            TransitionManager.beginDelayedTransition(clLogin, transition)
            when (stage) {
                Stage.LOGIN -> {
                }
                Stage.REGISTER -> {
                    tvErrorMessage?.text = ""
                    registerToLogin()
                    stage = Stage.LOGIN
                }
                Stage.FORGOT -> {
                    tvErrorMessage?.text = ""
                    forgotToLogin()
                    stage = Stage.LOGIN
                }
            }
        }
        btnVerityCode?.setOnClickListener {
            loginActivityViewModel?.sendVeritycode(etEmail?.text.toString())
        }
        tvForgot?.setOnClickListener {
            transition.duration = 700
            TransitionManager.beginDelayedTransition(clLogin, transition)
            when (stage) {
                Stage.LOGIN -> {
                    tvErrorMessage?.text = ""
                    loginToForgot()
                    stage = Stage.FORGOT
                }
                Stage.REGISTER -> {
                }
                Stage.FORGOT -> {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginActivityViewModel?.closeObserved()
        loginActivityViewModel = null
        clLogin = null
        btnConfirm = null
        btnCancel = null
        btnLogin = null
        btnRigester = null
        btnVerityCode = null
        tvForgot = null
        etEmail = null
        etPassword = null
        etPassworkCheck = null
        etVerityCode = null
        etName = null
        etPhone = null
        tvErrorMessage = null
        loginSet = null
        forgotSet = null
        rigesterSet = null
    }

    override fun onBind(observed: Observed<LoginActivityViewModel>) {
        observed.register(this)
    }

    override fun update(data: LoginActivityViewModel) {
        tvErrorMessage?.text = data.errorText
    }
}

