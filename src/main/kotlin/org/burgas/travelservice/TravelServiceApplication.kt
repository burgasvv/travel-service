package org.burgas.travelservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class TravelServiceApplication

fun main(args: Array<String>) {
    runApplication<TravelServiceApplication>(*args)
}
