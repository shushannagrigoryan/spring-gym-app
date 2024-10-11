package org.example.repository;

import java.util.List;
import java.util.Optional;
import org.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query("select u.username from UserEntity u")
    List<String> findAllUsernames();



    Optional<UserEntity> findByUsernameAndPassword(String username, String password);

    @Modifying
    @Query("update UserEntity u set u.password = :newPassword where u.username =:username")
    void updatePassword(@Param("username") String username, @Param("newPassword") String newPassword);

    //
    //    @Query("delete from UserEntity u where u.username = :username")
    //    void deleteByUsername(String username);

    //UserEntity updateByUsername(String username);


    //    @Modifying
    //    @Query("update UserEntity u set u.password =: password where u.username =: username")
    //    int updatePasswordByUsername(String password, String username);

}
