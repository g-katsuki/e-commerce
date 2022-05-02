package jp.co.rakuten.ecommerce.api.service;

import jp.co.rakuten.ecommerce.api.entity.Customer;
import jp.co.rakuten.ecommerce.api.entity.Item;
import jp.co.rakuten.ecommerce.api.repository.CustomerRepository;
import jp.co.rakuten.ecommerce.common.dto.IdDto;
import jp.co.rakuten.ecommerce.common.dto.ItemDto;
import jp.co.rakuten.ecommerce.common.dto.MessageDto;
import jp.co.rakuten.ecommerce.common.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private CustomerRepository customerRepository;

    @Transactional  // データベースに書き込む場合につける?
    public IdDto createUser(UserDto userDto) {
        Customer customer = makeCustomerEntity(userDto);
        customer = customerRepository.save(customer);
        return IdDto.builder().id(customer.getId()).build();
    }


    @Transactional  // ユーザーデータ更新
    public MessageDto updateUser(UserDto userDto) {
        // entityの生成
        Customer user = new Customer();
        user = makeCustomerEntity(userDto);
        user.setPoint(userDto.getPoint());
        customerRepository.save(user);
        return MessageDto.builder().message("Success").build();
    }


    public UserDto getUserByEmail(String email) {
        System.out.println("getemail");
        List<Customer> customers = customerRepository.findByEmail(email);
        if (CollectionUtils.isEmpty(customers)) return null;
        return makeUserDto(customers.get(0));
    }


    private UserDto makeUserDto(Customer customer) {
        return UserDto.builder().id(customer.getId())
                .email(customer.getEmail())
                .password(customer.getPassword())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .zipCode(customer.getZipCode())
                .address(customer.getAddress())
                .phone(customer.getPhone())
                .enabled(customer.isEnabled())
                .createdAt(customer.getCreatedAt())
                .point(customer.getPoint())
                .build();
    }

    private Customer makeCustomerEntity(UserDto userDto) {
        Customer customer = new Customer();
        customer.setId(userDto.getId());
        customer.setEmail(userDto.getEmail());
        customer.setPassword(userDto.getPassword());
        customer.setFirstName(userDto.getFirstName());
        customer.setLastName(userDto.getLastName());
        customer.setZipCode(userDto.getZipCode());
        customer.setAddress(userDto.getAddress());
        customer.setPhone(userDto.getPhone());
        customer.setEnabled(userDto.isEnabled());
        customer.setCreatedAt(userDto.getCreatedAt());
        customer.setPoint(userDto.getPoint());
        return customer;
    }
}