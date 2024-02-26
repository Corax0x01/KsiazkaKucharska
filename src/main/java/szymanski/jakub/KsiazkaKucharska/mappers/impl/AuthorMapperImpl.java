package szymanski.jakub.KsiazkaKucharska.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.KsiazkaKucharska.domain.dto.UserDto;
import szymanski.jakub.KsiazkaKucharska.domain.entities.UserEntity;
import szymanski.jakub.KsiazkaKucharska.mappers.Mapper;

@Component
public class AuthorMapperImpl implements Mapper<UserEntity, UserDto> {

    private final ModelMapper modelMapper;

    public AuthorMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
