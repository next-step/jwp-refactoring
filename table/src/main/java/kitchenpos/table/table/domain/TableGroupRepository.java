package kitchenpos.table.table.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.common.exception.NotFoundException;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {
	default TableGroup tableGroup(long id) {
		return findById(id).orElseThrow(
			() -> new NotFoundException(String.format("테이블 그룹 id(%d)를 찾을 수 없습니다.", id)));
	}
}
