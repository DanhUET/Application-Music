package pro.branium.feature_song.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import pro.branium.core_resources.R
import pro.branium.core_model.SongModel
import pro.branium.feature_song.databinding.FragmentDialogSongInfoBinding
import pro.branium.feature_song.viewmodel.DialogSongInfoViewModel

@AndroidEntryPoint
class DialogSongInfoFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDialogSongInfoBinding
    private val viewModel: DialogSongInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogSongInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractBundleData()
        setupObserver()
    }

    private fun extractBundleData() {
        val songId = arguments?.getString(ARG_SONG_ID) ?: return
        viewModel.getSongInfo(songId)
    }

    private fun setupObserver() {
        viewModel.songModel.observe(viewLifecycleOwner) { song ->
            song?.let {
                showSongInfo(it)
            }
        }
    }

    private fun showSongInfo(songModel: SongModel) {
        val title = getString(R.string.text_song_detail_title, songModel.title)
        val artist = getString(R.string.text_song_detail_artist, songModel.artist)
        val album = getString(R.string.text_song_detail_album, songModel.album)
        val duration = getString(
            R.string.text_song_detail_duration,
            viewModel.toTimeLabel(songModel.durationSeconds)
        )
        val counter = getString(R.string.text_song_detail_counter, songModel.counter)
        val favorite = getString(
            R.string.text_song_detail_favorite_status,
            /*if (songModel.favorite) getString(R.string.yes) else*/ getString(R.string.no)
        )
        val notAvailable = getString(R.string.not_available)
        val genre = getString(R.string.text_song_detail_genre, notAvailable)
        val year = getString(R.string.text_song_detail_year, notAvailable)

        Glide.with(this)
            .load(songModel.artworkUrl)
            .error(R.drawable.ic_album)
            .circleCrop()
            .into(binding.imageDetailSongArtwork)
        binding.textSongDetailTitle.text = title
        binding.textSongDetailArtist.text = artist
        binding.textSongDetailAlbum.text = album
        binding.textSongDetailDuration.text = duration
        binding.textSongDetailCounter.text = counter
        binding.textSongDetailFavoriteStatus.text = favorite
        binding.textSongDetailGenre.text = genre
        binding.textSongDetailYear.text = year
    }

    companion object {
        private const val ARG_SONG_ID = "arg_song_id"

        fun newInstance(songId: String): DialogSongInfoFragment {
            return DialogSongInfoFragment().apply {
                arguments = bundleOf(ARG_SONG_ID to songId)
            }
        }

        const val TAG = "DialogSongInfoFragment"
    }
}