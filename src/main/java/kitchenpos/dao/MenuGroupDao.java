package kitchenpos.dao;

import kitchenpos.domain.menu.group.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {
}
