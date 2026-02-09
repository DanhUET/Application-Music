package pro.branium.feature.search.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pro.branium.search.databinding.FragmentHistorySearchBinding

class SearchHistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistorySearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistorySearchBinding.inflate(inflater, container, false)
        return binding.root
    }
}
