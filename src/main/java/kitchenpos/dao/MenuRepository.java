package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(List<Long> ids);
}
