package kitchenpos.menu.domain.repository;

import java.util.List;
import kitchenpos.menu.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    int countByIdIn(List<Long> menuIds);
}
