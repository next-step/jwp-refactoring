package kitchenpos.domain.menu;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @EntityGraph(attributePaths = {"menuProducts"})
    List<Menu> findAll();
    long countByIdIn(List<Long> ids);
}
