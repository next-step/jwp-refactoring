package kitchenpos.menu.domain.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
	long countByIdIn(List<Long> ids);
}
