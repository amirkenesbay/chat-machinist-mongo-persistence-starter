package kz.rmr.chatmachinist_mongo

import kz.rmr.chatmachinist.model.Chat
import kz.rmr.chatmachinist.persistence.ChatRepository
import kz.rmr.chatmachinist_mongo.document.ChatDocument
import kz.rmr.chatmachinist_mongo.mapper.Mapper
import kz.rmr.chatmachinist_mongo.repository.ChatDocumentRepository
import mu.KotlinLogging
import kotlin.reflect.jvm.jvmName

class MongoChatRepository<STATE, CONTEXT>(
    private val chatMapper: Mapper<Chat<STATE, CONTEXT>, ChatDocument>,
    private val chatDocumentRepository: ChatDocumentRepository
) : ChatRepository<STATE, CONTEXT> {

    private val logger = KotlinLogging.logger(this::class.jvmName)

    override fun findByExternalId(externalId: String): Chat<STATE, CONTEXT>? {
        return chatDocumentRepository.findByExternalId(externalId)?.let {
            chatMapper.fromDocument(it)
        }
    }

    override fun save(it: Chat<STATE, CONTEXT>): Chat<STATE, CONTEXT> {
        logger.debug { "Saving the chat ${it.name}" }
        val saved = chatDocumentRepository.save(chatMapper.fromModel(it))
        return chatMapper.fromDocument(saved)
    }
}