package kitchenpos.menu.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("select distinct m from Menu m join fetch m.menuProducts")
    List<Menu> findAllWithMenuProduct();
}
