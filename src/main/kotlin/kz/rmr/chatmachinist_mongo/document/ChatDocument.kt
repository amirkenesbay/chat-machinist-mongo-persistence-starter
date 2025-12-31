package kz.rmr.chatmachinist_mongo.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.telegram.telegrambots.meta.api.objects.User

@Document("chats")
data class ChatDocument(
    @Id
    val id: ObjectId,
    val name: String,
    @Indexed
    val externalId: String,
    val dialogs: List<DialogDocument>,
    val user: User,
    val languageCode: String?,
)

data class DialogDocument(
    val id: ObjectId,
    val name: String,
    val messageIds: List<Int>,
    val state: String?,
    val stateClass: String,
    val pinnedMessageId: Int?,
    val context: Any?,
)