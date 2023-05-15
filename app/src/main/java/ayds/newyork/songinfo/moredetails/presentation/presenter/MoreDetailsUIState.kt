package ayds.newyork.songinfo.moredetails.presentation.presenter

data class MoreDetailsUIState(
    val artistName: String = "",
    val artistNameExtra: String = "artistName",
    val imageURL: String ="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU",
    val url: String? = "",
    val abstract: String? = "",
    val isLocallyStored: Boolean = false
)
