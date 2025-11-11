package dev.semeshin.snapadmin.auth.models;

import dev.semeshin.snapadmin.auth.models.id.ClassId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class EntityWithEmbededIdClass {
    @EmbeddedId
    private ClassId id;
    private String description;
}
