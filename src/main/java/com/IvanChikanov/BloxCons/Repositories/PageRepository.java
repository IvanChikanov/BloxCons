package com.IvanChikanov.BloxCons.Repositories;

import com.IvanChikanov.BloxCons.Models.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    @Query("SELECT p.id, p.name FROM Page p WHERE (:finded IS NULL OR p.name LIKE %:finded%) ORDER BY p.id DESC LIMIT 100")
    public List<String[]> getPageList(@Param("finded") String finded);
}
