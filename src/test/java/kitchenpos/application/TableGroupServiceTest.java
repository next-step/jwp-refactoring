package kitchenpos.application;

import static kitchenpos.exception.CannotCreateGroupTableException.TYPE.HAS_GROUP_TABLE;
import static kitchenpos.exception.CannotCreateGroupTableException.TYPE.INVALID_TABLE_COUNT;
import static kitchenpos.exception.CannotCreateGroupTableException.TYPE.NOT_EMPTY_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.exception.CannotCreateGroupTableException;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	TableGroupRepository tableGroupRepository;

	@Mock
	TableService tableService;

	@InjectMocks
	TableGroupService tableGroupService;


	@Test
	@DisplayName("테이블 그룹 생성")
	void testCreateMenuGroup() {
		// given
		List<Long> orderTableId = Lists.newArrayList(1L, 2L, 3L);
		List<OrderTable2> orderTables = createOrderTables(orderTableId, true);

		when(tableService.findAllById(anyList())).thenReturn(orderTables);
		when(tableGroupRepository.save(any(TableGroup2.class))).thenAnswer(returnsFirstArg());

		// when
		TableGroup2 actualTableGroup = tableGroupService.save(orderTableId);

		// then
		verify(tableGroupRepository, times(1)).save(actualTableGroup);
		assertThat(actualTableGroup.getOrderTables()).containsExactlyElementsOf(orderTables);
	}

	@Test
	@DisplayName("두개 미만의 주문 테이블로 테이블 그룹 생성")
	void testCreateMenuGroupWhenOrderTableSizeBelowThanTwo() {
		// given
		List<Long> orderTableId = Lists.newArrayList(1L);
		List<OrderTable2> orderTables = createOrderTables(orderTableId, true);

		when(tableService.findAllById(anyList())).thenReturn(orderTables);

		// when
		assertThatThrownBy(() -> tableGroupService.save(orderTableId))
			.isInstanceOf(CannotCreateGroupTableException.class)
				.hasMessage(INVALID_TABLE_COUNT.message);
	}

	@Test
	@DisplayName("빈 주문 테이블로 테이블 그룹 생성")
	void testCreateMenuGroupWhenOrderTableIsEmpty() {
		// given
		List<Long> orderTableId = Lists.newArrayList(1L, 2L, 3L);
		List<OrderTable2> orderTables = createOrderTables(orderTableId, false);

		when(tableService.findAllById(anyList())).thenReturn(orderTables);

		// when
		assertThatThrownBy(() -> tableGroupService.save(orderTableId))
			.isInstanceOf(CannotCreateGroupTableException.class)
			.hasMessage(NOT_EMPTY_ORDER_TABLE.message);
	}

	@Test
	@DisplayName("이미 테이블 그룹이 존재하는 테이블로 테이블 그룹 생성")
	void testCreateMenuGroupWhenOrderTableInAnotherGroup() {
		// given
		List<Long> orderTableId = Lists.newArrayList(1L, 2L, 3L);
		List<OrderTable2> orderTables = createOrderTables(orderTableId, true);
		createTableGroup(orderTables);

		when(tableService.findAllById(anyList())).thenReturn(orderTables);

		// when
		assertThatThrownBy(() -> tableGroupService.save(orderTableId))
			.isInstanceOf(CannotCreateGroupTableException.class)
			.hasMessage(HAS_GROUP_TABLE.message);
	}

	@Test
	@DisplayName("테이블 그룹 해제")
	void testCreateMenuUnGroup() {
		// given
		List<Long> orderTableId = Lists.newArrayList(1L, 2L, 3L);
		List<OrderTable2> orderTables = createOrderTables(orderTableId, true);
		TableGroup2 savedTableGroup = createTableGroup(orderTables);

		when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(savedTableGroup));

		// when
		tableGroupService.ungroup(1L);

		// then
		assertThat(savedTableGroup.getOrderTables())
			.filteredOn(OrderTable2::hasTableGroup)
			.isEmpty();
	}
	@Test
	@DisplayName("테이블 그룹 해제시 완료되지 않은 주문이 있을 경우")
	void testCreateMenuUnGroupWhenOrderStatusNotComplete() {
		// TODO
	}

	private TableGroup2 createTableGroup(List<OrderTable2> orderTables) {
		return new TableGroup2(orderTables);
	}

	private List<OrderTable2> createOrderTables(List<Long> orderTableIds, boolean isEmpty) {
		return orderTableIds.stream()
			.map(id -> getOrderTable(id, 1, isEmpty))
			.collect(Collectors.toList());
	}

	private static OrderTable2 getOrderTable(long id, int numberOfGuests, boolean isEmpty) {
		return new OrderTable2(id, numberOfGuests, isEmpty);
	}
}
