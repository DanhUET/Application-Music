package pro.branium.core_navigation.discovery

import androidx.fragment.app.Fragment

interface ArtistDetailNavigator {
    fun openArtistDetail(from: Fragment, artistId: Int)
}