package com.rrpvm.profile.presentation.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rrpvm.core.toPx
import com.rrpvm.profile.R
import com.rrpvm.profile.databinding.FragmentProfileLayoutBinding
import com.rrpvm.profile.presentation.menu.model.ProfileMenuItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileLayoutBinding? = null
    private lateinit var adapter: ProfileMenuAdapter
    private val binding get() = checkNotNull(_binding)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ProfileMenuAdapter(
            onLogoutClicked = viewModel::onLogoutClicked,
            onMenuOptionClicked = viewModel::onMenuOptionClicked
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentProfileLayoutBinding.inflate(inflater, container, false).let {
            _binding = it
            return@let it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            rvProfile.adapter = adapter
            rvProfile.addItemDecoration(
                ProfileMenuDecorator(
                    _strokeWidth = resources.toPx(1.15F),
                    _strokeColor = resources.getColor(
                        com.rrpvm.core.R.color.surface,
                        context?.theme
                    ),
                    footerMargin = resources.toPx(12).toInt()
                )
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.menuState.collectLatest { newData ->
                adapter.setNewData(newData.mItems)
            }
        }

    }

    override fun onDestroyView() {
        binding.rvProfile.adapter = null
        _binding = null
        super.onDestroyView()
    }
}