package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.MenuProduct;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
