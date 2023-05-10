package ayds.newyork.songinfo.moredetails.fulllogic.injector

import android.content.Context
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.ArtistInfoRepositoryImpl
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.ArtistInfoExternalStorageImpl
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.info.JsonToArtistInfoResolver
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.external.info.NYTArtistInfoServiceImpl
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.local.sqldb.ArtistInfoLocalStorageImpl
import ayds.newyork.songinfo.moredetails.fulllogic.model.data.local.sqldb.CursorToArtistInfoMapperImpl
import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInfoRepository
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.presenter.Presenter
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.presenter.PresenterImpl
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.view.OtherInfoView


object DependenciesInjector {

    private lateinit var presenter: Presenter

    fun init(otherInfoView: OtherInfoView) {

        val artistInfoLocalStorage = ArtistInfoLocalStorageImpl(
            otherInfoView as Context,
            cursorToArtistInfoMapper = CursorToArtistInfoMapperImpl()
        )
        val nyToArtisInfoResolver = JsonToArtistInfoResolver()
        val nyToArtistInfoService = NYTArtistInfoServiceImpl(nyToArtisInfoResolver)
        val artistInfoExternalStorage = ArtistInfoExternalStorageImpl(nyToArtistInfoService)
        val artistInfoRepository: ArtistInfoRepository =
            ArtistInfoRepositoryImpl(artistInfoLocalStorage, artistInfoExternalStorage)
        presenter = PresenterImpl(artistInfoRepository)

    }

    fun getPresenter(): Presenter = presenter
}

