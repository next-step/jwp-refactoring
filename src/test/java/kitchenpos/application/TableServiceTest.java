package kitchenpos.application;

import kitchenpos.dto.OrderTableRequest_ChangeEmpty;
import kitchenpos.dto.OrderTableRequest_ChangeGuests;
import kitchenpos.dto.OrderTableRequest_Create;
import kitchenpos.dto.OrderTableResponse;
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
	private OrderTableResponse savedOrderTable;

	@BeforeEach
	void setUp() {
		orderTableRequestCreate = new OrderTableRequest_Create(10, true);
		savedOrderTable = tableService.create(new OrderTableRequest_Create(10, false));
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

		OrderTableResponse response = tableService.changeEmpty(savedOrderTable.getId(), request);

		assertThat(response.isEmpty()).isEqualTo(isEmpty);
	}

	@DisplayName("이미 단체지정된 테이블의 비어있는 상태 변경시 예외 발생.")
	@Test
	void changeEmpty_AlreadyTableGroupIncluded() {
		// given
		// TODO: 2021-01-15 TableGroupService 완료 후 재작성
		throw new UnsupportedOperationException("TableGroupService 완료 후 재작성");
	}

	@DisplayName("특정 상태인 테이블을 비우려고 하면 예외 발생.")
	@Test
	void changeEmpty_StatusWrong() {
		// given
		// TODO: 2021-01-15 TableGroupService 완료 후 재작성
		throw new UnsupportedOperationException("TableGroupService 완료 후 재작성");
	}

	@DisplayName("테이블의 인원수를 변경한다.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 5, 99})
	void changeNumberOfGuests(int numberOfGuests) {
		// given
		OrderTableRequest_ChangeGuests request = new OrderTableRequest_ChangeGuests(numberOfGuests);

		// when
		OrderTableResponse response = tableService.changeNumberOfGuests(savedOrderTable.getId(), request);

		// then
		assertThat(response.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@DisplayName("테이블 인원 변경시 음수로 설정하면 예외 발생.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -999})
	void changeNumberOfGuests_GuestWrong(int numberOfGuests) {
		// given
		OrderTableRequest_ChangeGuests request = new OrderTableRequest_ChangeGuests(numberOfGuests);

		// when then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 인원 변경시 테이블이 비어있는 경우 예외 발생.")
	@Test
	void changeNumberOfGuests_TableEmpty() {
		// given
		tableService.changeEmpty(savedOrderTable.getId(), new OrderTableRequest_ChangeEmpty(true));

		// when & then
		OrderTableRequest_ChangeGuests request = new OrderTableRequest_ChangeGuests(5);
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), request))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
