package kitchenpos.tablegroup.application;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@InjectMocks
	private TableGroupService tableGroupService;

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private TableGroupRepository tableGroupRepository;

	@Test
	void create() {
		final List<OrderTable> orderTables = Arrays.asList(
			orderTable(1L, null, 4, true),
			orderTable(2L, null, 6, true)
		);
		final TableGroup tableGroup = tableGroup(1L, orderTables);

		given(orderTableRepository.findAllById(any())).willReturn(orderTables);
		given(tableGroupRepository.save(any())).willReturn(tableGroup);
		given(orderTableRepository.save(any()))
			.willReturn(orderTable(1L, tableGroup, 4, false))
			.willReturn(orderTable(2L, tableGroup, 6, false));

		final TableGroup createdTableGroup = tableGroupService.create(tableGroup(null, orderTables));

		assertThat(createdTableGroup.getId()).isNotNull();
		assertThat(createdTableGroup.getOrderTables().size()).isEqualTo(2);
		createdTableGroup.getOrderTables()
			.forEach(orderTable -> {
				assertThat(orderTable.getTableGroup().getId()).isEqualTo(createdTableGroup.getId());
				assertThat(orderTable.isEmpty()).isFalse();
			});
	}

	@Test
	void create_invalid_order_tables_size() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup(null, null)));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup(null, Collections.emptyList())));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup(null,
				Arrays.asList(orderTable(1L, null, 4, true))
			)));
	}

	@Test
	void create_not_found_order_table() {
		given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(
			orderTable(1L, null, 3, true)
		));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup(null, Arrays.asList(
				orderTable(1L, null, 3, true),
				orderTable(2L, null, 2, true)
			))));
	}

	@Test
	void create_not_empty_order_table() {
		final List<OrderTable> orderTables = Arrays.asList(
			orderTable(1L, null, 3, false),
			orderTable(2L, null, 2, true)
		);
		given(orderTableRepository.findAllById(any())).willReturn(orderTables);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup(null, orderTables)));
	}

	@Test
	void create_order_table_having_table_group_id_already() {
		final TableGroup tableGroup = tableGroup(1L, null);
		final List<OrderTable> orderTables = Arrays.asList(
			orderTable(1L, tableGroup, 4, true),
			orderTable(2L, null, 4, true)
		);
		given(orderTableRepository.findAllById(any())).willReturn(orderTables);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup(null, orderTables)));
	}

	@Test
	void ungroup() {
		final TableGroup tableGroup = tableGroup(1L, null);
		final OrderTable orderTable1 = orderTable(1L, tableGroup, 4, false);
		final OrderTable orderTable2 = orderTable(2L, tableGroup, 4, false);

		given(orderTableRepository.findAllByTableGroup_Id(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
		given(orderRepository.existsByOrderTable_IdInAndOrderStatusIn(any(), anyList())).willReturn(false);

		tableGroupService.ungroup(tableGroup.getId());

		verify(orderTableRepository, times(2)).save(any(OrderTable.class));
		assertThat(orderTable1.getTableGroup().getId()).isNull();
		assertThat(orderTable2.getTableGroup().getId()).isNull();
	}

	@Test
	void ungroup_order_table_status_cooking_or_meal() {
		final TableGroup tableGroup = tableGroup(1L, null);
		final OrderTable orderTable1 = orderTable(1L, tableGroup, 4, false);
		final OrderTable orderTable2 = orderTable(2L, tableGroup, 4, false);

		given(orderTableRepository.findAllByTableGroup_Id(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
		given(orderRepository.existsByOrderTable_IdInAndOrderStatusIn(any(), anyList())).willReturn(true);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
	}
}
