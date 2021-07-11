package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	private OrderTableRepository orderTableRepository;
	@InjectMocks
	private TableService tableSevrice;

	private List<String> 주문상태목록;
	private OrderTable 일번테이블;
	private OrderTable 이번테이블;
	private OrderTable 삼번테이블;
	private OrderTable 사번테이블;

	private TableGroup 단체지정;

	@BeforeEach
	void setUp() {

		주문상태목록 = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
		일번테이블 = new OrderTable(null, null, true);
		이번테이블 = new OrderTable(null, null, true);
		삼번테이블 = new OrderTable(null, new NumberOfGuests(3), false);
		사번테이블 = new OrderTable(null, new NumberOfGuests(3), false);
		List<OrderTable> orderTableList = new ArrayList<>();
		orderTableList.add(일번테이블);
		orderTableList.add(이번테이블);
		단체지정 = new TableGroup(null, orderTableList);

	}

	@DisplayName("주문 테이블 생성을 확인")
	@Test
	void testCreateTable() {
		OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
		OrderTable entity = new OrderTable(null, new NumberOfGuests(orderTableRequest.getNumberOfGuests()),
			orderTableRequest.isEmpty());
		when(orderTableRepository.save(entity)).thenReturn(entity);

		OrderTableResponse actual = tableSevrice.create(orderTableRequest);
		assertThat(actual.getNumberOfGuests()).isEqualTo(entity.getNumberOfGuests().getCount());
	}

	@DisplayName("주문 테이블 목록 반환을 확인")
	@Test
	void testTableList() {
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.add(new OrderTable(null, new NumberOfGuests(3), false));
		orderTables.add(new OrderTable(null, new NumberOfGuests(2), false));

		when(orderTableRepository.findAll()).thenReturn(orderTables);

		List<OrderTableResponse> actual = tableSevrice.list();
		List<OrderTableResponse> expectedResponses = orderTables.stream().map(OrderTableResponse::of).collect(
			Collectors.toList());
		assertThat(actual).containsExactlyElementsOf(expectedResponses);
	}

	@DisplayName("주문 테이블을 비어있는 상태 변경 테스트")
	@Test
	void testChangeEmpty() {
		OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
		OrderTable savedOrderTable = new OrderTable(null, new NumberOfGuests(3), false);
		Long orderTableId = 1L;

		when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));

		OrderTableResponse actual = tableSevrice.changeEmpty(orderTableId, orderTableRequest);
		Assertions.assertThat(actual.isEmpty()).isTrue();
	}

	@DisplayName("주문 테이블이 없는경우 오류 발생")
	@Test
	void testChangeEmptyErrorNotFoundOrderTable() {
		OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
		Long orderTableId = 1L;
		when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeEmpty(orderTableId, orderTableRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 주문 테이블을 찾을 수 없습니다.");
	}

	@DisplayName("주문 테이블이 단체 지정 되어있는 경우 오류 발생")
	@Test
	void testAlreadyTableGroup() {
		OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
		Long orderTableId = 1L;

		when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(일번테이블));
		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeEmpty(orderTableId, orderTableRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("단체 지정되어있는 테이블은 변경할 수 없습니다.");
	}

	@DisplayName("주문 테이블의 상태가 COOKING, MEAL 인경우 오류 발생")
	@Test
	void testOrderTableStatusNotCompletion() {
		OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
		Long orderTableId = 1L;

		List<OrderLineItem> orderLineItems = new ArrayList<>();
		orderLineItems.add(new OrderLineItem(null, null, 3));
		new Order(삼번테이블, OrderStatus.COOKING, null, orderLineItems);

		when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(삼번테이블));

		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeEmpty(orderTableId, orderTableRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 상태가 완료되어야 단체지정이 해제가능합니다.");
	}

	@DisplayName("주문 테이블의 방문 손님 수를 변경한다.")
	@Test
	void testChangeNumberOfGuests() {
		int changedNumberOfGuests = 3;
		OrderTableRequest request = new OrderTableRequest(changedNumberOfGuests, true);
		OrderTable savedOrderTable = new OrderTable(null, new NumberOfGuests(1), false);

		long orderTableId = 1L;
		when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));
		when(orderTableRepository.save(savedOrderTable)).thenReturn(savedOrderTable);

		OrderTableResponse actual = tableSevrice.changeNumberOfGuests(orderTableId, request);
		assertThat(actual.getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
	}

	@DisplayName("변경할 방문 손님 수가 0보다 작으면 오류 발생")
	@Test
	void testNumberOfGuestsUnderZero() {
		OrderTableRequest request = new OrderTableRequest(-1, true);
		Long orderTableId = 1L;
		OrderTable savedOrderTable = new OrderTable(null, new NumberOfGuests(1), true);

		when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));

		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeNumberOfGuests(orderTableId, request);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("방문 손님 수는 0보다 작을 수 없습니다.");
	}

	@DisplayName("변경할 주문 테이블이 없는 경우 오류 발생")
	@Test
	void testNotFoundChangeTargetTable() {
		OrderTableRequest request = new OrderTableRequest(2, true);

		Long orderTableId = 1L;
		when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.empty());
		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeNumberOfGuests(orderTableId, request);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 주문 테이블을 찾을 수 없습니다.");
	}

	@DisplayName("변경할 주문 테이블이 비어있는 경우")
	@Test
	void testChangeTargetOrderTableIsEmpty() {
		OrderTableRequest request = new OrderTableRequest(2, true);
		OrderTable savedOrderTable = new OrderTable(null, new NumberOfGuests(1), true);
		Long orderTableId = 1L;

		when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));

		Assertions.assertThatThrownBy(() -> {
			tableSevrice.changeNumberOfGuests(orderTableId, request);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있는 테이블입니다.");
	}
}