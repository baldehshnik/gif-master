package com.vd.study.core.presentation.viewbinding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KProperty

inline fun <reified VB : ViewBinding> Fragment.viewBinding(): FragmentViewBindingDelegate<VB> {
    return FragmentViewBindingDelegate(this, VB::class.java)
}

class FragmentViewBindingDelegate<VB : ViewBinding>(
    private val fragment: Fragment,
    private val viewModelClass: Class<VB>
) {

    private var binding: VB? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): VB {
        val owner = fragment.viewLifecycleOwner
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            throw IllegalArgumentException("Fragment was destroyed")
        } else if (fragment.view != null) {
            return createViewBinding(owner)
        } else {
            throw IllegalArgumentException("Fragment was not created yet")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun createViewBinding(owner: LifecycleOwner): VB {
        return this.binding ?: let {
            val bindMethod = viewModelClass.getMethod("bind", View::class.java)
            val localBinding = bindMethod.invoke(null, fragment.view) as VB

            owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    this@FragmentViewBindingDelegate.binding = null
                }
            })

            this.binding = localBinding
            localBinding
        }
    }
}