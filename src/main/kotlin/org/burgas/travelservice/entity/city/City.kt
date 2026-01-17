package org.burgas.travelservice.entity.city

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.OneToMany
import org.burgas.travelservice.entity.Model
import org.burgas.travelservice.entity.country.Country
import org.burgas.travelservice.entity.sight.Sight
import java.util.UUID

@Entity
@NamedEntityGraph(
    name = "city-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "country"),
        NamedAttributeNode(value = "sights")
    ]
)
class City : Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    override lateinit var id: UUID

    @Column(name = "name", nullable = false, unique = true)
    lateinit var name: String

    @Column(name = "description", nullable = false, unique = true)
    lateinit var description: String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    lateinit var country: Country

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    var sights: MutableList<Sight> = mutableListOf()

    constructor()
}