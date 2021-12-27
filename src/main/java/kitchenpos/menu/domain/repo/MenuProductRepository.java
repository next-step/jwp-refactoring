package kitchenpos.menu.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.domain.MenuProduct;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
