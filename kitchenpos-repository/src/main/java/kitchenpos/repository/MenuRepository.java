package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kitchenpos.domain.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
	int countByIdIn(List<Long> menuIds);

	@Query(value = "select distinct m from Menu m" +
		" join fetch m.menuGroup mg" +
		" left join fetch m.menuProducts.menuProducts mp" +
		" left join fetch mp.product p"
	)
	List<Menu> findAllFetch();
}
