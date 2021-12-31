package kitchenpos.infra.menu;

import kitchenpos.core.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {
    @Query("select count(m) from Menu m where m.id in (:ids)")
    int countByIdIn(@Param("ids") List<Long> menuIds);
}
