package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query("select u.username from UserEntity u")
    List<String> getAllUsernames();

    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    //    @Modifying
    //    @Query("update UserEntity u set u.password = :newPassword where u.username =:username")
    //    UserEntity updatePassword(String username, String newPassword);
    //
    //    @Query("delete from UserEntity u where u.username = :username")
    //    void deleteByUsername(String username);

}
