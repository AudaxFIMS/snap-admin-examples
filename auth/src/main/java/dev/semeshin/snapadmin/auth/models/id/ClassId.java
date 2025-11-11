package dev.semeshin.snapadmin.auth.models.id;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ClassId implements Serializable {
    private UUID idFirst;
    private String idSecond;
}
