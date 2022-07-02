package kitchenpos.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.NoSuchElementException;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    default TableGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new NoSuchElementException("테이블 그룹을 찾을 수 없습니다. id: " + id));
    }
}
