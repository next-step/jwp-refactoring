package kitchenpos.web.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.menu.domain.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    boolean existsById(Long id);
}
