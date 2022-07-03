package kitchenpos.menus.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsAllByIdIn(List<Long> menuIds);

    List<Menu> findMenusByIdIn(List<Long> menuIds);
}
