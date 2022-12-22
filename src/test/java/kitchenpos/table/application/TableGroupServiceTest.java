package kitchenpos.table.application;

import static kitchenpos.generator.OrderTableGenerator.*;
import static kitchenpos.generator.TableGroupGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.ui.request.TableGroupRequest;
import kitchenpos.table.ui.request.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.table.ui.response.TableGroupResponse;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	private TableGroupRepository tableGroupRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("단체 지정을 할 수 있다.")
	@Test
	void createTableGroupTest() {
		long 주문테이블1_아이디 = 1L;
		long 주문테이블2_아이디 = 2L;
		// given
		OrderTable 비어있는_다섯명_테이블 = 비어있는_다섯명_테이블();
		given(orderTableRepository.orderTable(주문테이블1_아이디)).willReturn(비어있는_다섯명_테이블);

		OrderTable 비어있는_두명_테이블 = 비어있는_두명_테이블();
		given(orderTableRepository.orderTable(주문테이블2_아이디)).willReturn(비어있는_두명_테이블);

		TableGroup 단체_지정 = 다섯명_두명_테이블그룹();
		given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_지정);

		TableGroupRequest tableGroupRequest = new TableGroupRequest(
			Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));

		// when
		TableGroupResponse response = tableGroupService.create(tableGroupRequest);

		// then
		verify(tableGroupRepository, only()).save(any(TableGroup.class));
		assertAll(
			() -> assertThat(단체_지정.orderTables().list()).hasSize(2),
			() -> assertThat(단체_지정.id()).isEqualTo(response.getId())
		);
	}

	@DisplayName("단체 지정을 할 주문 테이블은 2개 이상이어야 한다.")
	@Test
	void createTableGroupWithOneOrderTableTest() {
		// given
		TableGroupRequest tableGroupRequest = new TableGroupRequest(
			Collections.singletonList(new OrderTableIdRequest(1L)));

		// when
		Throwable throwable = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정을 할 주문 테이블은 모두 비어있어야 한다.")
	@Test
	void createTableGroupWithNotEmptyOrderTableTest() {
		// given
		long 주문테이블1_아이디 = 1L;
		long 주문테이블2_아이디 = 2L;
		// given
		OrderTable 비어있는_다섯명_테이블 = 비어있지_않은_5명_테이블();
		given(orderTableRepository.orderTable(주문테이블1_아이디)).willReturn(비어있는_다섯명_테이블);

		OrderTable 비어있는_두명_테이블 = 비어있는_두명_테이블();
		given(orderTableRepository.orderTable(주문테이블2_아이디)).willReturn(비어있는_두명_테이블);

		TableGroupRequest tableGroupRequest = new TableGroupRequest(
			Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));

		// when
		Throwable throwable = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정을 할 주문 테이블은 이미 단체 지정이 되어 있지 않아야 한다.")
	@Test
	void createTableGroupWithAlreadyGroupedOrderTableTest() {

		// given
		long 주문테이블1_아이디 = 1L;
		long 주문테이블2_아이디 = 2L;
		// given
		OrderTable 비어있는_다섯명_테이블 = 비어있지_않은_5명_테이블();
		given(orderTableRepository.orderTable(주문테이블1_아이디)).willReturn(비어있는_다섯명_테이블);

		OrderTable 그룹_지정된_테이블 = 다섯명_두명_테이블그룹().orderTables().list().get(0);
		given(orderTableRepository.orderTable(주문테이블2_아이디)).willReturn(그룹_지정된_테이블);

		TableGroupRequest tableGroupRequest = new TableGroupRequest(
			Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L)));

		// when
		Throwable throwable = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블을 단체 지정에서 제외할 수 있다.")
	@Test
	void ungroupTest() {
		// given
		long 단체_아이디 = 1L;
		TableGroup 다섯명_두명_테이블그룹 = 다섯명_두명_테이블그룹();
		given(tableGroupRepository.tableGroup(단체_아이디)).willReturn(다섯명_두명_테이블그룹);

		// when
		tableGroupService.ungroup(단체_아이디);

		// then
		assertThat(다섯명_두명_테이블그룹.orderTables().list().stream())
			.allMatch(
				orderTable -> orderTable.tableGroup() == null && orderTable.isEmpty()
			);
	}

	@DisplayName("주문 상태가 식사중, 조리중이면 테이블을 단체 지정에서 제외할 수 없다.")
	@Test
	void ungroupWithNotCompletionOrderTableTest() {
		// given
		long 단체_아이디 = 1L;
		TableGroup 다섯명_두명_테이블그룹 = 다섯명_두명_테이블그룹();
		given(tableGroupRepository.tableGroup(단체_아이디)).willReturn(다섯명_두명_테이블그룹);
		given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

		// when
		Throwable throwable = catchThrowable(() -> tableGroupService.ungroup(단체_아이디));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}
}
