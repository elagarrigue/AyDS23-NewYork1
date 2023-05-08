package ayds.newyork.songinfo.moredetails.fulllogic.presentation

import ayds.newyork.songinfo.moredetails.fulllogic.model.domain.ArtistInfoRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsPresentation {

     val viewObservable: Observable<MoreDetailsUIEvent>

     fun searchSong(term: String)

}

internal class MoreDetailsPresentationImpl(private val repository: ArtistInfoRepository) : MoreDetailsPresentation {

        override val viewObservable = Subject<MoreDetailsUIEvent>()

        override fun searchSong(term: String) {
            repository.getArtistInfoByTerm(term).let {
                viewObservable.notify(it)
            }
        }
    }
}