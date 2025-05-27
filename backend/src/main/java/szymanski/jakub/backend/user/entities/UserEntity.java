package szymanski.jakub.backend.user.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import szymanski.jakub.backend.common.BaseEntity;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.role.entities.RoleEntity;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User data stored in database.
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(callSuper = false)
public class UserEntity extends BaseEntity implements UserDetails, Principal {

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    /**
     * Defines if user account was locked.
     */
    private Boolean locked;

    /**
     * Defines if user account was enabled by verification of an email.
     */
    private Boolean enabled;

    /**
     * Recipes created by this user.
     */
    @OneToMany(mappedBy = "userEntity")
    private List<RecipeEntity> recipes;

    /**
     * Roles given to a user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoleEntity> roles;

    /**
     * Returns username of this {@link UserEntity} object.
     *
     * @return  username of this user
     */
    @Override
    public String getName() {
        return username;
    }

    /**
     * Returns authorities of this {@link UserEntity} object.
     *
     * @return  {@link Collection} of this user's roles as {@link GrantedAuthority}
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Returns password of this {@link UserEntity} object.
     *
     * @return  password of this user
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns username of this {@link UserEntity} object.
     *
     * @return  username of this user
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Method required by {@link UserDetails}.<br>
     * Accounts don't expire.
     *
     * @return  <code>true</code>
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Informs if this user account is not locked.
     *
     * @return  information if account is not locked -
     *          <code>true</code> if not locked, <code>false</code> if locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    /**
     * Method required by {@link UserDetails}.<br>
     * Credentials don't expire.
     *
     * @return  <code>true</code>
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Informs if this user account is enabled.
     *
     * @return  information if account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
