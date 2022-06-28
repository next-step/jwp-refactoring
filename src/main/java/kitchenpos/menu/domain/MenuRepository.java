package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
