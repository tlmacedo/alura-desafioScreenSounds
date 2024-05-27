package br.com.tlmacedo.alura.desafio.dados

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence

object Banco {

    private const val PU = "musicas"

    fun getEntityManager(): EntityManager {
        val entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory(PU)
        return entityManagerFactory.createEntityManager()
    }

}
