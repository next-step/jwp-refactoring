package kitchenpos.menu.dao;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
//    @Query("select count(m.id) = size from Menu m where m.id in (:ids)")
//    boolean existsAllIdsWithSize(List<Long> ids, int size);
}
