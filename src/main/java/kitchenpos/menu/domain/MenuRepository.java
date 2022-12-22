package kitchenpos.menu.domain;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    int countByIdIn(Collection<Long> ids);

}
