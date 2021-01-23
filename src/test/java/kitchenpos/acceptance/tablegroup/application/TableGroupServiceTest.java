package kitchenpos.acceptance.tablegroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableService orderTableService;
	@Mock
	private TableGroupRepository tableGroupRepository;
	@InjectMocks
	private TableGroupService tableGroupService;

	private List<Long> orderTableIds;
	private List<OrderTable> orderTables;
	private TableGroup tableGroup;
	private OrderTables hasGroupOrderTables;

	@BeforeEach
	void setUp() {
		orderTableIds = Arrays.asList(1L, 2L, 3L);
		orderTables = Arrays.asList(
			OrderTable.of(1L, 0, true),
			OrderTable.of(2L, 0, true),
			OrderTable.of(3L, 0, true)
		);
		tableGroup = TableGroup.of();
		hasGroupOrderTables = OrderTables.of(Arrays.asList(
			OrderTable.of(1L, 0, true),
			OrderTable.of(2L, 0, true),
			OrderTable.of(3L, 0, true)
		));
		hasGroupOrderTables.createGroup(tableGroup);
	}

	@DisplayName("단체: 단체 생성 테스트")
	@Test
	void createTest() {
		// given
		TableGroupRequest request = TableGroupRequest.of(orderTableIds);
		given(orderTableService.findAllById(any())).willReturn(orderTables);
		given(tableGroupRepository.save(any())).willReturn(TableGroup.of());

		// when
		final TableGroupResponse actual = tableGroupService.create(request);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getOrderTables().stream()
				.filter(OrderTableResponse::isEmpty)
				.count()).isEqualTo(0)
		);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(1. 주문 테이블이 2개 이상이여야 한다.)")
	@Test
	void errorTableGroupForNullTable() {
		// given // when
		TableGroupRequest request = TableGroupRequest.of(Collections.singletonList(1L));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.create(request)
		);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(2. 주문 테이블이 모두 등록되 있어야 한다.)")
	@Test
	void createTableGroupForNotExistTable() {
		// given
		List<OrderTable> failOrderTables = Arrays.asList(                // 2개
			OrderTable.of(1L, 0, true),
			OrderTable.of(2L, 0, true));
		TableGroupRequest request = TableGroupRequest.of(orderTableIds); // 3개

		// when
		when(orderTableService.findAllById(request.getOrderTables())).thenReturn(failOrderTables);

		// then
		assertThatThrownBy(
			() -> tableGroupService.create(request)
		).isInstanceOf(EntityNotFoundException.class);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(3. 주문 테이블이 빈 테이블이여야 한다.)")
	@Test
	void createTableGroupForNotEmptyTable() {
		// given
		TableGroupRequest request = TableGroupRequest.of(orderTableIds);
		OrderTable notEmptyTable = OrderTable.of(1L, 5, false);

		// when
		when(orderTableService.findAllById(request.getOrderTables())).thenReturn(
			Arrays.asList(notEmptyTable, mock(OrderTable.class), mock(OrderTable.class)));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.create(request)
		);
	}

	@DisplayName("단체[예외]: 단체 생성 테스트(3. 주문 테이블이 그룹에 포함되지 않아야 한다.)")
	@Test
	void createTableGroupForAlreadyHaveGroup() {
		// given
		OrderTable existGroupTable = OrderTable.of(1L, 0, false);
		existGroupTable.createGroup(TableGroup.of());
		TableGroupRequest request = TableGroupRequest.of(orderTableIds);

		// when
		when(orderTableService.findAllById(request.getOrderTables())).thenReturn(
			Arrays.asList(existGroupTable, mock(OrderTable.class), mock(OrderTable.class)));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.create(request)
		);
	}

	@DisplayName("단체: 단체 해지 테스트")
	@Test
	void ungroup() {
		// given
		given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));
		given(orderTableService.findAllByTableGroup(tableGroup)).willReturn(hasGroupOrderTables.getOrderTables());
		//given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

		// when
		tableGroupService.ungroup(1L);

		// then
		assertThat(hasGroupOrderTables.stream()
			.filter(table -> table.getTableGroup() == null)
			.collect(Collectors.toList())).containsAll(hasGroupOrderTables.getOrderTables());
	}

	/*
	@DisplayName("단체[예외]: 단체 해지 테스트(주문 상태가 조리 또는 식사인 테이블은 해지가 불가능하다.)")
	@Test
	void ungroupCookingTable() {
		given(orderTableService.findAllByTableGroupId(any())).willReturn(orderTables);
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

		//when-then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableGroupService.ungroup(1L)
		);
	}*/
}
