package pro.branium.feature.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_navigation.navigator.SearchNavigator
import pro.branium.library.databinding.FragmentLibraryBinding
import pro.branium.presentation_common.viewmodel.VoiceSearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private val voiceSearchViewModel: VoiceSearchViewModel by activityViewModels()

    @Inject
    lateinit var searchNavigator: SearchNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val scrollPosition = savedInstanceState.getInt(SCROLL_POSITION)
            binding.scrollViewLibrary.post {
                binding.scrollViewLibrary.scrollTo(0, scrollPosition)
            }
        }
        binding.btnSearchLibrary.setOnClickListener {
            searchNavigator.openSearch(null)
        }
        binding.btnSpeechToSearch.setOnClickListener {
            voiceSearchViewModel.triggerVoiceSearch(true)
        }
        voiceSearchViewModel.voiceSearchQuery.observe(viewLifecycleOwner) { query ->
            if (query.isNotEmpty()) {
                searchNavigator.openSearch(query)
                voiceSearchViewModel.setVoiceSearchQuery("")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::binding.isInitialized) {
            outState.putInt(SCROLL_POSITION, binding.scrollViewLibrary.scrollY)
        }
    }

    companion object {
        const val SCROLL_POSITION = "pro.branium.musicapp.ui.library.SCROLL_POSITION"
    }
}