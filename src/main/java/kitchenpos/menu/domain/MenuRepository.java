package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("select menu from Menu as menu join fetch menu.menuGroup join fetch menu.menuProducts as menuProduct join fetch menuProduct.product")
    List<Menu> findFetchJoinAll();
}
