package szymanski.jakub.backend.ingredient.services.impl;

import org.springframework.stereotype.Service;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.entities.IngredientEntity;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.ingredient.repositories.IngredientRepository;
import szymanski.jakub.backend.ingredient.services.IngredientService;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final Mapper<IngredientEntity, IngredientDto> ingredientMapper;

    public IngredientServiceImpl(IngredientRepository ingredientRepository, Mapper<IngredientEntity, IngredientDto> ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public List<IngredientDto> findAll() {
        return ingredientRepository.findAll().stream().map(ingredientMapper::mapTo).toList();
    }

    public Optional<IngredientDto> find(Long id) {
        return ingredientRepository.findById(id).map(ingredientMapper::mapTo);
    }

    public Optional<IngredientDto> find(String name) {
        return ingredientRepository.findByName(name).map(ingredientMapper::mapTo);
    }

    public IngredientDto save(IngredientDto ingredient) {
        IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredient);
        return ingredientMapper.mapTo(ingredientRepository.save(ingredientEntity));
    }

    public IngredientDto partialUpdate(Long id, IngredientDto ingredient) {
        ingredient.setId(id);

        IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredient);

        IngredientEntity updatedIngredient = ingredientRepository.findById(id).map(existingIngredient -> {
            Optional.ofNullable(ingredientEntity.getName()).ifPresent(existingIngredient::setName);
            return ingredientRepository.save(existingIngredient);
        }).orElseThrow(() -> new RuntimeException("Ingredient not found"));

        return ingredientMapper.mapTo(updatedIngredient);
    }

    public void delete(Long id) {
        ingredientRepository.deleteById(id);
    }

    public void delete(String name) {
        ingredientRepository.deleteByName(name);
    }

    public void delete(IngredientDto ingredient) {
        IngredientEntity ingredientEntity = ingredientMapper.mapFrom(ingredient);
        ingredientRepository.delete(ingredientEntity);
    }

    public boolean exists(Long id) {
        return ingredientRepository.existsById(id);
    }

    public boolean exists(String name) {
        return ingredientRepository.existsByName(name);
    }

}
