package kitchenpos.menugroup.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {
}
