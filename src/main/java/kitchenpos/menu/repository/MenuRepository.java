package kitchenpos.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.menu.domain.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
	int countByIdIn(List<Long> menuIds);

}
