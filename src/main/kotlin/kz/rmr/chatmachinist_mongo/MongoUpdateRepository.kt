package kz.rmr.chatmachinist_mongo

import kz.rmr.chatmachinist.model.UpdateResponse
import kz.rmr.chatmachinist.persistence.UpdateResponseRepository
import kz.rmr.chatmachinist_mongo.document.UpdateResponseDocument
import kz.rmr.chatmachinist_mongo.mapper.Mapper
import kz.rmr.chatmachinist_mongo.repository.UpdateResponseDocumentRepository

class MongoUpdateResponseRepository<STATE : Enum<STATE>, CONTEXT : Any>(
    private val updateResponseMapper: Mapper<UpdateResponse, UpdateResponseDocument>,
    private val updateResponseRepository: UpdateResponseDocumentRepository
) : UpdateResponseRepository<STATE, CONTEXT> {

    override fun save(updateResponse: UpdateResponse): UpdateResponse {
        updateResponseRepository.save(
            updateResponseMapper.fromModel(updateResponse)
        )
        return updateResponse
    }
}