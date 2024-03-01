package szymanski.jakub.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.backend.domain.dto.IngredientDto;
import szymanski.jakub.backend.domain.entities.IngredientEntity;
import szymanski.jakub.backend.mappers.Mapper;

@Component
public class IngredientMapperImpl implements Mapper<IngredientEntity, IngredientDto> {

    private final ModelMapper modelMapper;

    public IngredientMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public IngredientDto mapTo(IngredientEntity ingredientEntity) {
        return modelMapper.map(ingredientEntity, IngredientDto.class);
    }

    @Override
    public IngredientEntity mapFrom(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, IngredientEntity.class);
    }
}
