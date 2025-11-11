package dev.semeshin.snapadmin.auth.models.id;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClassId implements Serializable {
    private Integer idFirst;
    private Integer idSecond;
}
