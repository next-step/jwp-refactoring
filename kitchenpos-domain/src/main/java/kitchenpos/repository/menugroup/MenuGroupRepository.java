package kitchenpos.repository.menugroup;

import kitchenpos.application.menugroup.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}