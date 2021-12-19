package kitchenpos.domain.menu_group.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends MenuGroupRepository, JpaRepository<MenuGroup, Long> {
}
