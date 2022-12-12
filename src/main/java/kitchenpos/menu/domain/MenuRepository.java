package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    int countByIdIn(Collection<Long> ids);

}
