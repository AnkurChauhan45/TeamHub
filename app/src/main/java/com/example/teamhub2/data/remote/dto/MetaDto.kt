package com.example.teamhub2.data.remote.dto

data class MetaDto(
    val total_count: Int,
    val page: Int,
    val page_size: Int,
    val has_next_page: Boolean
)