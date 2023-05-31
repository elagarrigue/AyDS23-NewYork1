package ayds.newyork.songinfo.moredetails.injector

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.DataRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.local.sqldb.CursorToArtistInfoMapperImpl
import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.view.FormatterInfo
import ayds.newyork.songinfo.moredetails.presentation.view.OtherInfoViewActivity
import ayds.newyork.songinfo.moredetails.data.BrokerServiceImpl
import ayds.newyork.songinfo.moredetails.data.Proxy
import ayds.newyork.songinfo.moredetails.data.proxy.NYTProxyImpl
import ayds.newyork.songinfo.moredetails.data.proxy.WikipediaProxyImpl
import ayds.newyork.songinfo.moredetails.data.proxy.LastFMProxyImpl

object MoreDetailsDependenciesInjector {
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private const val NYTIMES_URL = "https://api.nytimes.com/svc/search/v2/"
    val otherInfo: OtherInfoViewActivity = OtherInfoViewActivity(formatterInfo = FormatterInfo(""))
    private lateinit var broker: BrokerServiceImpl
    private var proxys: MutableList<Proxy> = mutableListOf()
    fun init(otherInfoView: OtherInfoViewActivity) {
        val artistInfoLocalStorage = DataLocalStorageImpl(
            otherInfoView as Context,
            cursorToArtistInfoMapper = CursorToArtistInfoMapperImpl()
        )
        proxys.add(NYTProxyImpl())
        proxys.add(LastFMProxyImpl())
        proxys.add(WikipediaProxyImpl())
        broker = BrokerServiceImpl(proxys)
        val dataRepository: DataRepository = DataRepositoryImpl(artistInfoLocalStorage, broker)
        moreDetailsPresenter = MoreDetailsPresenterImpl(dataRepository)
    }

    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter
}