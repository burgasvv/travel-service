package org.burgas.travelservice.service

import org.burgas.travelservice.dto.Request
import org.burgas.travelservice.dto.Response
import org.burgas.travelservice.entity.Model
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Service
interface AsyncCrudService<in R : Request, M : Model, S : Response, F : Response> {

    fun findEntity(id: UUID): CompletableFuture<M>

    fun findById(id: UUID): CompletableFuture<F>

    fun findAll(): CompletableFuture<List<S>>

    fun create(request: R): CompletableFuture<Void>

    fun update(request: R): CompletableFuture<Void>

    fun delete(id: UUID): CompletableFuture<Void>
}