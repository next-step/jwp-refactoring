package kitchenpos.menu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
