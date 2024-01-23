package com.IvanChikanov.BloxCons.Repositories;

import com.IvanChikanov.BloxCons.Models.OtherModules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OtherModulesRepo extends JpaRepository<OtherModules, Long> {
    @Query("SELECT m.id, m.displayName FROM OtherModules m")
    public List<String[]> getOthersList();

}
