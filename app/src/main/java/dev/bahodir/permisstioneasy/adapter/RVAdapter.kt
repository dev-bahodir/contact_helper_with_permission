package dev.bahodir.permisstioneasy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import dev.bahodir.permisstioneasy.databinding.RvItemBinding
import dev.bahodir.permisstioneasy.user.User

class RVAdapter(var list: MutableList<User>, var listener: OnItemTouchClickListener) :
    RecyclerView.Adapter<RVAdapter.VH>() {

    private val viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    interface OnItemTouchClickListener {
        fun onCall(user: User, position: Int)
        fun onSms(user: User, position: Int)
    }

    inner class VH(var binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(user: User, position: Int) {
            binding.name.text = user.name
            binding.number.text = user.number

            binding.call.setOnClickListener {
                listener.onCall(user = user, position = position)
            }

            binding.sms.setOnClickListener {
                listener.onSms(user = user, position = position)
            }
            viewBinderHelper.setOpenOnlyOne(true)
            viewBinderHelper.bind(binding.swipeLayout, user.toString())
            viewBinderHelper.closeLayout(user.name)
            viewBinderHelper.closeLayout(user.number)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = list[position]
        holder.onBind(user, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}