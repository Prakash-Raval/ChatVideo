package com.example.chatvideo.ui.fragment.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.example.chatvideo.databinding.FragmentIncomingCallBinding
import com.example.chatvideo.model.NotificationData
import com.example.chatvideo.ui.viewmodel.call.CallViewModel
import com.example.chatvideo.util.Constant.INVITATION_ACCEPTED
import com.example.chatvideo.util.Constant.INVITATION_CANCEL
import com.example.chatvideo.util.Constant.INVITATION_REJECTED
import com.example.chatvideo.util.Constant.INVITATION_RESPONSE
import com.example.chatvideo.util.Constant.TYPE
import com.example.chatvideo.util.Constant.USER_DATA
import com.example.chatvideo.util.Constant.playerRingtone
import com.example.chatvideo.util.Constant.stopRingtone

class IncomingCallFragment : Fragment() {

    private lateinit var mBinding: FragmentIncomingCallBinding
    private lateinit var dataNotification: NotificationData
    private var isAudio = false

    private val argumentData by lazy {
        requireArguments().getParcelable<NotificationData>(USER_DATA)!!
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[CallViewModel::class.java]
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(
                initBroadcastManager,
                IntentFilter(INVITATION_RESPONSE)
            )
        playerRingtone(requireActivity())
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentIncomingCallBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataNotification = argumentData
        mBinding.apply {
            txtName.text = dataNotification.name
            txtUserName.text = dataNotification.name
            txtEmail.text = dataNotification.email
        }

        isAudio = requireActivity().intent.getBooleanExtra(TYPE, false)

        viewModel.callLivedata.observe(viewLifecycleOwner) {
            if (it)
                stopRingtone()
            viewModel.startCalling(requireContext(), dataNotification.meetingRoom, isAudio)
            findNavController().navigateUp()
        }

        mBinding.btnFinshCall.setOnClickListener {
            stopRingtone()
            viewModel.sendRemoteMessage(
                requireContext(), NotificationData(
                    name = dataNotification.name, meetingType = dataNotification.meetingType,
                    type = INVITATION_REJECTED, email = dataNotification.email,
                    senderToken = dataNotification.receiverToken,
                    receiverToken = dataNotification.senderToken,
                    acceptedOrRejected = false
                ),
                INVITATION_REJECTED, dataNotification.senderToken
            )
            findNavController().navigateUp()
        }

        mBinding.btnStartCall.setOnClickListener {
            viewModel.sendRemoteMessage(
                requireContext(), NotificationData(
                    name = dataNotification.name, meetingType = dataNotification.meetingType,
                    type = INVITATION_ACCEPTED, email = dataNotification.email,
                    senderToken = dataNotification.receiverToken,
                    receiverToken = dataNotification.senderToken,
                    acceptedOrRejected = true
                ),
                INVITATION_ACCEPTED, dataNotification.senderToken
            )
            stopRingtone()
        }


    }


    private val initBroadcastManager = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val type = intent!!.getParcelableExtra<NotificationData>("data")
            when (type!!.type) {
                INVITATION_CANCEL -> {
                    stopRingtone()
                    findNavController().navigateUp()
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(initBroadcastManager)
    }


}