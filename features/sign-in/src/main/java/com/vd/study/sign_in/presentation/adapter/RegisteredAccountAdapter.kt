package com.vd.study.sign_in.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vd.study.core.R as CoreResources
import com.vd.study.sign_in.databinding.RegisteredAccountItemBinding
import com.vd.study.sign_in.domain.entities.AccountEntity

class RegisteredAccountAdapter(
    private val items: List<AccountEntity>,
    private val onRegisteredAccountClickListener: OnRegisteredAccountClickListener,
    private val isLightTheme: Boolean
) : ListAdapter<AccountEntity, RegisteredAccountAdapter.RegisteredAccountViewHolder>(
    RegisteredAccountDiffUtil()
) {

    class RegisteredAccountViewHolder(
        private val binding: RegisteredAccountItemBinding,
        private val onRegisteredAccountClickListener: OnRegisteredAccountClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(account: AccountEntity, isDarkTheme: Boolean) = with(binding) {
            if (isDarkTheme) textAccountUsername.setTextColor(Color.WHITE)
            textAccountUsername.text = account.username

            Glide.with(binding.imageRegisteredAccount.context)
                .load(account.avatarUrl)
                .placeholder(CoreResources.drawable.placeholder_gray_gradient)
                .into(binding.imageRegisteredAccount)

            root.setOnClickListener { onRegisteredAccountClickListener.onClick(account) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisteredAccountViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RegisteredAccountItemBinding.inflate(inflater, parent, false)
        return RegisteredAccountViewHolder(binding, onRegisteredAccountClickListener)
    }

    override fun onBindViewHolder(holder: RegisteredAccountViewHolder, position: Int) {
        holder.bind(items[position], !isLightTheme)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class RegisteredAccountDiffUtil : DiffUtil.ItemCallback<AccountEntity>() {

        override fun areItemsTheSame(oldItem: AccountEntity, newItem: AccountEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AccountEntity, newItem: AccountEntity): Boolean {
            return oldItem == newItem
        }
    }
}