package com.rrpvm.authorization.presentation.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import com.rrpvm.authorization.R
import com.rrpvm.authorization.databinding.FragmentSigninScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private val viewModel: SignInViewModel by viewModels()
    private var _binding: FragmentSigninScreenBinding? = null
    private val binding get() = checkNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSigninScreenBinding.inflate(inflater, container, false).let {
            _binding = it
            return@let binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var visible = false

        binding.tilPasswordAuth.setEndIconDrawable(if (visible) R.drawable.ic_visible else R.drawable.ic_not_visible)
        binding.tilPasswordAuth.setEndIconOnClickListener {
            visible = !visible
            binding.tilPasswordAuth.setEndIconDrawable(if (visible) R.drawable.ic_visible else R.drawable.ic_not_visible)
        }

        setupClickListeners()
        observeLoadingState()
        viewModel.let {
            it.toString()
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnDoSignIn.setOnClickListener {
                viewModel.onSignInClicked()
            }
        }
    }

    private fun observeLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoadingState.collectLatest { isLoading ->
                    if (isLoading) {
                        binding.btnDoSignIn.showProgress()
                    } else {
                        binding.btnDoSignIn.icon = null
                    }
                }
            }
        }
    }

    fun MaterialButton.showProgress(@ColorInt tintColor: Int = this.iconTint.defaultColor) {
        val spec = CircularProgressIndicatorSpec(
            context, null, 0,
            com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_Small
        )

        spec.indicatorColors = intArrayOf(tintColor)

        val progressIndicatorDrawable =
            IndeterminateDrawable.createCircularDrawable(context, spec)

        this.icon = progressIndicatorDrawable
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}