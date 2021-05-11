package com.project.premiereservice.network

object MovieService {
    private const val SERVER_URL = "http://122.199.199.20/servertest/"

    val client = BaseService().getClient(SERVER_URL)?.create(MovieApi::class.java)
}