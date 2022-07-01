package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query(value = "select distinct m from Menu m "
        + "join fetch m.menuProducts.items item ")
    List<Menu> findAllMenuAndProducts();

    long countByIdIn(List<Long> ids);
}
