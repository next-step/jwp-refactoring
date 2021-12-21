package kitchenpos.menu.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;

public interface JpaMenuRepository extends MenuRepository, JpaRepository<Menu, Long> {
}
