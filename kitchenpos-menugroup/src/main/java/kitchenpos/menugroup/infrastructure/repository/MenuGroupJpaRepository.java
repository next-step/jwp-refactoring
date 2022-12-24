package kitchenpos.menugroup.infrastructure.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupJpaRepository extends JpaRepository<MenuGroup, Long> {
}
