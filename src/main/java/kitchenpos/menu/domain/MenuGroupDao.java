package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {
    boolean existsById(Long id);
}
