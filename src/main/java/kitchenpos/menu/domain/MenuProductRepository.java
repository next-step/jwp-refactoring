package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuProductRepository extends JpaRepository<MenuProductEntity, Long> {

    @Query(value = "select m from MenuProductEntity m "
        + "join fetch m.product "
        + "where m.seq in :menuProductIds")
    List<MenuProductEntity> findByIdIn(List<Long> menuProductIds);
}
