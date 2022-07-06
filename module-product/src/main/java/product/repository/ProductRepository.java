package product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
