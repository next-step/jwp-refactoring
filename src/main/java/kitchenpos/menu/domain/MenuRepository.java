package kitchenpos.menu.domain;

import java.util.List;
import java.util.Optional;

public interface MenuRepository {
	List<Menu> findAll();

	Menu save(Menu menu);

	Optional<Menu> findById(Long id);

	List<Menu> findAllByIdIn(List<Long> ids);
}
