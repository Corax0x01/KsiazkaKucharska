package szymanski.jakub.backend.recipeingredients.dtos;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.common.Mapper;

/**
 * Implementation of {@link Mapper} to map between {@link RecipeIngredientEntity} and {@link RecipeIngredientDto}.
 */
@Component
public class RecipeIngredientMapperImpl implements Mapper<RecipeIngredientEntity, RecipeIngredientDto> {

    private final ModelMapper modelMapper;

    /**
     * Class constructor.
     *
     * @param   modelMapper instance of {@link ModelMapper}
     */
    public RecipeIngredientMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Maps from {@link RecipeIngredientEntity} to {@link RecipeIngredientDto}.
     *
     * @param   recipeIngredientEntity      {@link RecipeIngredientEntity} object to be mapped
     * @return                              {@link RecipeIngredientDto} object from given {@link RecipeIngredientEntity}
     */
    @Override
    public RecipeIngredientDto mapTo(RecipeIngredientEntity recipeIngredientEntity) {
        return modelMapper.map(recipeIngredientEntity, RecipeIngredientDto.class);
    }

    /**
     * Maps from {@link RecipeIngredientDto} to {@link RecipeIngredientEntity}.
     *
     * @param   recipeIngredientDto         {@link RecipeIngredientDto} object to be mapped
     * @return                              {@link RecipeIngredientEntity} object from given {@link RecipeIngredientDto}
     */
    @Override
    public RecipeIngredientEntity mapFrom(RecipeIngredientDto recipeIngredientDto) {
        return modelMapper.map(recipeIngredientDto, RecipeIngredientEntity.class);
    }
}
