package ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.info

import ayds.newyork.songinfo.moredetails.fulllogic.model.ArtistInformation
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.NYTArtistInfoService
import retrofit2.Response

internal class NYTArtistInfoServiceImpl (
    private val NYtimesAPI : NYTimesAPI,
    private val NYTtoArtistInfo : NYTtoArtistInfoResolver
    ) : NYTArtistInfoService {

    override fun getArtistInfo(name: String): ArtistInformation? {
        val callResponse = getArtistInfoFromService(name)
        return NYTtoArtistInfo.getArtistInfoFromExternalData(callResponse.body())
    }

    private fun getArtistInfoFromService(artistName: String): Response<String> {
        return NYtimesAPI.getArtistInfo(artistName)
    }
}
