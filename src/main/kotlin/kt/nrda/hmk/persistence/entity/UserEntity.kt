package kt.nrda.hmk.persistence.entity

import jakarta.persistence.*

@Entity
@Table(name = "users", schema = "hmk") // Changed table name to "users"
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 50)
    var login: String,

    @Column(length = 255)
    var email: String? = null, // Changed from 'correo' to 'email' to match DB

    @Column(nullable = false)
    var password: String
)