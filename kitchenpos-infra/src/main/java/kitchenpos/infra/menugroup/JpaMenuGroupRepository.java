package kitchenpos.infra.menugroup;

import kitchenpos.core.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
