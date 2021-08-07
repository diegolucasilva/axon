package com.example.productservice.adapter.out.persistency
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class ProductEntity(
    @Id
    @Column(unique=true)
    val productId: UUID,
    @Column(unique=true)
    val title:String,
    val price:Double,
    var quantity: Int
)