package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDao extends JpaRepository<Menu, Long> {

    long countByIdIn(List<Long> ids);
}
