package kitchenpos.menu.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;

@Repository(value = "ToBeJpaMenuRepository")
public interface JpaMenuRepository extends MenuRepository, JpaRepository<Menu, Long> {
}
