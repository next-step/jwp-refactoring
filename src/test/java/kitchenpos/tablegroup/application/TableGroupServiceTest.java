package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.exception.TableGroupException;

@DisplayName("테이블 그룹(단체지정) : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	OrderTableRepository orderTableRepository;

	@Mock
	TableGroupRepository tableGroupRepository;

	@Mock
	OrderTable orderTable;

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

		// when // then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroupRequest);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.ORDER_TABLE_IS_NULL.getMessage());
	}

	@DisplayName("테이블 그룹을 생성할 떄 주문 테이블의 갯수가 1개인 경우 예외처리 테스트")
	@Test
	void createTableGroupSizeOneOrderTableList() {
		// given
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		tableGroupRequest = TableGroupRequest.of(Collections.singletonList(anyLong()));

		// when // then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroupRequest);
		}).isInstanceOf(TableGroupException.class)
			.hasMessageContaining(ErrorCode.NEED_MORE_ORDER_TABLES.getMessage());
	}

	@DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있는 상태인 경우 예외처리 테스트")
	@Test
	void createTableGroupEmptyOrderTable() {
		// given
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		tableGroupRequest = TableGroupRequest.of(Arrays.asList(orderTable.getId(), orderTable.getId()));

		// when
		when((orderTable.isEmpty())).thenReturn(false);

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
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		given(orderTable.isEmpty()).willReturn(true);
		tableGroupRequest = TableGroupRequest.of(Arrays.asList(orderTable.getId(), orderTable.getId()));

		// when
		when(tableGroupRepository.save(
			tableGroupRequest.toEntity(Arrays.asList(orderTable, orderTable)))).thenReturn(tableGroup);

		// then
		assertThat(tableGroupService.create(tableGroupRequest)).isEqualTo(tableGroup);
	}

	@DisplayName("테이블 그룹을 해체하는 기능 테스트")
	@Test
	void unGroup() {
		// given
		given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(tableGroup));
		given(orderTable.getTableGroup()).willReturn(null);

		// when
		tableGroupService.ungroup(tableGroup.getId());

		// then
		assertThat(orderTable.getTableGroup()).isNull();
	}
}
