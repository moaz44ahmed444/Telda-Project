package com.telda.telda_project.repository;

import com.telda.telda_project.entity.Transaction;
import com.telda.telda_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findBySender(User sender);
    List<Transaction> findByReceiver(User receiver);
    List<Transaction> findBySenderAndReceiver(User sender, User receiver);

    @Query("select t from Transaction t where t.sender = :user OR t.receiver = :user")
    List<Transaction> findByUserInvolved(@Param("user") User user);
}
