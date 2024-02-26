package szymanski.jakub.KsiazkaKucharska.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.KsiazkaKucharska.domain.dto.RecipeDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.RecipeEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;

@Component
public class RecipeMapperImpl implements Mapper<RecipeEntity, RecipeDto> {

    private final ModelMapper modelMapper;

    public RecipeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RecipeDto mapTo(RecipeEntity recipeEntity) {
        return modelMapper.map(recipeEntity, RecipeDto.class);
    }

    @Override
    public RecipeEntity mapFrom(RecipeDto recipeDto) {
        return modelMapper.map(recipeDto, RecipeEntity.class);
    }
}
