package kichenpos.menu.infra;

import kichenpos.menu.domain.Product;
import kichenpos.menu.domain.ProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long>, ProductRepository {
}
