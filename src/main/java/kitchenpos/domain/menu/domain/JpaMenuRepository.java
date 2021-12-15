package kitchenpos.domain.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends MenuRepository, JpaRepository<Menu, Long> {
}
