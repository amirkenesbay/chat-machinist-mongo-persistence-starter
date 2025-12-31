package kz.rmr.chatmachinist_mongo.repository

import kz.rmr.chatmachinist_mongo.document.ChatDocument
import org.springframework.data.mongodb.repository.MongoRepository


interface ChatDocumentRepository: MongoRepository<ChatDocument, String> {

    fun findByExternalId(externalId: String): ChatDocument?
}