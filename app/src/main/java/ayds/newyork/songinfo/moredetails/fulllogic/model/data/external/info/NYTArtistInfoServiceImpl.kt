package ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.info

import ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.NYTArtistInfoService
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInformation
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val NYTIMES_URL = "https://api.nytimes.com/svc/search/v2/"

class NYTArtistInfoServiceImpl(
    private val NYTtoArtistInfo: NYTtoArtistInfoResolver
) : NYTArtistInfoService {

    private val newYorkTimesRetrofit = Retrofit.Builder()
        .baseUrl(NYTIMES_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val newYorkTimesAPI: NYTimesAPI = newYorkTimesRetrofit.create(NYTimesAPI::class.java)
    override fun getArtistInfo(name: String): ArtistInformation? {
        val callResponse = getArtistInfoFromService(name)
        return NYTtoArtistInfo.getArtistInfoFromExternalData(name, callResponse.body())
    }

    private fun getArtistInfoFromService(artistName: String): Response<String> {
        return newYorkTimesAPI.getArtistInfo(artistName)
    }
}
