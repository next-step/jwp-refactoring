package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * packageName : kitchenpos.menu.domain
 * fileName : MenuRepository
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query(value = "select distinct m from Menu m " +
            "left join fetch m.menuProducts mp ")
    List<Menu> findAllJoinFetch();
}
