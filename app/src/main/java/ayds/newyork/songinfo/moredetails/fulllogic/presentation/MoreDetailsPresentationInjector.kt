package ayds.newyork.songinfo.moredetails.fulllogic.presentation


import ayds.newyork.songinfo.moredetails.fulllogic.model.MoreDetailsModel
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.presenter.Presenter
import ayds.newyork.songinfo.moredetails.fulllogic.presentation.view.OtherInfoView


object MoreDetailsPresentationInjector {

    private lateinit var moreDetailsModel: MoreDetailsModel
    private lateinit var presenter: Presenter

    fun getMoreDetailsModel(): MoreDetailsModel = moreDetailsModel

    fun getPresenter(): Presenter = presenter

    fun initView(otherInfoView: OtherInfoView) {
        //MoreDetailsModelInjector.initMoreDetailsModel(otherInfoView)
    }
}