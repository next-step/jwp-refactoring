package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    @Query(value = "select m from MenuProduct m "
        + "join fetch m.product "
        + "where m.seq in :menuProductIds")
    List<MenuProduct> findByIdIn(List<Long> menuProductIds);
}
