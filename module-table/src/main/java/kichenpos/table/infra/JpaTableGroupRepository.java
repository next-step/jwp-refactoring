package kichenpos.table.infra;

import kichenpos.table.domain.TableGroup;
import kichenpos.table.domain.TableGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long>, TableGroupRepository {
}
