package org.burgas.travelservice.entity.identity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.burgas.travelservice.entity.Model
import java.util.UUID

@Entity
class Identity : Model {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    override lateinit var id: UUID

    @Enumerated(value = EnumType.STRING)
    @Column(name = "authority", nullable = false)
    lateinit var authority: Authority

    @Column(name = "username", nullable = false, unique = true)
    lateinit var username: String

    @Column(name = "password", nullable = false)
    lateinit var password: String

    @Column(name = "email", nullable = false, unique = true)
    lateinit var email: String

    @Column(name = "enabled", nullable = false)
    var enabled: Boolean = true

    @Column(name = "firstname", nullable = false)
    lateinit var firstname: String

    @Column(name = "lastname", nullable = false)
    lateinit var lastname: String

    @Column(name = "patronymic", nullable = false)
    lateinit var patronymic: String

    constructor()
}