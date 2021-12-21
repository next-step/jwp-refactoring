package kitchenpos.menugroup.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;

public interface JpaMenuGroupRepository extends MenuGroupRepository, JpaRepository<MenuGroup, Long> {
}
