package kitchenpos.menu.domain.dao;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuDao {

    @Override
    long countByIdIn(List<Long> ids);
}
