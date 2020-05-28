package dev.reeve.animalcrossing.debug

class TreeLog {
    private var findTreesLog = HashMap<Int, ArrayList<Double>>()
    private var averages = HashMap<Int, Double>()

    fun addEvent(version: Int, time: Double) {
        if (findTreesLog.containsKey(version)) {
            findTreesLog[version]!!.add(time)
        } else {
            findTreesLog[version] = arrayListOf(time)
        }
        calculateAverages()
    }

    private fun calculateAverages() {
        for (pair in findTreesLog.entries) {
            averages[pair.key] = pair.value.average()
        }
    }
}