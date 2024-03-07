package org.rambots.config

object LookUpTable {
    private val lookUpTable = arrayOf(
        Table(20.0, 20.0, 10.0, 1.5),
        Table(4.2,5.2,5.7,24.5)
    )
    fun getTable(distance: Int): Table {
        return lookUpTable[distance]
    }
}