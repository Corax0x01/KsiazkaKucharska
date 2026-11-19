package szymanski.jakub.backend.common;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue
    protected Long id;

    /**
     * Creation date.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    /**
     * Last modification date.
     */
    @LastModifiedDate
    @Column(insertable = false)
    protected LocalDateTime lastModifiedDate;
}
