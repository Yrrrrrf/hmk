package kt.nrda.hmk.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "games", schema = "hmk")
data class GameEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null
)
