package kitchenpos.menu.dao;

import kitchenpos.menu.domain.MenuGroup;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {

    boolean existsById(Long id);
}
