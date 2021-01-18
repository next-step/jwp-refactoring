package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuDao extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids);
}
