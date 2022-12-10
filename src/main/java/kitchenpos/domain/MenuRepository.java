package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    long countByIdIn(List<Long> ids); // TODO 추후 필요하지 않으면 지워야 함
}
