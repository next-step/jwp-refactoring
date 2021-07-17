package kitchenpos.menuGroup.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuGroupRequest;

public interface MenuGroupRepository extends JpaRepository<MenuGroupRequest, Long> {
}
