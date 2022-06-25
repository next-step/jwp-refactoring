package kitchenpos.menu.infrastructure;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    int countByIdIn(List<Long> ids);
}
