package kz.rmr.chatmachinist_mongo.repository

import kz.rmr.chatmachinist_mongo.document.CallbackDataDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface CallbackDataDocumentRepository: MongoRepository<CallbackDataDocument, String> {

    fun findByEncodedData(encodedData: String): CallbackDataDocument?

}