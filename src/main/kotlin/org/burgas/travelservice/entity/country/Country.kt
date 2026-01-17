package org.burgas.travelservice.entity.country

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.NamedSubgraph
import jakarta.persistence.OneToMany
import org.burgas.travelservice.entity.Model
import org.burgas.travelservice.entity.city.City
import java.util.UUID

@Entity
@NamedEntityGraph(
    name = "country-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "cities")
    ]
)
class Country : Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    override lateinit var id: UUID

    @Column(name = "name", nullable = false, unique = true)
    lateinit var name: String

    @Column(name = "description", nullable = false, unique = true)
    lateinit var description: String

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    var cities: MutableList<City> = mutableListOf()

    constructor()
}