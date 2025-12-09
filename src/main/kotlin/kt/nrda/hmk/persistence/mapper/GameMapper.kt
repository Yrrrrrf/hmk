package kt.nrda.hmk.persistence.mapper

import kt.nrda.hmk.domain.model.Game
import kt.nrda.hmk.persistence.entity.GameEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface GameMapper {
    fun toEntity(domain: Game): GameEntity
    fun toDomain(entity: GameEntity): Game
}
