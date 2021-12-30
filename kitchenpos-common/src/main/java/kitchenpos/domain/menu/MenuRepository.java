package kitchenpos.domain.menu;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT COUNT(m) FROM Menu m WHERE m.id in :ids")
    Integer countByIdIn(@Param("ids") List<Long> ids);
}
