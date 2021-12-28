package menu.repository;

import org.springframework.data.jpa.repository.*;

import menu.domain.*;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
