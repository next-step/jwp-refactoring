package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT COUNT(m) FROM Menu m WHERE m.id in :ids")
    Integer countByIdIn(@Param("ids") List<Long> ids);
}