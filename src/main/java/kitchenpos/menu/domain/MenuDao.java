package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDao extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);

    @Query("select distinct m from Menu m " +
            "inner join fetch m.menuGroup ")
    List<Menu> findAllWithMenuGroupFetchJoin();
}
