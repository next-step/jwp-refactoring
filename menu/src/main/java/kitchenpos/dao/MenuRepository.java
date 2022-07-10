package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.menuProducts")
    List<Menu> findAll();
    List<Menu> findAllByIdIn(List<Long> ids);
    boolean existsByMenuGroup(MenuGroup menuGroup);
}
