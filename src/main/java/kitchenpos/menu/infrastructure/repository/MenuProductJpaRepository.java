package kitchenpos.menu.infrastructure.repository;

import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductJpaRepository extends JpaRepository<MenuProduct, Long> {
}
