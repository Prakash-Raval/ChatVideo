package com.example.chatvideo.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chatvideo.R
import com.example.chatvideo.databinding.ItemUserBinding
import com.example.chatvideo.model.User

class UserAdapter(val userList: ArrayList<User>, val itemClick: OnClick) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(private val mBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        fun bind(user: User) {
            mBinding.apply {
                this.user = user
                if (!user.status) tvOnlineStatus.visibility=View.GONE
                btnCallAudio.setOnClickListener {
                    itemClick.startVoiceMeeting(user)
                }

                btnCallVideo.setOnClickListener {
                    itemClick.startVideoMeeting(user)
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_user,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount() = userList.size

    interface OnClick {
        fun startVideoMeeting(user: User)
        fun startVoiceMeeting(user: User)
    }
}