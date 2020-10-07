package de.theopensourceguy.doyouevenscale.core.model

interface ListableEntity {

    var id : Long

    var name : String
}

interface Copyable<T> {
    fun copy(): T
}