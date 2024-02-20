package com.IvanChikanov.BloxCons.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cell implements Comparable<Cell>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Grid grid;

    private int number;
    private String moduleLink;

    @OneToMany(mappedBy = "cell", cascade = {CascadeType.PERSIST, CascadeType.ALL})
    private Set<ModuleUnit> moduleUnits;

    public Cell(Grid grid, int num)
    {
        this.grid = grid;
        this.number = num;
        this.moduleLink = null;
    }

    public void AddUnit(ModuleUnit moduleUnit){
        this.moduleUnits.add(moduleUnit);
    }
    @Override
    public int compareTo(Cell o) {
        if(this.getNumber() < o.getNumber()) {
            return -1;
        } else if (this.getNumber() == o.getNumber()) {
            return 0;
        } else {
            return 1;
        }
    }
}
