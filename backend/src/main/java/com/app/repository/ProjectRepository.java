package com.app.repository;

import com.app.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

}