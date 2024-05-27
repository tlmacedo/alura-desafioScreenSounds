package br.com.tlmacedo.alura.desafio.dados

import jakarta.persistence.EntityManager

abstract class DAO<TModel, TEntity>(
    val manager: EntityManager,
    val entityType: Class<TEntity>
) {

    abstract fun toModel(entity: TEntity): TModel
    abstract fun toEntity(model: TModel): TEntity

    open fun getLista(): List<TModel> {
        val query = manager.createQuery("from $entityType.simpleName", entityType).resultList
        return query.map { toModel(it) }
    }

    open fun adicionar(model: TModel): TModel {
        val entity = toEntity(model)
        manager.transaction {
            manager.persist(entity)
        }
        return toModel(entity)
    }

    private fun recuperarEntityPeloId(id: Long): TEntity {
        val query = manager.createQuery("from $entityType.simpleName where id = :id", entityType)
        query.setParameter("id", id)
        return query.singleResult
    }

    open fun recuperarPeloId(id: Long): TModel {
        val entity = recuperarEntityPeloId(id)
        return toModel(entity)
    }

    open fun atualizar(id: Long): TModel {
        val entity = recuperarEntityPeloId(id)!!
        manager.transaction {
            manager.merge(entity)
        }
        return toModel(entity)
    }

    open fun apagar(id: Long) {
        val query = recuperarEntityPeloId(id)
        manager.transaction {
            manager.remove(query)
        }
    }

    private inline fun <T> EntityManager.transaction(block: () -> T): T {
        val tx = this.transaction
        tx.begin()
        try {
            val result = block()
            tx.commit()
            return result
        } catch (e: Exception) {
            tx.rollback()
            throw e
        }
    }

}