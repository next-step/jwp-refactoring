package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.menu.domain.MenuProduct;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
