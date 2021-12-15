package kitchenpos.table.domain;

import kitchenpos.common.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup tableGroup(long id) {
        return findById(id).orElseThrow(
            () -> new NotFoundException(String.format("단체 지정된 그룹 id(%d)를 찾을 수 없습니다.", id)));
    }
}
