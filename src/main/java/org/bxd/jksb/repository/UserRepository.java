package org.bxd.jksb.repository;

import org.bxd.jksb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
