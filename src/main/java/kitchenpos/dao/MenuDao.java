package kitchenpos.dao;

import kitchenpos.domain.menu.Menu;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDao extends JpaRepository<Menu, Long> {

    long countByIdIn(List<Long> ids);
}
