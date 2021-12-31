package kitchenpos.table.domain;

import kitchenpos.exception.NotFoundTableGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
    default TableGroup findByIdElseThrow(Long id) {
        return this.findById(id)
                .orElseThrow(NotFoundTableGroupException::new);
    }

    int countById(Long id);
}
