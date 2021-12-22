package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDao extends JpaRepository<Menu, Long> {

    Long countByIdIn(List<Long> menuIds);
}
