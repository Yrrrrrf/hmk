package kt.nrda.hmk.persistence.mapper

import kt.nrda.hmk.domain.model.Score
import kt.nrda.hmk.persistence.entity.ScoreEntity
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [UserMapper::class, GameMapper::class])
interface ScoreEntityMapper {

    fun toEntity(domain: Score): ScoreEntity

    @InheritInverseConfiguration
    fun toDomain(entity: ScoreEntity): Score

    fun toDomainList(entities: List<ScoreEntity>): List<Score>
}
