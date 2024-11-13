package org.example.repositories;

import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("select username from UserEntity")
    List<String> findAllUsernames();

    @Query("select max(u.usernameIndex) from UserEntity u where u.username like concat(:firstName, '.', :lastName, '%')")
    Long findUsernameMaxIndex(@Param("firstName") String firstName, @Param("lastName") String lastName);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    Long countByActive(boolean active);

}
