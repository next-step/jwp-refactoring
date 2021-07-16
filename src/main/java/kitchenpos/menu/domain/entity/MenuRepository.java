package kitchenpos.menu.domain.entity;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    int countByIdIn(List<Long> menuIds);
}
