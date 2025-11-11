package dev.semeshin.snapadmin.auth.models;

import dev.semeshin.snapadmin.auth.models.id.ClassId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@IdClass(ClassId.class)
public class EntityWithIdClass {
    @Id
    private UUID idFirst;

    @Id
    private String idSecond;

    private String description;
}
