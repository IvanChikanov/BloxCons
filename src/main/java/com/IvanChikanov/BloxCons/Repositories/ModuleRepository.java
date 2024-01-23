package com.IvanChikanov.BloxCons.Repositories;

import com.IvanChikanov.BloxCons.Models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    @Query("SELECT m.id, m.displayName FROM Module m WHERE m.type='main'")
    public List<String[]> getListModule();

    public List<Module> findAllByIdIn(List<Long> ids);
}
