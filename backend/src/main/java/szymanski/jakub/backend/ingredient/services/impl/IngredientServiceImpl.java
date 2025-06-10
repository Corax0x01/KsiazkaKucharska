package szymanski.jakub.backend.ingredient.services.impl;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException;
import szymanski.jakub.backend.ingredient.repositories.IngredientRepository;
import szymanski.jakub.backend.ingredient.services.IngredientService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final Mapper<IngredientEntity, IngredientDto> ingredientMapper;

    public List<IngredientDto> findAll() {
        return ingredientRepository
                .findAll()
                .stream()
                .map(ingredientMapper::mapTo)
                .toList();
    }

    public IngredientDto find(@NonNull Long id) {
        IngredientEntity ingredientEntity = ingredientRepository.findById(id)
                .orElseThrow(
                        () -> new IngredientNotFoundException("Ingredient with id: " + id + " not found")
                );

        return ingredientMapper.mapTo(ingredientEntity);
    }

    public IngredientDto find(@NonNull String name) {
        IngredientEntity ingredientEntity = ingredientRepository.findByName(name)
                .orElseThrow(
                        () -> new IngredientNotFoundException("Ingredient with name: " + name + " not found")
                );

        return ingredientMapper.mapTo(ingredientEntity);
    }

    public Long save(@NonNull IngredientDto ingredient) {
        IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredient);

        return ingredientRepository.save(ingredientEntity).getId();
    }

    public Long partialUpdate(@NonNull Long id, @NonNull IngredientDto ingredient) {
        ingredient.setId(id);

        IngredientEntity updatedIngredient = ingredientRepository.findById(id).map(existingIngredient -> {
            Optional.ofNullable(ingredient.getName()).ifPresent(existingIngredient::setName);
            return ingredientRepository.save(existingIngredient);
        }).orElseThrow(
                () -> new IngredientNotFoundException("Ingredient with id: " + id + " not found")
        );

        return updatedIngredient.getId();
    }

    public void delete(@NonNull Long id) {
        if (!exists(id)) {
            throw new IngredientNotFoundException("Ingredient with id: " + id + " not found");
        }
        ingredientRepository.deleteById(id);
    }

    public void delete(@NonNull String name) {
        if(!exists(name)) {
            throw new IngredientNotFoundException("Ingredient with name: " + name + " not found");
        }
        ingredientRepository.deleteByName(name);
    }

    public void delete(@NonNull IngredientDto ingredient) {
        Long id = ingredient.getId();
        String name = ingredient.getName();
        if(id != null) {
            if(!exists(id)){
                throw new IngredientNotFoundException("Ingredient " + ingredient + " not found");
            } else {
                ingredientRepository.deleteById(id);
            }
        }else if(name != null) {
            if (!exists(name)) {
                throw new IngredientNotFoundException("Ingredient " + ingredient + " not found");
            } else {
                ingredientRepository.deleteByName(name);
            }
        }else {
            throw new NullPointerException("Cannot delete ingredient. ID and name are null");
        }
    }

    public boolean exists(@NonNull Long id) {
        return ingredientRepository.existsById(id);
    }

    public boolean exists(@NonNull String name) {
        return ingredientRepository.findByName(name).isPresent();
    }
}
