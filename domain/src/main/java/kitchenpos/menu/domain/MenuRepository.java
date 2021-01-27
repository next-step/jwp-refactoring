package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);

    List<Menu> findByIdIn(List<Long> ids);
}
