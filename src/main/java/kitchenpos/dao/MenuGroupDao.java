package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {

    boolean existsById(Long id);
}
