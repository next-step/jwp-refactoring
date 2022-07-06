package kitchenpos.common.menu.repository;

import kitchenpos.common.menu.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
