package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private TableGroupRepository tableGroupRepository;
	@Mock
	private OrderRepository orderRepository;
	@InjectMocks
	private TableGroupService tableGroupService;

	private List<OrderTable> 주문테이블;
	private OrderTable 일번테이블;
	private OrderTable 이번테이블;
	private List<Long> 주문테이블아이디목록;
	private List<String> OrderStatusList;
	private TableGroup 단체지정;

	@BeforeEach
	void setUp() {
		주문테이블 = new ArrayList<>();
		일번테이블 = new OrderTable(null, new NumberOfGuests(1), false);
		이번테이블 = new OrderTable(null, new NumberOfGuests(2), false);

		주문테이블.add(일번테이블);
		주문테이블.add(이번테이블);

		주문테이블아이디목록 = 주문테이블.stream().map(OrderTable::getId).collect(Collectors.toList());
		OrderStatusList = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

		LocalDateTime createdDate = LocalDateTime.of(2021, 7, 6, 0, 0, 0);
		단체지정 = new TableGroup(createdDate);
	}

	@DisplayName("단체 지정 해제 테스트")
	@Test
	void testUnGroup() {
		Long tableGroupId = 1L;

		when(tableGroupRepository.findById(tableGroupId)).thenReturn(Optional.of(단체지정));

		tableGroupService.ungroup(tableGroupId);

		List<TableGroup> actual = 단체지정.getOrderTables()
			.stream()
			.map(OrderTable::getTableGroup)
			.collect(Collectors.toList());
		Assertions.assertThat(actual.isEmpty()).isTrue();
	}

	@DisplayName("주문 테이블이 2테이블 이하인경우 단체지정 오류 발생")
	@Test
	void testOrderTablesEmpty() {
		List<Long> orderTableIds = new ArrayList<>();
		orderTableIds.add(1L);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(일번테이블));

		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroupRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 테이블이이 둘 이상이어야 단체지정을 할 수 있습니다.");
	}

	@DisplayName("저장 되어있는 주문 테이블이 테이블 그룹이 없으면 오류 발생")
	@Test
	void testOrderTableNotSetTableGroup() {
		List<Long> orderTableIds = new ArrayList<>();
		orderTableIds.add(1L);
		orderTableIds.add(2L);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(일번테이블));
		when(orderTableRepository.findById(2L)).thenReturn(Optional.of(이번테이블));

		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroupRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문테이블이 단체지정이 되어있거나, 비어있지 않은 테이블입니다.");
	}

	@DisplayName("단체 지정 테스트")
	@Test
	void testCreateTableGroup() {

		List<Long> orderTableIds = new ArrayList<>();
		orderTableIds.add(1L);
		orderTableIds.add(2L);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

		일번테이블.changeEmpty(true);
		이번테이블.changeEmpty(true);

		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(일번테이블));
		when(orderTableRepository.findById(2L)).thenReturn(Optional.of(이번테이블));
		when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(단체지정);

		TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

		assertThat(actual.getOrderTables()).containsExactlyElementsOf(단체지정.getOrderTables().stream().map(
			OrderTableResponse::of).collect(Collectors.toList()));
	}
}