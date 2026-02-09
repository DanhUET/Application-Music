package pro.branium.feature.search.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_ui.dialog.ConfirmationDialogFragment
import pro.branium.feature.search.domain.model.HistorySearchedKeyModel
import pro.branium.feature.search.viewmodel.SearchingViewModel
import pro.branium.search.R
import pro.branium.search.databinding.FragmentHistorySearchedKeyBinding

@AndroidEntryPoint
class HistorySearchedKeyFragment : Fragment() {
    private lateinit var binding: FragmentHistorySearchedKeyBinding

    private val viewModel: SearchingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistorySearchedKeyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        binding.tvTitleHistorySearchedKey.setOnClickListener { clearSearchViewFocus() }
        binding.tvClearHistorySearchedKey.setOnClickListener {
            clearSearchViewFocus()
            if (viewModel.historySearchedKeysEntity.value?.isNotEmpty() == true) {
                val messageId = R.string.message_confirm_clear_history
                val dialog = ConfirmationDialogFragment(
                    messageId = messageId,
                    listener = object : ConfirmationDialogFragment.OnDeleteConfirmListener {
                        override fun onConfirm(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                viewModel.clearAllKeys()
                            }
                        }
                    })
                dialog.show(
                    requireActivity().supportFragmentManager,
                    ConfirmationDialogFragment.TAG
                )
            } else {
                val snackBar = Snackbar.make(
                    requireView(),
                    R.string.message_no_history_searched_keys,
                    Snackbar.LENGTH_SHORT
                )
                snackBar.anchorView = binding.root
//                    requireActivity().findViewById(R.id.mini_player)
                snackBar.show()
            }
        }
    }

    private fun clearSearchViewFocus() {
        val searchView = requireActivity().findViewById<SearchView>(R.id.search_view_home)
        searchView.clearFocus()
    }

    private fun setupObserver() {
        viewModel.historySearchedKeysEntity.observe(viewLifecycleOwner) { keys ->
            populateHistorySearchedKeys(keys)
            if (keys.isEmpty()) {
                binding.searchKeyEmptyLayout.visibility = View.VISIBLE
                binding.searchKeyContentLayout.visibility = View.GONE
                binding.includeEmptyContent.tvEmptyMessage.text =
                    getString(R.string.message_no_history_searched_keys)
                binding.includeEmptyContent
                    .ivEmptyContent.setImageResource(R.drawable.ic_search)
            } else {
                binding.searchKeyEmptyLayout.visibility = View.GONE
                binding.searchKeyContentLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun populateHistorySearchedKeys(keys: List<HistorySearchedKeyModel>) {
        val chipGroup = binding.chipGroupHistorySearchedKey
        chipGroup.removeAllViews()
        keys.forEach { key ->
            val chip = Chip(requireContext()).apply {
                text = key.key
                isCloseIconVisible = true
                chipBackgroundColor = ColorStateList.valueOf(
                    MaterialColors.getColor(
                        this,
                        com.google.android.material.R.attr.colorSurface
                    )
                )
                setTextColor(
                    MaterialColors.getColor(
                        this,
                        com.google.android.material.R.attr.colorOnSurface
                    )
                )
                setOnClickListener {
                    viewModel.setSelectedKey(key.key)
                }
                setOnCloseIconClickListener {
                    removeKeywordFromHistory(key.key)
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun removeKeywordFromHistory(key: String) {
        val messageId = R.string.msg_confirm_delete_search_key
        val dialog = ConfirmationDialogFragment(
            messageId = messageId,
            listener = object : ConfirmationDialogFragment.OnDeleteConfirmListener {
                override fun onConfirm(isConfirmed: Boolean) {
                    if (isConfirmed) {
                        viewModel.removeSearchKey(key)
                    }
                }
            })
        dialog.show(
            requireActivity().supportFragmentManager,
            ConfirmationDialogFragment.TAG
        )
    }
}
