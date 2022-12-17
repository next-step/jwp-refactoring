package kitchenpos.infrastructure.jpa.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);

    List<Menu> findAllByIdIn(List<Long> id);
}
