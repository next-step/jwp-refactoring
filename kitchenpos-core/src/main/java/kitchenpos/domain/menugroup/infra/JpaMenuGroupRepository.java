package kitchenpos.domain.menugroup.infra;

import kitchenpos.domain.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
