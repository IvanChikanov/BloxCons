package com.IvanChikanov.BloxCons.Repositories;

import com.IvanChikanov.BloxCons.Models.Cell;
import com.IvanChikanov.BloxCons.Models.Image;
import com.IvanChikanov.BloxCons.Models.ModuleUnit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModuleUnitRepository extends JpaRepository<ModuleUnit, Long> {
    @Query("SELECT mu FROM ModuleUnit mu WHERE mu.cell = :cell_id")
    public List<ModuleUnit> getModels(@Param("cell_id") Cell cell_id);
    @Transactional
    @Modifying
    @Query("UPDATE ModuleUnit md SET md.imageId = :imgID, md.textContent = :text, " +
            "md.otherJsonOption = :other, md.tag = :tag WHERE md.id = :id")
    public void UpdateUnit(@Param("imgID") String imgID, @Param("text") String text,
                           @Param("other") String other, @Param("tag") String tag, @Param("id") Long id);


}
