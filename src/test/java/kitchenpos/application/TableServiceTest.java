package kitchenpos.application;

import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class TableServiceTest {

	@Autowired
	private TableService tableService;

	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private TableTestSupport tableTestSupport;

	private OrderTableRequest_Create orderTableRequestCreate;
	private OrderTableResponse savedOrderTable1;
	private OrderTableResponse savedOrderTable2;

	@BeforeEach
	void setUp() {
		orderTableRequestCreate = new OrderTableRequest_Create(10, true);
		savedOrderTable1 = tableService.create(new OrderTableRequest_Create(10, false));
		savedOrderTable2 = tableService.create(new OrderTableRequest_Create(10, false));
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

	@DisplayName("이미 단체지정된 테이블의 비어있는 상태 변경시 예외 발생.")
	@Test
	void changeEmpty_AlreadyTableGroupIncluded() {
		// given
		// 단체 지정하기 전에 테이블을 비워놓는다.
		tableService.changeEmpty(savedOrderTable1.getId(), new OrderTableRequest_ChangeEmpty(true));
		tableService.changeEmpty(savedOrderTable2.getId(), new OrderTableRequest_ChangeEmpty(true));
		// 테이블들을 단체 지정한다.
		tableGroupService.create(new TableGroupRequest_Create(Arrays.asList(
				savedOrderTable1.getId(), savedOrderTable2.getId())));

		assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), new OrderTableRequest_ChangeEmpty(false)))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("특정 상태인 테이블을 비우려고 하면 예외 발생.")
	@Test
	void changeEmpty_StatusWrong() {
		tableTestSupport.addOrder(savedOrderTable1.getId());

		assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), new OrderTableRequest_ChangeEmpty(false)))
				.isInstanceOf(IllegalArgumentException.class);
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

	@DisplayName("테이블 인원 변경시 음수로 설정하면 예외 발생.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -999})
	void changeNumberOfGuests_GuestWrong(int numberOfGuests) {
		// given
		OrderTableRequest_ChangeGuests request = new OrderTableRequest_ChangeGuests(numberOfGuests);

		// when then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable1.getId(), request))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 인원 변경시 테이블이 비어있는 경우 예외 발생.")
	@Test
	void changeNumberOfGuests_TableEmpty() {
		// given
		tableService.changeEmpty(savedOrderTable1.getId(), new OrderTableRequest_ChangeEmpty(true));

		// when & then
		OrderTableRequest_ChangeGuests request = new OrderTableRequest_ChangeGuests(5);
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable1.getId(), request))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
