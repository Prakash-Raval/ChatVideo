package com.example.chatvideo.ui.fragment.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chatvideo.databinding.FragmentSignUpBinding
import com.example.chatvideo.model.User
import com.example.chatvideo.ui.viewmodel.auth.SignUpViewModel
import com.example.chatvideo.util.Constant.dialog
import com.example.chatvideo.util.Constant.showDialog

class SignUpFragment : Fragment() {

    private lateinit var mBinding: FragmentSignUpBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[SignUpViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSignUpBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply {

            bntSignIn.setOnClickListener {
                findNavController().navigateUp()
            }

            btnSinUp.setOnClickListener {
                when {
                    TextUtils.isEmpty(txtName.text!!.toString()) -> {
                        txtName.error =
                            "Required field"
                        txtName.requestFocus()
                        return@setOnClickListener
                    }
                    TextUtils.isEmpty(txtEmail.text!!.toString()) -> {
                        txtEmail.error =
                            "Required field"
                        txtEmail.requestFocus()
                        return@setOnClickListener
                    }
                    TextUtils.isEmpty(txtPassword.text!!.toString()) -> {
                        txtPassword.error =
                            "Required field"
                        txtPassword.requestFocus()
                        return@setOnClickListener
                    }
                    TextUtils.isEmpty(txtConfirmPassword.text!!.toString()) -> {
                        txtConfirmPassword.error =
                            "Required field"
                        txtConfirmPassword.requestFocus()
                        return@setOnClickListener
                    }
                    else -> {
                        val name = mBinding.txtName.text.toString()
                        val email = mBinding.txtEmail.text.toString()
                        val password = mBinding.txtPassword.text.toString()
                        val confirmPassword = mBinding.txtConfirmPassword.text.toString()

                        if (confirmPassword == password) {
                            showDialog(requireActivity())
                            viewModel.createAccount(
                                User(
                                    name = name,
                                    email = email,
                                    password = password
                                )
                            )
                        } else {
                            txtConfirmPassword.error =
                                "not equal the password"
                            txtConfirmPassword.requestFocus()
                            return@setOnClickListener
                        }
                    }
                }
            }

            viewModel.getSignUp().observe(viewLifecycleOwner) {
                if (it) {
                    Toast.makeText(requireContext(), "True", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                dialog.dismiss()
            }

        }
    }

}