package com.IvanChikanov.BloxCons.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ModuleUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Cell cell;

    @Column(columnDefinition = "TEXT")
    private String textContent = "{}";

    private String imageId;

    @Column(columnDefinition = "TEXT")
    private String otherJsonOption = "{}";

    private String tag;

}
