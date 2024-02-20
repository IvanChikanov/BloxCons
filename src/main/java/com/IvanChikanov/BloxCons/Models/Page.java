package com.IvanChikanov.BloxCons.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String theme;
    private Boolean math;

    @OneToMany(mappedBy = "page", fetch = FetchType.EAGER)
    private SortedSet<Grid> grids = new TreeSet<>();

    public Page(String name)
    {
        this.name = name;
        this.theme = "system";
        this.math = false;
    }
    public void AddGrid(Grid grid)
    {
        this.grids.add(grid);
        grid.setPage(this);
        grid.setNumber(this.grids.size());
    }
    public void deleteGrid(Grid grid)
    {
        this.grids.remove(grid);
    }
}
