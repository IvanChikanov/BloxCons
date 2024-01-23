package com.IvanChikanov.BloxCons.Repositories;

import com.IvanChikanov.BloxCons.Models.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {

    @Query("SELECT p.id, p.name FROM Page p")
    public List<String[]> getPageList();
}
