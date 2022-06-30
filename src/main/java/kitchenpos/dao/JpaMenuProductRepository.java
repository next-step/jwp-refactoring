package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuProductRepository extends JpaRepository<MenuProduct, Long>, MenuProductRepository {
}
