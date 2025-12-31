package kz.rmr.chatmachinist_mongo.document

import kz.rmr.chatmachinist.model.ActionContext
import kz.rmr.chatmachinist.model.EventType

data class MatchedTransitionDocument(
    val dialog: DialogDocument,
    val transitionDefinition: TransitionDefinitionDocument,
    val actionContext: ActionContext<*, *>
)

data class TriggerDefinitionDocument(
    val triggerChatName: String,
    val triggerContext: Any?,
    val triggerChatId: String,
    val triggerDialogId: String?,
)

data class ThenDefinitionDocument(
    val to: String,
    val triggerDefinition: TriggerDefinitionDocument?,
    val noReply: Boolean
)

data class DesiredConditionDefinitionDocument(
    val eventTypes: List<EventType>,
    val from: String?,
    val buttonType: Enum<*>?,
    val text: String?,
    val guarded: Boolean,
    val repliedToMessage: Boolean?,
)

data class TransitionDefinitionDocument(
    val name: String,
    val startDialog: Boolean,
    val desiredConditions: List<DesiredConditionDefinitionDocument>,
    val thenDefinition: ThenDefinitionDocument
)