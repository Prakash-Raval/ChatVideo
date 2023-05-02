package com.example.chatvideo.ui.fragment.auth

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.chatvideo.R
import com.example.chatvideo.databinding.FragmentSignInBinding
import com.example.chatvideo.ui.viewmodel.auth.SignInViewModel
import com.example.chatvideo.util.Constant.dialog
import com.example.chatvideo.util.Constant.showDialog
import com.google.android.material.snackbar.Snackbar

class SigInFragment : Fragment() {

    private lateinit var mBinding: FragmentSignInBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[SignInViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSignInBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.apply {

            btnSignUp.setOnClickListener {
                findNavController().navigate(
                    R.id.action_sigInFragment_to_signUpFragment
                )
            }

            btnSinIn.setOnClickListener {
                val email = txtEmail.text.toString()
                val password = txtPassword.text.toString()
                when {
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
                    else -> {
                        showDialog(requireActivity())

                        viewModel.signInWithEmailAndPassword(
                            requireContext(),
                            email = email,
                            password = password
                        )
                    }
                }

            }

        }

        viewModel.getSignIn().observe(viewLifecycleOwner) {

            when (it) {
                true -> {
                    findNavController().navigate(R.id.action_sigInFragment_to_userListFragment)
                }
                else -> Snackbar.make(
                    mBinding.root,
                    "please try again later",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()

        }


    }

}