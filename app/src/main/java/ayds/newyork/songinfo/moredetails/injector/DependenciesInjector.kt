package ayds.newyork.songinfo.moredetails.injector

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.DataRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.external.ArtistInfoExternalStorageImpl
import ayds.newyork.songinfo.moredetails.data.external.info.JsonToArtistInfoResolver
import ayds.newyork.songinfo.moredetails.data.external.info.NYTArtistInfoServiceImpl
import ayds.newyork.songinfo.moredetails.data.external.info.NYTimesAPI
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.local.sqldb.CursorToArtistInfoMapperImpl
import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.view.FormatterInfo
import ayds.newyork.songinfo.moredetails.presentation.view.OtherInfoViewActivity
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object DependenciesInjector {
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private const val NYTIMES_URL = "https://api.nytimes.com/svc/search/v2/"
    val otherInfo: OtherInfoViewActivity = OtherInfoViewActivity(formatterInfo = FormatterInfo(""))

    fun init(otherInfoView: OtherInfoViewActivity) {
        val artistInfoLocalStorage = DataLocalStorageImpl(
            otherInfoView as Context,
            cursorToArtistInfoMapper = CursorToArtistInfoMapperImpl()
        )
        val nyToArtisInfoResolver = JsonToArtistInfoResolver()
        val newYorkTimesRetrofit = Retrofit.Builder()
            .baseUrl(NYTIMES_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val newYorkTimesAPI: NYTimesAPI = newYorkTimesRetrofit.create(NYTimesAPI::class.java)
        val nyToArtistInfoService = NYTArtistInfoServiceImpl(nyToArtisInfoResolver, newYorkTimesAPI)
        val artistInfoExternalStorage = ArtistInfoExternalStorageImpl(nyToArtistInfoService)
        val dataRepository: DataRepository =
            DataRepositoryImpl(artistInfoLocalStorage, artistInfoExternalStorage)
        moreDetailsPresenter = MoreDetailsPresenterImpl(dataRepository)
    }

    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter
}