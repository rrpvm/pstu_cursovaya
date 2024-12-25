package com.rrpvm.authorization.presentation.signin

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.rrpvm.authorization.R
import com.rrpvm.authorization.databinding.FragmentSigninScreenBinding
import com.rrpvm.core.setSafeText
import com.rrpvm.core.showProgress
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
        setupUiEventListeners()
        observeLoadingState()
        observeDataScreen()
        viewModel.let {
            it.toString()
        }
    }

    private fun setupUiEventListeners() {
        binding.apply {
            btnDoSignIn.setOnClickListener {
                viewModel.onSignInClicked()
            }
            tilPasswordAuth.setEndIconOnClickListener {
                viewModel.onPasswordVisibilityToggle()
            }
            edUserPassword.doOnTextChanged { text, start, before, count ->
                viewModel.onPasswordInput(text.toString())
            }
            edUserLogin.doOnTextChanged { text, start, before, count ->
                viewModel.onUsernameInput(text.toString())
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

    private fun observeDataScreen() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dataScreen.collectLatest { screen ->
                    onPasswordProcess(screen.passwordData())
                    onLoginProcess(screen.loginData())
                }
            }
        }
    }

    private fun onPasswordProcess(data: PasswordData) {
        binding.tilPasswordAuth.apply {
            setEndIconDrawable(data.mPasswordIcon)
            editText?.let { ed ->
                ed.setSafeText(data.mPassword)
                val newInputType = if (data.mPasswordVisible) {
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                }
                ed.inputType = newInputType
            }
            error = data.errorText
        }
    }

    private fun onLoginProcess(data: LoginData) {
        binding.tilUsername.apply {
            editText?.let { ed ->
                ed.setSafeText(data.mUsername)
            }
            error = data.errorText
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}