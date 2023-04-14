package ayds.newyork.songinfo.home.view


class FormatterDate {

    private lateinit var strategy: PrecisionFormatStrategy
    fun setStrategy(strategyToUse: PrecisionFormatStrategy) {
       this.strategy =  strategyToUse
    }
    fun executeStrategy(date: String) : String {
        return strategy.format(date)
    }
 }