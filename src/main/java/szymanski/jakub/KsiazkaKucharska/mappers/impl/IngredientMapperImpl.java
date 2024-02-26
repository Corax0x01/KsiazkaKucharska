package szymanski.jakub.KsiazkaKucharska.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.KsiazkaKucharska.domain.dto.IngredientDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.IngredientEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;

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
