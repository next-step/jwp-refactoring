package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.exception.TableGroupException;

@DisplayName("테이블 그룹(단체지정) : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	TableGroupValidator tableGroupValidator;

	@Mock
	TableGroupRepository tableGroupRepository;

	@Mock
	OrderTable orderTable;

	@Mock
	OrderTables orderTables;

	@Mock
	TableGroup tableGroup;

	@InjectMocks
	private TableGroupService tableGroupService;

	private TableGroupRequest tableGroupRequest;

	@DisplayName("테이블 그룹을 생성할 때 존재하지 않는 주문 테이블일 경우 예외처리 테스트")
	@Test
	void createTableGroupEmptyOrderTables() {
		// given
		tableGroupRequest = TableGroupRequest.of(Collections.emptyList());

		// when
		doThrow(new TableGroupException(ErrorCode.ORDER_TABLE_IS_NULL))
			.when(tableGroupValidator)
			.findValidatedOrderTables(tableGroupRequest.getOrderTables());

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroupRequest);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.ORDER_TABLE_IS_NULL.getMessage());
	}

	@DisplayName("테이블 그룹을 생성할 떄 주문 테이블의 갯수가 1개인 경우 예외처리 테스트")
	@Test
	void createTableGroupSizeOneOrderTableList() {
		// given
		tableGroupRequest = TableGroupRequest.of(Collections.singletonList(1L));

		// when
		doThrow(new TableGroupException(ErrorCode.NEED_MORE_ORDER_TABLES))
			.when(tableGroupValidator)
			.findValidatedOrderTables(tableGroupRequest.getOrderTables());

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroupRequest);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.NEED_MORE_ORDER_TABLES.getMessage());
	}

	@DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있는 상태인 경우 예외처리 테스트")
	@Test
	void createTableGroupEmptyOrderTable() {
		// given
		tableGroupRequest = TableGroupRequest.of(Arrays.asList(orderTable.getId(), orderTable.getId()));

		// when
		doThrow(new TableGroupException(ErrorCode.ORDER_TABLE_IS_EMPTY))
			.when(tableGroupValidator)
			.findValidatedOrderTables(tableGroupRequest.getOrderTables());

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroupRequest);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
	}

	@DisplayName("테이블 그룹을 생성하는 테스트")
	@Test
	void createTableGroup() {
		// given
		tableGroupRequest = TableGroupRequest.of(Arrays.asList(orderTable.getId(), orderTable.getId()));

		// when
		when(tableGroupValidator.findValidatedOrderTables(tableGroupRequest.getOrderTables()))
			.thenReturn(orderTables);
		when(tableGroupRepository.save(tableGroupRequest.toEntity())).thenReturn(tableGroup);

		// then
		assertThat(tableGroupService.create(tableGroupRequest)).isEqualTo(tableGroup);
	}
}
