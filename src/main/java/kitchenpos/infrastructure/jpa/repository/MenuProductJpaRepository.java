package kitchenpos.infrastructure.jpa.repository;

import kitchenpos.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuProductJpaRepository extends JpaRepository<MenuProduct, Long> {
}
