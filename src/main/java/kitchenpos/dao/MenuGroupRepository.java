package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
