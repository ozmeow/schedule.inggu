package ru.wzrdmhm.schedule_inggu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wzrdmhm.schedule_inggu.model.entity.Group;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    List<Group> findAllByOrderByCode();

    Optional<Group> findById(String groupCode);

    Optional<Group> findByCode(String groupCode);

}

