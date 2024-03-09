package org.rambots.config

object LookUpTable {
    private val lookUpTable = arrayOf(
        Table(4000.0, 5000.0, 10.0, 1.5),
        Table(3000.0,4000.0,5.7,24.5)
    )
    fun getTable(distance: Int): Table {
        return lookUpTable[distance]
    }
}