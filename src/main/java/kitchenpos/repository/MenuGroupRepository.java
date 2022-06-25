package kitchenpos.repository;

import kitchenpos.domain.MenuGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroupEntity, Long> {
}
