package org.example.repositories;

import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    @Query("select username from UserEntity")
    List<String> findAllUsernames();

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    @Query("update UserEntity u set u.password = :password where u.username = :username")
    void updatePassword(@Param("username") String username, @Param("password") String password);



}
