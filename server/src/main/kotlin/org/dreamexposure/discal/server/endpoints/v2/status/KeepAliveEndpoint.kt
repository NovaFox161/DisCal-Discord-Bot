package org.dreamexposure.discal.server.endpoints.v2.status

import kotlinx.serialization.encodeToString
import org.dreamexposure.discal.core.`object`.network.discal.ConnectedClient
import org.dreamexposure.discal.core.`object`.network.discal.NetworkInfo
import org.dreamexposure.discal.core.logger.LogFeed
import org.dreamexposure.discal.core.logger.`object`.LogObject
import org.dreamexposure.discal.core.utils.GlobalVal
import org.dreamexposure.discal.server.utils.Authentication
import org.dreamexposure.discal.server.utils.responseMessage
import org.json.JSONException
import org.json.JSONObject
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v2/status")
class KeepAliveEndpoint(val networkInfo: NetworkInfo) {

    @PostMapping(value = ["/keep-alive"], produces = ["application/json"])
    fun keepAlive(swe: ServerWebExchange, response: ServerHttpResponse, @RequestBody rBody: String): Mono<String> {
        return Authentication.authenticate(swe).flatMap { authState ->
            if (!authState.success) {
                response.rawStatusCode = authState.status
                return@flatMap Mono.just(GlobalVal.JSON_FORMAT.encodeToString(authState))
            } else if (!authState.fromDiscalNetwork) {
                response.rawStatusCode = GlobalVal.STATUS_AUTHORIZATION_DENIED
                return@flatMap responseMessage("Only official DisCal clients can use this endpoint")
            }

            //Handle request
            val body = JSONObject(rBody)
            val index = body.getInt("index")

            if (networkInfo.doesClientExist(index)) {
                //In network, update info
                val cc = networkInfo.getClient(index)
                val oldPid = cc.pid

                val newClient = cc.copy(
                        expectedClientCount = body.optInt("expected", -1),
                        version = body.optString("version", "Unknown"),
                        d4jVersion = body.optString("d4j_version", "Unknown"),
                        connectedServers = body.getInt("guilds"),
                        lastKeepAlive = System.currentTimeMillis(),
                        uptime = body.getString("uptime"),
                        memUsed = body.getDouble("memory"),
                        pid = body.getString("pid"),
                )

                if (oldPid != body.getString("pid")) {
                    LogFeed.log(LogObject.forStatus("Client pid changed", "Shard index: ${cc.clientIndex}"))
                }

                networkInfo.updateClient(newClient)
            } else {
                //Not in network, add info
                val cc = ConnectedClient(
                        index,
                        body.optInt("expected", -1),
                        body.optString("version", "Unknown"),
                        body.optString("d4j_version", "Unknown"),
                        body.getInt("guilds"),
                        System.currentTimeMillis(),
                        body.getString("uptime"),
                        body.getDouble("memory"),
                        body.getString("pid")
                )

                networkInfo.addClient(cc)
            }

            response.rawStatusCode = GlobalVal.STATUS_SUCCESS
            return@flatMap responseMessage("Success!")
        }.onErrorResume(JSONException::class.java) {
            it.printStackTrace()

            response.rawStatusCode = GlobalVal.STATUS_BAD_REQUEST
            return@onErrorResume responseMessage("Bad Request")
        }.onErrorResume {
            LogFeed.log(LogObject.forException("[API-v2] keep alive err", it, this.javaClass))

            response.rawStatusCode = GlobalVal.STATUS_INTERNAL_ERROR
            return@onErrorResume responseMessage("Internal Server Error")
        }
    }
}
