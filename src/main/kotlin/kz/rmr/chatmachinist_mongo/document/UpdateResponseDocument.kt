package kz.rmr.chatmachinist_mongo.document

import kz.rmr.chatmachinist.model.ReplyResult
import kz.rmr.chatmachinist.model.UpdateStatus
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.telegram.telegrambots.meta.api.objects.Update
import java.lang.Exception

@Document("update_response")
class UpdateResponseDocument(
    @Id
    val id: ObjectId = ObjectId(),
    val update: Update,
    val status: UpdateStatus,
    val exception: String?,
    val matchedTransition: MatchedTransitionDocument?,
    val replyResult: ReplyResult?
)
