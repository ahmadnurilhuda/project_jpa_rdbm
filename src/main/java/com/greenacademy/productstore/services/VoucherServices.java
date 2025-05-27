package com.greenacademy.productstore.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.Voucher;
import com.greenacademy.productstore.repositories.VoucherRepository;

@Service
public class VoucherServices {

    private VoucherRepository voucherRepository;

    public VoucherServices(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public Page<Voucher> getAll(Pageable pageable) {
        return voucherRepository.findAll(pageable);
    }

    public Optional<Voucher> getById(Integer id) {
        return voucherRepository.findById(id);
    }

    public Voucher create(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public Voucher update(Voucher voucher, Integer id) {
        Voucher ExistingVoucher = voucherRepository.findById(id).get();

        ExistingVoucher.setName(voucher.getName());
        if (!voucher.getCode().equals(ExistingVoucher.getCode())) {
            ExistingVoucher.setCode(voucher.getCode());
        }
        ExistingVoucher.setDiscount(voucher.getDiscount());
        ExistingVoucher.setDiscountType(voucher.getDiscountType());
        ExistingVoucher.setQuantity(voucher.getQuantity());

        return voucherRepository.save(ExistingVoucher);
    }

    public void delete(Voucher voucher) {
        voucherRepository.delete(voucher);
    }

    public Voucher getByCode(String code) {
        return voucherRepository.getByCode(code);
    }

    public BigDecimal calculateDiscount(String code, BigDecimal totalPrice) throws Exception {

        if (code == null || code.isEmpty()) {
            return BigDecimal.ZERO;

        }

        Voucher voucher = voucherRepository.getByCode(code);
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher not found");
        }

        if (voucher.getQuantity() == 0) {
            throw new IllegalArgumentException("Voucher is out of stock");
        }

        if (voucher.getMinPurchase() != null && voucher.getMinPurchase().compareTo(BigDecimal.ZERO) > 0
                && totalPrice.compareTo(voucher.getMinPurchase()) < 0) {
            throw new IllegalArgumentException("Minimum purchase not met");
        }

        if (voucher.getDiscountType().equals("FIXED")) {
            if (voucher.getDiscount().compareTo(totalPrice) > 0) {
                return totalPrice;
            }
            return voucher.getDiscount();
        }
        BigDecimal discount = voucher.getDiscount().divide(BigDecimal.valueOf(100));
        BigDecimal discountAmount = totalPrice.multiply(discount);
        return discountAmount;
    }

    public void useVoucher(String code) {
        Voucher voucher = voucherRepository.getByCode(code);
        voucher.setQuantity(voucher.getQuantity() - 1);
        voucherRepository.save(voucher);
    }
}
