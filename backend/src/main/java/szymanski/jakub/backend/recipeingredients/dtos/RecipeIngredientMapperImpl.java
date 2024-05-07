package szymanski.jakub.backend.recipeingredients.dtos;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.shared.mappers.Mapper;

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
