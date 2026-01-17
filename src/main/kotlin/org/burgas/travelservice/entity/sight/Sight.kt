package org.burgas.travelservice.entity.sight

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.NamedSubgraph
import org.burgas.travelservice.entity.Model
import org.burgas.travelservice.entity.city.City
import org.burgas.travelservice.entity.excursion.Excursion
import java.util.UUID

@Entity
@NamedEntityGraph(
    name = "sight-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "city", subgraph = "city-subgraph"),
        NamedAttributeNode(value = "excursions")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "city-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "country")
            ]
        )
    ]
)
class Sight : Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    override lateinit var id: UUID

    @Column(name = "name", nullable = false, unique = true)
    lateinit var name: String

    @Column(name = "description", nullable = false, unique = true)
    lateinit var description: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    lateinit var city: City

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sight_excursion",
        joinColumns = [JoinColumn(name = "sight_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "excursion_id", referencedColumnName = "id")]
    )
    var excursions: MutableList<Excursion> = mutableListOf()

    fun addExcursion(excursion: Excursion) {
        this.excursions.add(excursion)
        excursion.sights.add(this)
    }

    fun removeExcursion(excursion: Excursion) {
        this.excursions.removeIf { findExcursion -> findExcursion.id == excursion.id }
        excursion.sights.removeIf { sight -> sight.id == this.id }
    }

    constructor()
}