package com.IvanChikanov.BloxCons.Repositories;

import com.IvanChikanov.BloxCons.Models.Cell;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CellRepository extends JpaRepository<Cell, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Cell c SET c.moduleLink = :mdl, c.number = :numb WHERE c.id = :id")
    public void updateCell(@Param("mdl") String module,@Param("numb") int number, @Param("id")Long id);
}
