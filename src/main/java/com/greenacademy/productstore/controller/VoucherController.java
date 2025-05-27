package com.greenacademy.productstore.controller;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.greenacademy.productstore.models.Voucher;
import com.greenacademy.productstore.services.VoucherServices;

@Controller
public class VoucherController {

    private VoucherServices voucherServices;

    public VoucherController(VoucherServices voucherServices){
        this.voucherServices = voucherServices;
    }

    @GetMapping("/admin/vouchers")
    public String index(Model model, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);
        PagedModel<Voucher> vouchers = new PagedModel<>(voucherServices.getAll(pageable));

        model.addAttribute("vouchers", vouchers);
        model.addAttribute("metadata", vouchers.getMetadata());
        return "pages/vouchers/index";
    }

    @GetMapping("/admin/vouchers/create")
    public String create(Model model) {
        model.addAttribute("voucher", new Voucher());
        return "pages/vouchers/create";
    }
    @PostMapping("/admin/vouchers")
    public String store(@ModelAttribute("voucher") Voucher voucher) {
        voucherServices.create(voucher);
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/admin/vouchers/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, Voucher voucher) {
        Optional<Voucher> voucherOptional = voucherServices.getById(id);
        model.addAttribute("voucher", voucherOptional.get());
        return "pages/vouchers/edit";
    }

    @PostMapping("/admin/vouchers/{id}")
    public String update(@PathVariable("id") Integer id, @ModelAttribute("voucher") Voucher voucher) {
        voucherServices.update(voucher, id);
        return "redirect:/admin/vouchers";
    }

    @PostMapping("/admin/vouchers/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Optional<Voucher> voucher = voucherServices.getById(id);
        voucherServices.delete(voucher.get());
        return "redirect:/admin/vouchers";
    }
}
