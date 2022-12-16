package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query(value = "select m from Menu m join fetch m.menuProducts.menuProducts")
    List<Menu> findAllWithMenuProducts();

    long countByIdIn(List<Long> menuIds);
}
