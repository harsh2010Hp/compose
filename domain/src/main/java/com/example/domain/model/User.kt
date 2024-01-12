package com.example.domain.model

data class User(
    val name: String? = null,
    val email: String? = null,
    val username: String? = null,
    val companyName: String? = null,
    val address: Address? = null,
)

data class Address(
    val zipcode: String? = null,
    val city: String? = null,
    val street: String? = null
)

