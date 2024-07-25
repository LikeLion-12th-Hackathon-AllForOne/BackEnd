package com.likelion.allForOne.domain.tblUser;

import com.likelion.allForOne.entity.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<TblUser, Long> {
}
