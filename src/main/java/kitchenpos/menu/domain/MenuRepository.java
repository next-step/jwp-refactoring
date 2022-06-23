package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    @Query(value = "select m from MenuEntity m "
        + "join fetch m.menuProducts")
    List<MenuEntity> findAllMenuAndProducts();
}
