package br.com.zup.beagleui.framework.data

import br.com.zup.beagleui.framework.action.Action
import br.com.zup.beagleui.framework.data.deserializer.BeagleDeserializationException
import br.com.zup.beagleui.framework.data.deserializer.BeagleDeserializer
import br.com.zup.beagleui.framework.exception.BeagleException
import br.com.zup.beagleui.framework.networking.HttpClient
import br.com.zup.beagleui.framework.networking.HttpClientFactory
import br.com.zup.beagleui.framework.networking.RequestData
import br.com.zup.beagleui.framework.widget.core.Widget
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class BeagleService(
    private val deserializer: BeagleDeserializer = BeagleDeserializer(),
    private val httpClient: HttpClient = HttpClientFactory().make()
) {

    @Throws(BeagleException::class)
    suspend fun fetchWidget(url: String): Widget {
        return deserializeWidget(fetchData(url))
    }

    @Throws(BeagleException::class)
    suspend fun fetchAction(url: String): Action {
        return deserializeAction(fetchData(url))
    }

    private suspend fun fetchData(url: String): String = suspendCancellableCoroutine { cont ->
        try {
            val call = httpClient.execute(request = RequestData(url),
                onSuccess = { response ->
                    cont.resume(String(response.data))
                }, onError = { error ->
                    cont.resumeWithException(BeagleException(error.message, error))
                }
            )

            cont.invokeOnCancellation {
                call.cancel()
            }
        } catch (ex: Exception) {
            cont.resumeWithException(BeagleException(ex.message, ex))
        }
    }

    private fun deserializeAction(response: String): Action {
        try {
            return deserializer.deserializeAction(response)
        } catch (exception: BeagleDeserializationException) {
            throw BeagleException("Action deserialization error with respective json: $response")
        }
    }

    private fun deserializeWidget(response: String): Widget {
        try {
            return deserializer.deserializeWidget(response)
        } catch (exception: BeagleDeserializationException) {
            throw BeagleException("Widget deserialization error with respective json: $response")
        }
    }
}
