package kitchenpos.menugroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menugroup.domain.MenuGroup;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {
}
