package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m join fetch m.menuProducts.elements")
    List<Menu> findAllWithMenuProducts();
}
