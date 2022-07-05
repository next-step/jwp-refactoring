package kitchenpos.menus.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
