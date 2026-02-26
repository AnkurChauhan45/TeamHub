package com.example.teamhub2.data.mapper

import com.example.teamhub2.data.local.entity.EmployeeEntity
import com.example.teamhub2.data.remote.dto.EmployeeDto

fun EmployeeDto.toEntity(): EmployeeEntity {
    return EmployeeEntity(
        id = id,
        name = name,
        designation = designation,
        department = department,
        isActive = is_active,
        imgUrl = img_url,
        email = email,
        city = city,
        country = country,
        joiningDate = joining_date
    )
}
