package de.theopensourceguy.doyouevenscale.core.model

class Instrument(val numStrings: Int,
                 val numFrets: Int,
                 val tuning: Tuning
) {
    companion object {
        val MaxFrets = 30 // Any more than that and it's gonna look messy af
    }

    fun getFretsForScale(scale: Scale) : List<List<Int>> = (1..numStrings).map {
        getFretsForScale(it, scale)
    }

    fun getFretsForScale(stringNo: Int, scale: Scale) : List<Int> {
        val openString = tuning.pitchOf(stringNo)
        return (0..numFrets).filter {
            scale.contains(openString.shift(it))
        }
    }

    fun getRoots(stringNo: Int, root: Note) : List<Int> {
        val openString = tuning.pitchOf(stringNo)
        return (0..numFrets).filter {
            openString.shift(it) == root
        }
    }

    fun getRoots(stringNo: Int, scale: Scale) : List<Int> = getRoots(stringNo, scale.root)

    fun getRoots(root: Note) : List<List<Int>> = (1..numStrings).map { getRoots(it, root) }

    fun getRoots(scale: Scale) : List<List<Int>> = getRoots(scale.root)
}

