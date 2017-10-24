package com.example.demo.Repository;

import com.example.demo.Model.Deposit;
import com.example.demo.Model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by snsoft on 27/9/2017.
 */
public interface Depositrepository extends JpaRepository<Deposit,Integer>,JpaSpecificationExecutor<Deposit> {
}
