package com.example.chatvideo.ui.fragment.home

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.example.chatvideo.R
import com.example.chatvideo.adapter.UserAdapter
import com.example.chatvideo.databinding.FragmentUserListBinding
import com.example.chatvideo.model.NotificationData
import com.example.chatvideo.model.User
import com.example.chatvideo.ui.activity.MainActivity
import com.example.chatvideo.ui.viewmodel.home.UserViewModel
import com.example.chatvideo.util.Constant.CALL_AUDIO
import com.example.chatvideo.util.Constant.CALL_VIDEO
import com.example.chatvideo.util.Constant.INVITATION_RESPONSE
import com.example.chatvideo.util.Constant.REMOTE_MSG_INVITATION
import com.example.chatvideo.util.Constant.TYPE_CALL
import com.example.chatvideo.util.Constant.USER_DATA
import com.example.chatvideo.util.Constant.editor
import com.example.chatvideo.util.Constant.getUserProfile
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class UserListFragment : Fragment(), UserAdapter.OnClick {

    private lateinit var mBinding: FragmentUserListBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }

    private lateinit var user: User
    private val userAdapter by lazy {
        UserAdapter(arrayListOf(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        mBinding = FragmentUserListBinding.inflate(inflater, container, false).apply {
            executePendingBindings()
        }
        return mBinding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllUser()

        activity?.applicationContext?.let {
            viewModel.getProfile(it) {
                user = getUserProfile(it)
            }
        }

        activity?.applicationContext?.let { viewModel.updateUserToken(it) }



        viewModel.userLiveData.observe(viewLifecycleOwner) {
            userAdapter.apply {
                userList.clear()
                userList.addAll(it)
                notifyDataSetChanged()
            }
        }

        mBinding.userList.apply {
            adapter = userAdapter
        }

    }


    override fun startVideoMeeting(user: User) {
        when {
            !user.status -> {
                Snackbar.make(
                    requireView(),
                    "${user.name} is not online",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            else -> {
                findNavController()
                    .navigate(
                        R.id.action_userListFragment_to_outgoingInvitationFragment,
                        Bundle().apply {
                            putParcelable(USER_DATA, user)
                            putString(TYPE_CALL, CALL_VIDEO)
                        })
            }
        }
    }

    override fun startVoiceMeeting(user: User) {
        when {
            !user.status -> {
                Snackbar.make(
                    requireView(),
                    "${user.name} is not available for meeting",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            else -> {
                findNavController().navigate(
                    R.id.action_userListFragment_to_outgoingInvitationFragment,
                    Bundle().apply {
                        putParcelable(USER_DATA, user)
                        putString(TYPE_CALL, CALL_AUDIO)
                    })
            }
        }
    }

    private val invitationBroadcastManager = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val type = intent!!.getParcelableExtra<NotificationData>("data")
            Timber.e("iii msg ${type.toString()}")
            when (type!!.type) {
                REMOTE_MSG_INVITATION -> {
                    Timber.e("iii msg", type.type)
                    findNavController().navigate(
                        R.id.action_userListFragment_to_callFragment,
                        Bundle().apply {
                            putParcelable(
                                USER_DATA,
                                type
                            )
                            putString(TYPE_CALL, CALL_AUDIO)
                        })
                }
                else -> {
                    Timber.e("iii msg2", type.type)
                }
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                CoroutineScope(Dispatchers.IO).launch {

                    viewModel.getLogOut()
                    delay(500)
                    logout()
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(
                invitationBroadcastManager,
                IntentFilter(INVITATION_RESPONSE)
            )
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(invitationBroadcastManager)
        super.onStop()
    }

    private fun logout() {
        editor(requireContext())!!.clear().clear().apply()
        requireActivity().finish()
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}