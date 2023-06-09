package ayds.newyork.songinfo.moredetails.injector

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.DataRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.local.sqldb.DataLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.local.sqldb.CursorToArtistInfoMapperImpl
import ayds.newyork.songinfo.moredetails.domain.DataRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.view.OtherInfoViewActivity
import ayds.newyork.songinfo.moredetails.data.broker.BrokerServiceImpl
import ayds.newyork.songinfo.moredetails.data.broker.proxy.Proxy
import ayds.newyork.songinfo.moredetails.data.broker.proxy.NYTProxyImpl
import ayds.newyork.songinfo.moredetails.data.broker.proxy.WikipediaProxyImpl
import ayds.newyork.songinfo.moredetails.data.broker.proxy.LastFMProxyImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.FormatterInfo
import lisboa4LastFM.LastFMInjector
import wikipedia.external.external.WikipediaInjector
import ayds.ny1.newyorktimes.DependenciesInjector

object MoreDetailsDependenciesInjector {

    val otherInfo: OtherInfoViewActivity = OtherInfoViewActivity()
    private lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var broker: BrokerServiceImpl

    fun init(otherInfoView: OtherInfoViewActivity) {
        val artistInfoLocalStorage = DataLocalStorageImpl(
            otherInfoView as Context,
            cursorToArtistInfoMapper = CursorToArtistInfoMapperImpl()
        )
        var proxys: MutableList<Proxy> = mutableListOf()
        proxys.add(NYTProxyImpl(DependenciesInjector.init()))
        proxys.add(LastFMProxyImpl(LastFMInjector.getLastFmService()))
        proxys.add(WikipediaProxyImpl(WikipediaInjector.generateWikipediaService()))
        broker = BrokerServiceImpl(proxys)
        val dataRepository: DataRepository = DataRepositoryImpl(artistInfoLocalStorage, broker)
        val formatterInfo = FormatterInfo("")
        moreDetailsPresenter = MoreDetailsPresenterImpl(dataRepository, formatterInfo)
    }

    fun getPresenter(): MoreDetailsPresenter = moreDetailsPresenter
}