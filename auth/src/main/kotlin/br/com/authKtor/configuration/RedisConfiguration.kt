package com.example.main.kotlin.br.com.authKtor.configuration

import redis.clients.jedis.Jedis

object RedisConfiguration {
    val jedis: Jedis = Jedis("localhost", 6379)

    fun connect() {
        jedis.connect()
    }
}