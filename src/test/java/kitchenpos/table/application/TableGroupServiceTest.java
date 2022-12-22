package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.TableGroupFixtures;
import kitchenpos.table.domain.GroupTablesValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.UnGroupTablesValidator;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	TableGroupRepository tableGroupRepository;

	@Mock
	OrderTableRepository orderTableRepository;

	@Mock
	GroupTablesValidator groupTablesValidator;

	@Mock
	UnGroupTablesValidator unGroupTablesValidator;

	@InjectMocks
	TableGroupService tableGroupService;

	@Test
	@DisplayName("테이블 그룹 생성 성공")
	void testCreateMenuGroup() {
		// given
		List<Long> orderTableId = Lists.newArrayList(1L, 2L);
		OrderTables orderTables = TableGroupFixtures.anEmptyOrderTables(2);

		when(orderTableRepository.findAllById(anyList())).thenReturn(orderTables.toList());
		when(tableGroupRepository.save(any(TableGroup.class))).thenAnswer(returnsFirstArg());

		// when
		TableGroup actualTableGroup = tableGroupService.group(orderTableId);

		// then
		verify(tableGroupRepository, times(1)).save(actualTableGroup);
		assertThat(actualTableGroup.getOrderTables().toList())
			.containsExactlyElementsOf(orderTables.toList());
	}

	@Test
	@DisplayName("테이블 그룹 해제에 성공")
	void testCreateMenuUnGroup() {
		OrderTables orderTables = TableGroupFixtures.aGroupedOrderTables();
		TableGroup savedTableGroup = createTableGroup(orderTables);

		when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(savedTableGroup));

		// when
		tableGroupService.ungroup(1L);

		// then
		assertThat(savedTableGroup.getOrderTables().toList())
			.filteredOn(OrderTable::hasTableGroup)
			.isEmpty();
	}

	private TableGroup createTableGroup(OrderTables orderTables) {
		return TableGroup.group(orderTables.toList(), groupTablesValidator);
	}
}
