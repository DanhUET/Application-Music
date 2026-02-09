package pro.branium.core_navigation.home

import androidx.fragment.app.Fragment

interface AlbumDetailNavigator {
    fun openAlbumDetail(from: Fragment, albumId: Int)
}