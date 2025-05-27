package com.greenacademy.productstore.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.greenacademy.productstore.models.Voucher;

public interface VoucherRepository extends CrudRepository<Voucher, Integer> {

    Page<Voucher> findAll(Pageable pageable);
    Optional<Voucher> findById(Integer id);

    Voucher getByCode(String code);
    
}
