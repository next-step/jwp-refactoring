package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menuGroup.domain.MenuGroup;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
