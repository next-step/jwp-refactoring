package kitchenpos.menu.domain;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface MenuRepository extends JpaRepository<Menu, Long> {
	int countByIdIn(List<Long> menuIds);

	List<Menu> findAllByIdIn(List<Long> menuIds);
}
