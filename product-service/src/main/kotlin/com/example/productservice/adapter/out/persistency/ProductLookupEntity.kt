package com.example.productservice.adapter.out.persistency
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="productlookup")
data class ProductLookupEntity(
    @Id
    @Column(unique=true)
    val productId: UUID,
    @Column(unique=true)
    val title:String,
)

//https://axoniq.io/blog-overview/set-based-validation
//Only save the constraints fields