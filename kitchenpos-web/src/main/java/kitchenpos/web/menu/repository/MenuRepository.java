package kitchenpos.web.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.menu.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
