package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);
}
