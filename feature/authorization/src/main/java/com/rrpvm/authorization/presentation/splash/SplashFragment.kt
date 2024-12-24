package com.rrpvm.authorization.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rrpvm.authorization.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val viewModel: SplashScreenViewModel by viewModels<SplashScreenViewModel>()
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = checkNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSplashScreenBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEffects.collectLatest { effect: SplashScreenViewEffect ->
                    when (effect) {
                        SplashScreenViewEffect.SignInClicked -> {
                            findNavController().navigate(SplashFragmentDirections.actionFragmentSplashToFragmentSignin())
                        }
                    }
                }
            }
        }


        binding.btnSignIn.setOnClickListener {
            viewModel.onSignInButtonClicked()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}