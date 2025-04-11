package szymanski.jakub.backend.user.services;

import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {

    /**
     * Finds all users.
     *
     * @return  list of all {@link UserDto} objects or empty list if none was found
     */
    List<UserDto> findAll();

    /**
     * Finds user by ID.
     *
     * @param   id                      ID of user to find
     * @return                          {@link UserDto} object with given ID
     * @throws  UserNotFoundException   if user with given ID was not found
     */
    UserDto find(Long id);

    /**
     * Finds user by username.
     *
     * @param   username                username of user to find
     * @return                          {@link UserDto} object with given username
     * @throws  UserNotFoundException   if user with given username was not found
     */
    UserDto find(String username);

    /**
     * Saves a user.
     *
     * @param   user    {@link UserDto} object with user's data to save
     * @return          ID of saved user
     */
    Long save(UserDto user);

    /**
     * Updates user with specified ID.
     *
     * @param   id                      ID of the user to update
     * @param   user                    {@link UserDto} object containing user's updated data
     * @return                          ID of updated user
     * @throws  UserNotFoundException   if user with given ID was not found
     */
    Long partialUpdate(Long id, UserDto user);

    /**
     * Deletes user with given ID.
     *
     * @param   id          ID of user to delete
     * @throws  UserNotFoundException   if user with given ID was not found
     */
    void delete(Long id);

    /**
     * Deletes specified user.
     *
     * @param   user        {@link UserDto} object to delete
     * @throws  UserNotFoundException   if user was not found
     */
    void delete(UserDto user);

    /**
     * Checks if user with given ID exists.
     *
     * @param   id          ID of user to check
     * @return              <code>true</code> if user exists, <code>false</code> otherwise
     */
    boolean exists(Long id);

    /**
     * Checks if user exists.
     *
     * @param   user    {@link UserEntity} object to check
     * @return          <code>true</code> if user exists, <code>false</code> otherwise
     */
    boolean exists(UserEntity user);

}
