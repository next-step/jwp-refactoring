package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.MenuV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuV2, Long> {
    Long countByIdIn(List<Long> ids);
}
