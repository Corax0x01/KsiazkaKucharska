package szymanski.jakub.backend.ingredient.dtos;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.common.Mapper;

/**
 * Implementation of {@link Mapper} to map between {@link IngredientEntity} and {@link IngredientDto}.
 */
@Component
public class IngredientMapperImpl implements Mapper<IngredientEntity, IngredientDto> {

    private final ModelMapper modelMapper;

    /**
     * Class constructor.
     *
     * @param   modelMapper instance of {@link ModelMapper}
     */
    public IngredientMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Maps from {@link IngredientEntity} to {@link IngredientDto}.
     *
     * @param   ingredientEntity    {@link IngredientEntity} object to be mapped
     * @return                      {@link IngredientDto} object from given {@link IngredientEntity}
     */
    @Override
    public IngredientDto mapTo(IngredientEntity ingredientEntity) {
        return modelMapper.map(ingredientEntity, IngredientDto.class);
    }

    /**
     * Maps from {@link IngredientDto} to {@link IngredientEntity}.
     *
     * @param   ingredientDto       {@link IngredientDto} object to be mapped
     * @return                      {@link IngredientEntity} object from given {@link IngredientDto}
     */
    @Override
    public IngredientEntity mapFrom(IngredientDto ingredientDto) {
        return modelMapper.map(ingredientDto, IngredientEntity.class);
    }
}
