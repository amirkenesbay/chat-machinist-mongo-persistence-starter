package kz.rmr.chatmachinist_mongo.autoconfigure

import kz.rmr.chatmachinist.model.CallbackData
import kz.rmr.chatmachinist.model.Chat
import kz.rmr.chatmachinist.model.UpdateResponse
import kz.rmr.chatmachinist.persistence.CallbackDataRepository
import kz.rmr.chatmachinist.persistence.ChatRepository
import kz.rmr.chatmachinist.persistence.UpdateResponseRepository
import kz.rmr.chatmachinist_mongo.MongoCallbackDataRepository
import kz.rmr.chatmachinist_mongo.MongoChatRepository
import kz.rmr.chatmachinist_mongo.MongoUpdateResponseRepository
import kz.rmr.chatmachinist_mongo.document.CallbackDataDocument
import kz.rmr.chatmachinist_mongo.document.ChatDocument
import kz.rmr.chatmachinist_mongo.document.UpdateResponseDocument
import kz.rmr.chatmachinist_mongo.mapper.CallbackDataMapperImpl
import kz.rmr.chatmachinist_mongo.mapper.ChatMapper
import kz.rmr.chatmachinist_mongo.mapper.DialogMapper
import kz.rmr.chatmachinist_mongo.mapper.Mapper
import kz.rmr.chatmachinist_mongo.mapper.MatchedTransitionMapper
import kz.rmr.chatmachinist_mongo.mapper.UpdateResponseMapperImpl
import kz.rmr.chatmachinist_mongo.repository.CallbackDataDocumentRepository
import kz.rmr.chatmachinist_mongo.repository.ChatDocumentRepository
import kz.rmr.chatmachinist_mongo.repository.UpdateResponseDocumentRepository
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@AutoConfiguration
@ConditionalOnProperty("chat-machinist.persistence.type", havingValue = "mongodb", matchIfMissing = false)
@EnableMongoRepositories(basePackages = ["kz.rmr.chatmachinist_mongo.repository"])
class MongoPersistenceAutoConfiguration<STATE : Enum<STATE>, CONTEXT : Any> {


    @Bean
    fun chatRepository(
        chatDocumentRepository: ChatDocumentRepository,
        chatMapper: Mapper<Chat<STATE, CONTEXT>, ChatDocument>
    ): ChatRepository<STATE, CONTEXT> {
        return MongoChatRepository(
            chatMapper,
            chatDocumentRepository
        )
    }

    @Bean
    fun updateResponseRepository(
        updateResponseMapper: Mapper<UpdateResponse, UpdateResponseDocument>,
        updateResponseDocumentRepository: UpdateResponseDocumentRepository
    ): UpdateResponseRepository<STATE, CONTEXT> {
        return MongoUpdateResponseRepository(
            updateResponseMapper,
            updateResponseDocumentRepository
        )
    }

    @Bean
    fun callbackDataRepository(
        callbackDataDocumentRepository: CallbackDataDocumentRepository,
        callbackDataMapper: Mapper<CallbackData, CallbackDataDocument>
    ): CallbackDataRepository {
        return MongoCallbackDataRepository(
            callbackDataDocumentRepository,
            callbackDataMapper
        )
    }

    @Bean
    fun dialogMapper(): DialogMapper<STATE, CONTEXT> {
        return DialogMapper()
    }

    @Bean
    fun chatMapper(
        dialogMapper: DialogMapper<STATE, CONTEXT>
    ): ChatMapper<STATE, CONTEXT> {
        return ChatMapper(dialogMapper)
    }

    @Bean
    fun matchedTransitionMapper(
        dialogMapper: DialogMapper<STATE, CONTEXT>
    ): MatchedTransitionMapper<STATE, CONTEXT> {
        return MatchedTransitionMapper(dialogMapper)
    }

    @Bean
    fun updateMapper(
        matchedTransitionMapper: MatchedTransitionMapper<STATE, CONTEXT>
    ): Mapper<UpdateResponse, UpdateResponseDocument> {
        return UpdateResponseMapperImpl(
            matchedTransitionMapper
        )
    }

    @Bean
    fun callbackDataMapper(): Mapper<CallbackData, CallbackDataDocument> {
        return CallbackDataMapperImpl()
    }
}