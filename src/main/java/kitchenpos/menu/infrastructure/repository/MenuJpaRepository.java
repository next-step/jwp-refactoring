package kitchenpos.menu.infrastructure.repository;

import kitchenpos.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);

    List<Menu> findAllByIdIn(List<Long> id);
}
