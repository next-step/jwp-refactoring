package kitchenpos.menu.domain.dao;

import kitchenpos.menu.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupDao {

    @Override
    boolean existsById(Long id);
}
