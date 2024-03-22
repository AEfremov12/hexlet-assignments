package exercise.mapper;

import org.mapstruct.*;

import exercise.dto.CarCreateDTO;
import exercise.dto.CarUpdateDTO;
import exercise.dto.CarDTO;
import exercise.model.Car;

// BEGIN
@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class CarMapper {

//    public abstract Car map(@MappingTarget CarUpdateDTO dto);
    public abstract Car map(CarCreateDTO dto);
    public abstract CarDTO map(Car car);

    public abstract void update (CarUpdateDTO dto, @MappingTarget Car car);
//    public abstract void fromUpdateToModel (Car car, @MappingTarget CarUpdateDTO dto);
}
// END
