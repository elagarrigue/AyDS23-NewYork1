package ayds.newyork.songinfo.moredetails.data.external.info

import ayds.newyork.songinfo.moredetails.data.external.NYTArtistInfoService
import ayds.newyork.songinfo.moredetails.domain.ArtistInformation
import retrofit2.Response

class NYTArtistInfoServiceImpl(
    private val NYTtoArtistInfo: NYTtoArtistInfoResolver,
    private val newYorkTimesAPI : NYTimesAPI
) : NYTArtistInfoService {

    override fun getArtistInfo(name: String): ArtistInformation? {
        val callResponse = getArtistInfoFromService(name)
        return NYTtoArtistInfo.getArtistInfoFromExternalData(name, callResponse.body())
    }

    private fun getArtistInfoFromService(artistName: String): Response<String> {
        return newYorkTimesAPI.getArtistInfo(artistName)
    }
}
