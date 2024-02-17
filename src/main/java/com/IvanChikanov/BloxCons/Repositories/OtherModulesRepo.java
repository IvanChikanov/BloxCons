package com.IvanChikanov.BloxCons.Repositories;

import com.IvanChikanov.BloxCons.Models.OtherModules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface OtherModulesRepo extends JpaRepository<OtherModules, Long> {
    @Query("SELECT m.id, m.displayName FROM OtherModules m")
    public List<String[]> getOthersList();

    @Query("SELECT m FROM OtherModules m WHERE m.id IN :ids")
    public Set<OtherModules> getOtherModulesSet(@Param("ids") Collection<Long> ids);

    @Query("SELECT om FROM OtherModules om WHERE om.name = :name")
    public OtherModules findByName(@Param("name")String name);
}
