package szymanski.jakub.backend.user.dtos;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.common.Mapper;

/**
 * Implementation of {@link Mapper} to map between {@link UserEntity} and {@link UserDto}.
 */
@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

    private final ModelMapper modelMapper;

    /**
     * Class constructor.
     *
     * @param   modelMapper instance of {@link ModelMapper}
     */
    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Maps from {@link UserEntity} to {@link UserDto}.
     *
     * @param   userEntity  {@link UserEntity} object to be mapped
     * @return              {@link UserDto} object from given {@link UserEntity}
     */
    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    /**
     * Maps from {@link UserDto} to {@link UserEntity}.
     *
     * @param   userDto {@link UserDto} object to be mapped
     * @return          {@link UserEntity} object from given {@link UserDto}
     */
    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
