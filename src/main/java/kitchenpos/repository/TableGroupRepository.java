package kitchenpos.repository;

import kitchenpos.domain.TableGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroupEntity, Long> {
}
