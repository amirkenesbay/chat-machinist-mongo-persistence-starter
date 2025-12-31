package kz.rmr.chatmachinist_mongo

import kz.rmr.chatmachinist.model.CallbackData
import kz.rmr.chatmachinist.persistence.CallbackDataRepository
import kz.rmr.chatmachinist_mongo.document.CallbackDataDocument
import kz.rmr.chatmachinist_mongo.mapper.Mapper
import kz.rmr.chatmachinist_mongo.repository.CallbackDataDocumentRepository

class MongoCallbackDataRepository(
    private val callbackDataRepository: CallbackDataDocumentRepository,
    private val callbackDataMapper: Mapper<CallbackData, CallbackDataDocument>,
    ) : CallbackDataRepository {
    override fun findAll(): List<CallbackData> {
        return callbackDataRepository.findAll().map {
            callbackDataMapper.fromDocument(it)
        }
    }

    override fun findByEncodedData(encodedData: String): CallbackData? {
        return callbackDataRepository.findByEncodedData(encodedData)?.let {
            callbackDataMapper.fromDocument(it)
        }
    }

    override fun save(it: CallbackData): CallbackData {
        return callbackDataMapper.fromModel(it)
            .let {
                callbackDataRepository.save(it)
            }.let {
                callbackDataMapper.fromDocument(it)
            }
    }
}