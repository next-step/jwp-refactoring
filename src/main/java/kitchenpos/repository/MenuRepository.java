package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT COUNT(m.id) FROM Menu m WHERE m.id IN (:ids)")
    long countByIdIn(@Param("ids") List<Long> ids);
}
