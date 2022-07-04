package kitchenpos.dao;

import kitchenpos.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

}
