package com.internship.matcher.repository;

import com.internship.matcher.model.entity.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InternshipRepository extends JpaRepository<Internship, Long> {

    List<Internship> findByActiveTrue();

    @Query("SELECT i FROM Internship i WHERE i.active = true AND " +
           "(:location IS NULL OR LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:sector IS NULL OR LOWER(i.sector) LIKE LOWER(CONCAT('%', :sector, '%'))) AND " +
           "(:keyword IS NULL OR LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(i.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Internship> search(@Param("location") String location,
                             @Param("sector") String sector,
                             @Param("keyword") String keyword);
}
