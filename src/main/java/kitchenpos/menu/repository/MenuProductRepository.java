package kitchenpos.menu.repository;

import kitchenpos.menu.domain.MenuProductV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuProductRepository extends JpaRepository<MenuProductV2, Long> {
}
