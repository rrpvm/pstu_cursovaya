package com.rrpvm.kinofeed.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rrpvm.kinofeed.databinding.BottomDialogFragmentGenresFilterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenresFilterModalBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomDialogFragmentGenresFilterBinding? = null
    private val binding get() = checkNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return BottomDialogFragmentGenresFilterBinding.inflate(inflater, container, false).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}