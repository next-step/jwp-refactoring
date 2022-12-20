package kitchenpos.menu.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);

    @Query("select m from Menu m join fetch m.menuProducts.values join fetch m.menuGroup")
    List<Menu> findAll();
}
