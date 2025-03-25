package szymanski.jakub.backend.recipe.dtos;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.common.Mapper;

/**
 * Implementation of {@link Mapper} to map between {@link RecipeEntity} and {@link RecipeDto}.
 */
@Component
public class RecipeMapperImpl implements Mapper<RecipeEntity, RecipeDto> {

    private final ModelMapper modelMapper;

    /**
     * Class constructor.
     *
     * @param   modelMapper     instance of {@link ModelMapper}
     */
    public RecipeMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Maps from {@link RecipeEntity} to {@link RecipeDto}.
     *
     * @param   recipeEntity    {@link RecipeEntity} object to be mapped
     * @return                  {@link RecipeDto} object from given {@link RecipeEntity}
     */
    @Override
    public RecipeDto mapTo(RecipeEntity recipeEntity) {
        return modelMapper.map(recipeEntity, RecipeDto.class);
    }

    /**
     * Maps from {@link RecipeDto} to {@link RecipeEntity}.
     *
     * @param   recipeDto       {@link RecipeDto} object to be mapped
     * @return                  {@link RecipeEntity} object from given {@link RecipeDto}
     */
    @Override
    public RecipeEntity mapFrom(RecipeDto recipeDto) {
        return modelMapper.map(recipeDto, RecipeEntity.class);
    }
}
