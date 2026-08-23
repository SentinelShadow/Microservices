package com.microservice.practice.accounts.service;

import com.microservice.practice.accounts.dto.CustomerDto;

public interface IAccountsService {

    /**
     *
     * @param customerDto -- CustomerDto object
     */
    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto
     * @return boolean indicating if update is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber
     * @return boolean indicating if delete is successful or not
     */
    boolean deleteAccount(String mobileNumber);
}
