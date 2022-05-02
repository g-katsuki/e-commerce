package jp.co.rakuten.ecommerce.api.repository;

import jp.co.rakuten.ecommerce.api.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {  // RepositoryはDBを繋ぐもの

    List<Customer> findByEmail(String email);
}
