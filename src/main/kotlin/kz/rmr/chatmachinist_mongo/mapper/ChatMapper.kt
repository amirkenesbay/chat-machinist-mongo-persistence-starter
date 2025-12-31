package kz.rmr.chatmachinist_mongo.mapper

import kz.rmr.chatmachinist.model.*
import kz.rmr.chatmachinist_mongo.document.CallbackDataDocument
import kz.rmr.chatmachinist_mongo.document.ChatDocument
import kz.rmr.chatmachinist_mongo.document.DesiredConditionDefinitionDocument
import kz.rmr.chatmachinist_mongo.document.DialogDocument
import kz.rmr.chatmachinist_mongo.document.MatchedTransitionDocument
import kz.rmr.chatmachinist_mongo.document.ThenDefinitionDocument
import kz.rmr.chatmachinist_mongo.document.TransitionDefinitionDocument
import kz.rmr.chatmachinist_mongo.document.TriggerDefinitionDocument
import kz.rmr.chatmachinist_mongo.document.UpdateResponseDocument
import org.bson.types.ObjectId

interface Mapper<MODEL, ENTITY> {

    fun fromModel(model: MODEL): ENTITY

    fun fromDocument(document: ENTITY): MODEL
}

class DialogMapper<STATE : Enum<STATE>, CONTEXT : Any> :
    Mapper<Dialog<STATE, CONTEXT>, DialogDocument> {
    override fun fromModel(model: Dialog<STATE, CONTEXT>): DialogDocument {
        return DialogDocument(
            id = model.id?.let { ObjectId(it) } ?: ObjectId(),
            name = model.name,
            messageIds = model.botMessageIds,
            state = model.currentState?.name,
            stateClass = model.currentState!!.javaClass.canonicalName,
            pinnedMessageId = model.pinnedMessageId,
            context = model.context,
        )
    }

    override fun fromDocument(document: DialogDocument): Dialog<STATE, CONTEXT> {
        val stateClass = (this.javaClass.classLoader.loadClass(document.stateClass) as Class<STATE>)

        return Dialog(
            id = document.id.toHexString(),
            name = document.name,
            currentState = stateClass.enumConstants.first { it.name == document.state },
            botMessageIds = document.messageIds.toMutableList(),
            pinnedMessageId = document.pinnedMessageId,
            context = document.context as CONTEXT,
        )
    }

}

class ChatMapper<STATE : Enum<STATE>, CONTEXT : Any>(
    private val dialogMapper: DialogMapper<STATE, CONTEXT>
) : Mapper<Chat<STATE, CONTEXT>, ChatDocument> {

    override fun fromModel(chat: Chat<STATE, CONTEXT>): ChatDocument {
        return ChatDocument(
            id = chat.id?.let { ObjectId(it) } ?: ObjectId(),
            externalId = chat.externalId,
            dialogs = chat.dialogs.map {
                dialogMapper.fromModel(it)
            },
            user = chat.user,
            name = chat.name,
            languageCode = chat.languageCode,
        )
    }

    override fun fromDocument(document: ChatDocument): Chat<STATE, CONTEXT> {
        return Chat(
            id = document.id.toHexString(),
            externalId = document.externalId,
            name = document.name,
            user = document.user,
            dialogs = document.dialogs.map {
                dialogMapper.fromDocument(it)
            }.toMutableList(),
            languageCode = document.languageCode,
        )
    }
}


class MatchedTransitionMapper<STATE : Enum<STATE>, CONTEXT : Any>(
    private val dialogMapper: DialogMapper<STATE, CONTEXT>
) : Mapper<MatchedTransition<STATE, CONTEXT>, MatchedTransitionDocument> {
    override fun fromModel(model: MatchedTransition<STATE, CONTEXT>): MatchedTransitionDocument {
        return MatchedTransitionDocument(
            dialog = dialogMapper.fromModel(model.dialog),
            transitionDefinition = model.transitionDefinition.let {
                TransitionDefinitionDocument(
                    name = it.name,
                    startDialog = it.startDialog,
                    desiredConditions = it.desiredConditions.map { desiredCondition ->
                        DesiredConditionDefinitionDocument(
                            eventTypes = desiredCondition.eventTypes,
                            from = desiredCondition.from?.name,
                            buttonType = desiredCondition.buttonType,
                            text = desiredCondition.text,
                            guarded = desiredCondition.guard != null,
                            repliedToMessage = desiredCondition.repliedToMessage,
                        )
                    },
                    thenDefinition = ThenDefinitionDocument(
                        to = it.thenDefinition.to.name,
                        triggerDefinition = it.thenDefinition.triggerDefinition?.let { triggerDefinition ->
                            TriggerDefinitionDocument(
                                triggerChatName = triggerDefinition.triggerChatNameResolver.invoke(model.actionContext),
                                triggerContext = triggerDefinition.triggerContextBuilder?.invoke(model.actionContext),
                                triggerChatId = triggerDefinition.triggerChatIdResolver.invoke(model.actionContext)
                                    .toString(),
                                triggerDialogId = triggerDefinition.triggerDialogIdResolver?.invoke(model.actionContext),
                            )
                        },
                        noReply = it.thenDefinition.noReply,
                    )
                )
            },
            actionContext = model.actionContext,
        )
    }

    override fun fromDocument(document: MatchedTransitionDocument): MatchedTransition<STATE, CONTEXT> {
        TODO()
//        return MatchedTransition(
//            dialog = dialogMapper.fromDocument(document.dialog),
//            transitionDefinition = document.transitionDefinition as TransitionDefinition<STATE, CONTEXT>,
//            actionContext = document.actionContext as ActionContext<STATE, CONTEXT>
//        )
    }

}


class UpdateResponseMapperImpl<STATE : Enum<STATE>, CONTEXT : Any>(
    private val matchedTransitionMapper: MatchedTransitionMapper<STATE, CONTEXT>
) : Mapper<UpdateResponse, UpdateResponseDocument> {

    override fun fromModel(model: UpdateResponse): UpdateResponseDocument {
        return UpdateResponseDocument(
            id = model.id?.let { ObjectId(it) } ?: ObjectId(),
            update = model.update,
            status = model.status,
            exception = model.exception,
            matchedTransition = model.matchedTransition?.let { matchedTransitionMapper.fromModel(it as MatchedTransition<STATE, CONTEXT>) },
            replyResult = model.replyResult,
        )
    }

    override fun fromDocument(document: UpdateResponseDocument): UpdateResponse {
        TODO()
    }
}

class CallbackDataMapperImpl : Mapper<CallbackData, CallbackDataDocument> {
    override fun fromModel(model: CallbackData): CallbackDataDocument {
        return CallbackDataDocument(
            id = model.id?.let { ObjectId(it) } ?: ObjectId(),
            encodedData = model.encodedData,
            callbackData = model.callbackData,
        )
    }

    override fun fromDocument(document: CallbackDataDocument): CallbackData {
        return CallbackData(
            id = document.id.toHexString(),
            encodedData = document.encodedData,
            callbackData = document.callbackData,
        )
    }
}