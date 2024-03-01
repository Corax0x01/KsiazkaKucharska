package szymanski.jakub.backend.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.backend.domain.dto.RecipeDto;
import szymanski.jakub.backend.domain.entities.RecipeEntity;
import szymanski.jakub.backend.mappers.Mapper;

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
