package kitchenpos.menuGroup.dao;

import kitchenpos.menuGroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
