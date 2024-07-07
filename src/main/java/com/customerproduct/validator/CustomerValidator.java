package com.customerproduct.validator;

import com.customerproduct.exception.BadRequestException;
import com.customerproduct.model.Customer;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class CustomerValidator {

    private static final String PHONE_NUMBER_PATTERN = "\\+?[0-9]{10,15}";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public void isPhoneNumberValid(String phoneNumber) throws BadRequestException {
        if (!Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber)) {
            throw new BadRequestException("Invalid phone number format");
        }
    }

    public void isEmailValid(String email) throws BadRequestException {
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new BadRequestException("Invalid email format");
        }
    }

    public void isValidCustomer(Customer customer) throws BadRequestException {
        if (customer.getClient() <= 0) {
            throw new BadRequestException("Invalid client ID");
        }
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }
        if (customer.getCustomerCode() == null || customer.getCustomerCode().trim().isEmpty()) {
            throw new BadRequestException("Customer code cannot be empty");
        }

        this.isPhoneNumberValid(customer.getPhoneNo());
        this.isEmailValid(customer.getEmail());
    }
}
