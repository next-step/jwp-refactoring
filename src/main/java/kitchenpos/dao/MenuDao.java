package kitchenpos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Menu;

public interface MenuDao extends JpaRepository<Menu, Long> {
	int countByIdIn(List<Long> menuIds);
}
