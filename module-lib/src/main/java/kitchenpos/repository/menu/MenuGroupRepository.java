package kitchenpos.repository.menu;

import kitchenpos.domain.menu.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
