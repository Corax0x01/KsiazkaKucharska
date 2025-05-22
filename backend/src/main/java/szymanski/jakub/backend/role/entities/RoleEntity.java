package szymanski.jakub.backend.role.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import szymanski.jakub.backend.common.BaseEntity;
import szymanski.jakub.backend.user.entities.UserEntity;

import java.util.List;

/**
 * Role entity stored in database.
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@EqualsAndHashCode(callSuper = false)
public class RoleEntity extends BaseEntity {

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<UserEntity> users;
}
