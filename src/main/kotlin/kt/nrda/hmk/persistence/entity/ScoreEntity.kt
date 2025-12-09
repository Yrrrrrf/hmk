package kt.nrda.hmk.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "records", schema = "hmk")
data class ScoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    var game: GameEntity,

    @Column(nullable = false)
    var score: Int,

    @Column(name = "played_at")
    var playedAt: LocalDateTime? = null
)
