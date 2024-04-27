package com.bvp.task.user.repository;

import com.bvp.task.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    Optional<List<User>> findByDateOfBirthBetween(LocalDate fromDate, LocalDate toDate);

    Boolean existsByEmailAndDeletedFalse(String email);
}
