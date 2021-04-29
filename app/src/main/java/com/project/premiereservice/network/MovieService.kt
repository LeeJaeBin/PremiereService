package com.project.premiereservice.network

object MovieService {
    private const val SERVER_URL = "http://192.168.100.20/servertest"

    val client = BaseService().getClient(SERVER_URL)?.create(MovieApi::class.java)
}