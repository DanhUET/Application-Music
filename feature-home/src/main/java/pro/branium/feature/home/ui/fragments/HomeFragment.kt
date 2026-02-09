package pro.branium.feature.home.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_navigation.navigator.SearchNavigator
import pro.branium.home.databinding.FragmentHomeBinding
import pro.branium.presentation_common.viewmodel.VoiceSearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class
HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val voiceSearchViewModel: VoiceSearchViewModel by activityViewModels()

    @Inject
    lateinit var searchNavigator: SearchNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val scrollPosition = savedInstanceState.getInt(SCROLL_POSITION)
            binding.scrollViewHome.post {
                binding.scrollViewHome.scrollTo(0, scrollPosition)
            }
        }
        binding.btnSearchHome.setOnClickListener {
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
        if (this::binding.isInitialized) {
            val scrollPosition = binding.scrollViewHome.scrollY
            outState.putInt(SCROLL_POSITION, scrollPosition)
        }
    }

    companion object {
        const val SCROLL_POSITION = "pro.branium.musicapp.ui.home.SCROLL_POSITION"
    }
}
// lệnh build module: ./gradlew :feature-home:assembleDebug