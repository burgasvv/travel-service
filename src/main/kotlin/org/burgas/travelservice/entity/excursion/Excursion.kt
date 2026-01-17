package org.burgas.travelservice.entity.excursion

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.NamedSubgraph
import org.burgas.travelservice.entity.Model
import org.burgas.travelservice.entity.sight.Sight
import java.util.UUID

@Entity
@NamedEntityGraph(
    name = "excursion-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "sights", subgraph = "sights-subgraph")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "sights-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "city", subgraph = "city-subgraph")
            ]
        ),
        NamedSubgraph(
            name = "city-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "country")
            ]
        )
    ]
)
class Excursion : Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    override lateinit var id: UUID

    @Column(name = "name", nullable = false, unique = true)
    lateinit var name: String

    @Column(name = "description", nullable = false, unique = true)
    lateinit var description: String

    @Column(name = "price", nullable = false)
    var price: Double = 0.0

    @ManyToMany(mappedBy = "excursions", fetch = FetchType.LAZY)
    var sights: MutableList<Sight> = mutableListOf()

    constructor()
}