package com.example.enggo.api

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.enggo.model.GPTQuestion
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ChatGPTClient {
    private const val BASE_URL = "https://api.openai.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val chatGPTApi: ChatGPTApiService by lazy {
        retrofit.create(ChatGPTApiService::class.java)
    }

    val conversation: MutableList<Message> = mutableListOf()

    fun generateQuestions(paragraph: String, onQuestionsReceived: (List<GPTQuestion>) -> Unit) {
        val prompt = "Here is a paragraph: '$paragraph'. Please generate several T/F/NG questions from it." +
                "Sample answer, please follow this format: 1. Cat is a small domesticated carnivorous mammal? True\n" +
                "2. Cat is the the largest domesticated species of the family Felidae? False"
        val message = Message(role = "user", content = prompt)
        conversation.add(message)

        val request = ChatGPTRequest(
            model = "gpt-3.5-turbo",
            messages = conversation
        )
        val call = chatGPTApi.chatWithGPT(request)
        call.enqueue(object : Callback<ChatGPTResponse> {
            override fun onResponse(call: Call<ChatGPTResponse>, response: Response<ChatGPTResponse>) {
                if (response.isSuccessful) {
                    val chatResponse = response.body()
                    val questions = chatResponse?.choices?.firstOrNull()?.message?.content
                        ?.let { parseResponse(it) } ?: emptyList()

                    Log.d("QUESTION GENERATED", questions.toString())

                    // Khi có câu hỏi từ GPT, gọi hàm onQuestionsReceived để truyền vào UI
                    onQuestionsReceived(questions)
                    val reply = chatResponse?.choices?.firstOrNull()?.message?.content ?: "No response"
                    Log.d("GPT reply", reply)

                } else {
                    val reply = "Request failed with code: ${response.code()}"
                    Log.e("reply", reply)
                }
            }

            override fun onFailure(call: Call<ChatGPTResponse>, t: Throwable) {
                val reply = "Request failed: ${t.message}"
                Log.e("reply", reply)
            }
        })
    }

    fun parseResponse(response: String): List<GPTQuestion> {
        val regex = """(\d+)\.\s(.*?)\s*(True|False)""".toRegex()
        val questions = mutableListOf<GPTQuestion>()
        regex.findAll(response).forEach { match ->
            val questionText = match.groupValues[2]
            val answer = match.groupValues[3]
            questions.add(GPTQuestion(questionText, answer))
        }
        return questions
    }
}