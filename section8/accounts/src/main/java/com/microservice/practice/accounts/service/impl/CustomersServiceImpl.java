package com.microservice.practice.accounts.service.impl;

import com.microservice.practice.accounts.dto.AccountsDto;
import com.microservice.practice.accounts.dto.CardsDto;
import com.microservice.practice.accounts.dto.CustomerDetailsDto;
import com.microservice.practice.accounts.dto.LoansDto;
import com.microservice.practice.accounts.entity.Accounts;
import com.microservice.practice.accounts.entity.Customer;
import com.microservice.practice.accounts.exception.ResourceNotFoundException;
import com.microservice.practice.accounts.mapper.AccountsMapper;
import com.microservice.practice.accounts.mapper.CustomerMapper;
import com.microservice.practice.accounts.repository.AccountsRepository;
import com.microservice.practice.accounts.repository.CustomerRepository;
import com.microservice.practice.accounts.service.ICustomersService;
import com.microservice.practice.accounts.service.client.CardsFeignClient;
import com.microservice.practice.accounts.service.client.LoanFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsClient;
    private LoanFeignClient loansClient;


    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
