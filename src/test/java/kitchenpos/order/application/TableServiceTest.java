package kitchenpos.order.application;

import kitchenpos.common.application.NotFoundException;
import kitchenpos.order.dto.OrderTableRequest_ChangeEmpty;
import kitchenpos.order.dto.OrderTableRequest_ChangeGuests;
import kitchenpos.order.dto.OrderTableRequest_Create;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TableServiceTest {

	@Autowired
	private TableService tableService;

	private OrderTableRequest_Create orderTableRequestCreate;
	private OrderTableResponse savedOrderTable1;

	@BeforeEach
	void setUp() {
		orderTableRequestCreate = new OrderTableRequest_Create(10, true);
		savedOrderTable1 = tableService.create(new OrderTableRequest_Create(10, false));
	}

	@DisplayName("새로운 테이블을 생선한다.")
	@Test
	void create() {
		OrderTableResponse response = tableService.create(orderTableRequestCreate);

		assertThat(response.getId()).isNotNull();
		assertThat(response.getNumberOfGuests()).isEqualTo(orderTableRequestCreate.getNumberOfGuests());
		assertThat(response.isEmpty()).isEqualTo(orderTableRequestCreate.isEmpty());
		assertThat(response.getTableGroupId()).isNull();
	}

	@DisplayName("테이블 리스트를 반환한다.")
	@Test
	void list() {
		// given
		List<OrderTableResponse> orderTables = Arrays.asList(tableService.create(orderTableRequestCreate),
				tableService.create(orderTableRequestCreate),
				tableService.create(orderTableRequestCreate));

		// when then
		final List<Long> tableIds = orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList());
		assertThat(tableService.list())
				.map(OrderTableResponse::getId)
				.containsAll(tableIds);
	}

	@DisplayName("테이블의 비어있는 상태를 바꾼다.")
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void changeEmpty(boolean isEmpty) {
		OrderTableRequest_ChangeEmpty request = new OrderTableRequest_ChangeEmpty(isEmpty);

		OrderTableResponse response = tableService.changeEmpty(savedOrderTable1.getId(), request);

		assertThat(response.isEmpty()).isEqualTo(isEmpty);
	}

	@DisplayName("존재하지 않는 테이블의 비어있는 상태 변경시 예외 발생.")
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void changeEmpty_NotExistOrderTable(boolean isEmpty) {
		OrderTableRequest_ChangeEmpty request = new OrderTableRequest_ChangeEmpty(isEmpty);

		assertThatThrownBy(() -> tableService.changeEmpty(-55L, request))
				.isInstanceOf(NotFoundException.class)
				.hasMessageMatching(TableService.MSG_CANNOT_FIND_ORDER_TABLE);
	}

	@DisplayName("테이블의 인원수를 변경한다.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 5, 99})
	void changeNumberOfGuests(int numberOfGuests) {
		// given
		OrderTableRequest_ChangeGuests request = new OrderTableRequest_ChangeGuests(numberOfGuests);

		// when
		OrderTableResponse response = tableService.changeNumberOfGuests(savedOrderTable1.getId(), request);

		// then
		assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@DisplayName("존재하지 않는 테이블의 인원 변경시 예외 발생.")
	@Test
	void changeNumberOfGuests_NotExistTable() {
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, new OrderTableRequest_ChangeGuests(5)))
				.isInstanceOf(NotFoundException.class)
				.hasMessageMatching(TableService.MSG_CANNOT_FIND_ORDER_TABLE);
	}
}
