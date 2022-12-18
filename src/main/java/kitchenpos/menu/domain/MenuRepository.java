package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select distinct m " +
            "from Menu m " +
            "left join fetch m.menuGroup " +
            "left join fetch m.menuProducts.menuProductItems ")
    List<Menu> findAllWithGroupAndProducts();
}
