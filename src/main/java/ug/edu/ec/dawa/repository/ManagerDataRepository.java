package ug.edu.ec.dawa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ug.edu.ec.dawa.entity.ManagerData;

@Repository
public interface ManagerDataRepository extends JpaRepository<ManagerData,Integer> {
}
