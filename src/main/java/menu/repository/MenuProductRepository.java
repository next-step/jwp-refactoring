package menu.repository;

import org.springframework.data.jpa.repository.*;

import menu.domain.*;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
}
