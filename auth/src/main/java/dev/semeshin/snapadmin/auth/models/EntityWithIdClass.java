package dev.semeshin.snapadmin.auth.models;

import dev.semeshin.snapadmin.auth.models.id.ClassId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(ClassId.class)
public class EntityWithIdClass {
    @Id
    private Integer idFirst;

    @Id
    private Integer idSecond;

    private String description;
}
