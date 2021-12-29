package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.*;

import kitchenpos.menu.domain.*;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
