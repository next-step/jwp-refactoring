package kitchenpos.table.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.table.domain.TableGroup;

public interface TableGroupDao extends JpaRepository<TableGroup, Long> {
}
