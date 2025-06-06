package com.nor2code.springboot.thymeleafdemo.dao;

import com.nor2code.springboot.thymeleafdemo.entity.Withdrawals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WithdrawalsRepository extends JpaRepository<Withdrawals, String> {

    @Query("SELECT SUM(w.amount) FROM Withdrawals w WHERE w.members.user_id = :userId AND DATE(w.createdAt) = CURRENT_DATE")
    Integer getTotalWithdrawalsForToday(@Param("userId") String userId);
}
