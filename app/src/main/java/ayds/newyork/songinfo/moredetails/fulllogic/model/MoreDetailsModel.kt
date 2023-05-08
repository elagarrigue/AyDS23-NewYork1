package ayds.newyork.songinfo.moredetails.fulllogic.model

import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.repository.SongRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface MoreDetailsModel {

       /* val songObservable: Observable<Song>

        fun searchSong(term: String)

        fun getSongById(id: String): Song*/
    }

internal class MoreDetailsModelImpl() : MoreDetailsModel {

      /*  override val songObservable = Subject<Song>()

        override fun searchSong(term: String) {
            repository.getSongByTerm(term).let {
                songObservable.notify(it)
            }
        }

        override fun getSongById(id: String): Song = repository.getSongById(id)
    }/*
}