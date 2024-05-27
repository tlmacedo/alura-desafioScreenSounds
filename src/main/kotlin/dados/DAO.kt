package br.com.tlmacedo.alura.desafio.dados

import jakarta.persistence.EntityManager

abstract class DAO<TModel, TEntity>(
    val manager: EntityManager,
    val entityType: Class<TEntity>
) {

    abstract fun toModel(entity: TEntity): TModel
    abstract fun toEntity(model: TModel): TEntity

    open fun findAll(): List<TModel> {
        val entities = manager.createQuery("from $entityType.simpleName", entityType).resultList
        return entities.map { toModel(it) }
    }

    open fun create(model: TModel): TModel {
        val entity = toEntity(model)
        manager.transaction {
            manager.persist(entity)
        }
        return toModel(entity)
    }

    open fun find(id: Long): TModel? {
        val entity = manager.find(entityType, id)
        return entity?.let { toModel(it) }
    }

    open fun update(id: Long): TModel {
        val entity = toEntity(find(id)!!)
        manager.transaction {
            manager.merge(entity)
        }
        return toModel(entity)
    }

    fun delete(id: Long) {
        val entity = toEntity(find(id)!!)
        manager.transaction {
            manager.remove(entity)
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