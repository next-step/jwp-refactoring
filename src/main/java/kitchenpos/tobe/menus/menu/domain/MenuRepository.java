package kitchenpos.tobe.menus.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findAllByIdIn(final List<Long> menuIds);
}
