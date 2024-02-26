package szymanski.jakub.KsiazkaKucharska.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.KsiazkaKucharska.domain.dto.RecipeIngredientDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeIngredientEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;

@Component
public class RecipeIngredientMapperImpl implements Mapper<RecipeIngredientEntity, RecipeIngredientDto> {

    private final ModelMapper modelMapper;

    public RecipeIngredientMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RecipeIngredientDto mapTo(RecipeIngredientEntity recipeIngredientEntity) {
        return modelMapper.map(recipeIngredientEntity, RecipeIngredientDto.class);
    }

    @Override
    public RecipeIngredientEntity mapFrom(RecipeIngredientDto recipeIngredientDto) {
        return modelMapper.map(recipeIngredientDto, RecipeIngredientEntity.class);
    }
}
