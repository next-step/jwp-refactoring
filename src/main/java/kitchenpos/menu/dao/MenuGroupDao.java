package kitchenpos.menu.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.MenuGroup;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {
    MenuGroup save(MenuGroup entity);

    Optional<MenuGroup> findById(Long id);

    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
