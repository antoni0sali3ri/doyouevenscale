package com.github.antoni0sali3ri.doyouevenscale.core.model

interface ListableEntity {

    var id : Long

    var name : String
}

interface Copyable<T> {
    fun copy(): T
}