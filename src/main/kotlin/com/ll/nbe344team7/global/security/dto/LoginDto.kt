package com.ll.nbe344team7.global.security.dto

data class LoginDto(
    val username: String,
    val password: String
){
    constructor(): this("", "")
}
