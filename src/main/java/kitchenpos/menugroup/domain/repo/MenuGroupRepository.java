package kitchenpos.menugroup.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menugroup.domain.domain.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
