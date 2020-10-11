package com.github.antoni0sali3ri.doyouevenscale.core.model

/**
 * Interface to specify shared attributes between database entities/model classes.
 */
interface ListableEntity {

    /**
     * Row id for Room database.
     */
    var id: Long

    /**
     * The name (for display) of the entity.
     */
    var name: String
}
