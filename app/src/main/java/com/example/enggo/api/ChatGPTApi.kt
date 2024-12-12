package com.example.enggo.api

import com.example.enggo.BuildConfig
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.Serializable


interface ChatGPTApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${BuildConfig.openAIkey}"
    )
    @POST("v1/chat/completions")
    fun chatWithGPT(
        @Body request: ChatGPTRequest
    ): Call<ChatGPTResponse>
}

data class ChatGPTRequest(
    @SerializedName("model")
    val model: String,
    @SerializedName("messages")
    val messages: List<Message>
): Serializable {
    override fun toString(): String {
        return "ChatGPTRequest(model='$model', messages=${messages.joinToString(separator = "\n")})"
    }
}

data class Message(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String
): Serializable {
    override fun toString(): String {
        return "Message(role='$role', content='$content')"
    }
}

data class ChatGPTResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("choices")
    val choices: List<Choice>
): Serializable {
    override fun toString(): String {
        return "ChatGPTResponse(id='$id', choices=${choices.joinToString(separator = "\n")})"
    }
}

data class Choice(
    @SerializedName("index")
    val index: Int,
    @SerializedName("message")
    val message: Message
): Serializable {
    override fun toString(): String {
        return "Choice(index= $index , message=$message)"
    }
}