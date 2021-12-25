package kitchenpos.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.menu.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select distinct m from Menu m "
        + "join fetch m.menuProducts.menuProducts mp")
    List<Menu> findMenus();
}
