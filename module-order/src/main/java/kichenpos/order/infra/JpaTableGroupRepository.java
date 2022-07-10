package kichenpos.order.infra;

import kichenpos.order.domain.TableGroup;
import kichenpos.order.domain.TableGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTableGroupRepository extends JpaRepository<TableGroup, Long>, TableGroupRepository {
}
