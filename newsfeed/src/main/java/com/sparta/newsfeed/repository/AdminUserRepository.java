package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {
}
