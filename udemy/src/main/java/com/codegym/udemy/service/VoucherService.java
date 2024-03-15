package com.codegym.udemy.service;

import com.codegym.udemy.dto.VoucherDto;

public interface VoucherService {
    void saveVoucher(Long userId, VoucherDto voucherDto);

    VoucherDto getVoucherById(Long voucherId);
    void editVoucher(Long voucherId, VoucherDto voucherDto);

    boolean deleteVoucher(Long voucherId);
}
