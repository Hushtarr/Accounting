package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) = 1 THEN TRUE ELSE FALSE END FROM User u WHERE u.role.description = 'Admin' AND u.company.id = :companyId")
    boolean isOnlyAdminInCompany(@Param("companyId") Long companyId);

    List<User> findByCompany_Id(Long companyId); // Add this method
    List<User> findAllByRoleDescription(String roleDescription);

}
