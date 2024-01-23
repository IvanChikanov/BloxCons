package com.IvanChikanov.BloxCons.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Grid implements Comparable<Grid>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Page page;

    private int number;

    @OneToMany(mappedBy = "grid", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private SortedSet<Cell> cellArray = new TreeSet<>();

    public Grid(int rowCount)
    {

        for (int i = 1; i <= rowCount; i++)
        {
            cellArray.add(new Cell(this, i));
        }
    }
    public Cell addCell(Cell cell)
    {
        cell.setGrid(this);
        cell.setNumber(cellArray.size() + 1);
        cellArray.add(cell);
        return cell;
    }
    @Override
    public int compareTo(Grid o) {
        if(this.getNumber() < o.getNumber()) {
            return -1;
        } else if (this.getNumber() == o.getNumber()) {
            return 0;
        } else {
            return 1;
        }
    }
}
