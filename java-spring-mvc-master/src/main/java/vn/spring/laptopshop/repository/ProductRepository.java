package vn.spring.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.spring.laptopshop.domain.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  // Product save(Product spring);

  // List<Product> findOneByName(String name);

  // List<Product> findAll();

  // Product findById(long id);

  // void deleteById(long id);
}
