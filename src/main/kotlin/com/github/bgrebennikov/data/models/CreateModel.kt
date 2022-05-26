package com.github.bgrebennikov.data.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateModel(
    val userId: Int,
    val message: String
)
