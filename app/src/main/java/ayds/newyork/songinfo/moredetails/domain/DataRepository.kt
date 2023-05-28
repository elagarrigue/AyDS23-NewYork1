package ayds.newyork.songinfo.moredetails.domain

interface DataRepository {
    fun getDataByTerm(term: String): Card?
}

