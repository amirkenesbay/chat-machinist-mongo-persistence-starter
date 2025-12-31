package kz.rmr.chatmachinist_mongo.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("callback_data")
data class CallbackDataDocument (
    @Id
    val id: ObjectId = ObjectId(),
    val encodedData: String,
    val callbackData: Any,
)
