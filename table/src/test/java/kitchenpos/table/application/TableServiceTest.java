package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.generator.OrderTableGenerator;
import kitchenpos.table.table.appliation.TableService;
import kitchenpos.table.table.appliation.TableValidator;
import kitchenpos.table.table.domain.OrderTable;
import kitchenpos.table.table.domain.OrderTableRepository;
import kitchenpos.table.table.domain.TableGroup;
import kitchenpos.table.table.ui.request.NumberOfGuestsRequest;
import kitchenpos.table.table.ui.request.OrderTableRequest;
import kitchenpos.table.table.ui.request.TableStatusRequest;
import kitchenpos.table.table.ui.response.OrderTableResponse;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private TableValidator tableValidator;

	@InjectMocks
	private TableService tableService;

	private OrderTable 주문테이블;

	@BeforeEach
	void setUp() {
		주문테이블 = OrderTableGenerator.주문테이블(2, false);
	}

	@DisplayName("테이블을 등록할 수 있다.")
	@Test
	void createTableTest() {
		// given
		given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문테이블);
		OrderTableRequest orderTableRequest = new OrderTableRequest(2, false);

		// when
		OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);

		// then
		assertThat(savedOrderTable)
			.satisfies(orderTable -> {
				assertThat(orderTable.getId()).isEqualTo(주문테이블.id());
				assertThat(orderTable.getNumberOfGuests()).isEqualTo(주문테이블.getNumberOfGuests());
				assertThat(orderTable.isEmpty()).isEqualTo(주문테이블.isEmpty());
			});
	}

	@DisplayName("테이블 목록을 조회할 수 있다.")
	@Test
	void tableListTest() {
		// when
		tableService.list();

		// then
		verify(orderTableRepository, only()).findAll();
	}

	@DisplayName("테이블의 상태를 빈 테이블로 변경할 수 있다.")
	@Test
	void updateTableStatusTest() {
		// given
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(주문테이블));
		willDoNothing().given(tableValidator).validateChangeEmpty(any());
		TableStatusRequest tableStatusRequest = new TableStatusRequest(true);

		// when
		OrderTableResponse response = tableService.changeEmpty(주문테이블.id(), tableStatusRequest);

		// then
		assertThat(response.isEmpty()).isTrue();
	}

	@DisplayName("빈 테이블 여부를 변경하려는 테이블의 정보가 이미 저장되어 있어야 한다.")
	@Test
	void updateTableStatusWithNullTableTest() {
		// given
		TableStatusRequest tableStatusRequest = new TableStatusRequest(true);
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

		// when
		Throwable throwable = catchThrowable(() -> tableService.changeEmpty(주문테이블.id(), tableStatusRequest));

		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class);
	}

	@DisplayName("테이블 그룹에 속해있으면 빈 테이블로 변경할 수 없다.")
	@Test
	void updateTableStatusWithExistGroupTableTest() {
		// given
		TableStatusRequest tableStatusRequest = new TableStatusRequest(true);
		OrderTable 빈_한명_테이블 = OrderTableGenerator.빈_한명_테이블();
		OrderTable 비어있는_두명_테이블 = OrderTableGenerator.비어있는_두명_테이블();
		TableGroup.from(Arrays.asList(빈_한명_테이블, 비어있는_두명_테이블));
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(빈_한명_테이블));

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(주문테이블.id(), tableStatusRequest));
	}

	@DisplayName("주문 상태가 조리중, 식사중이면 빈 테이블로 변경할 수 없다.")
	@Test
	void updateTableStatusWithCookingOrMealOrderStatusTest() {
		// given
		TableStatusRequest tableStatusRequest = new TableStatusRequest(true);
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(주문테이블));
		willThrow(IllegalArgumentException.class).given(tableValidator).validateChangeEmpty(any());

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(주문테이블.id(), tableStatusRequest));
	}

	@DisplayName("방문한 손님 수를 변경할 수 있다.")
	@Test
	void updateNumberOfGuestsTest() {
		// given
		int guestNumbersToChange = 4;
		NumberOfGuestsRequest request = new NumberOfGuestsRequest(guestNumbersToChange);
		given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));

		// when
		OrderTableResponse response = tableService.changeNumberOfGuests(주문테이블.id(), request);

		// then
		assertThat(response.getNumberOfGuests()).isEqualTo(guestNumbersToChange);
	}

	@DisplayName("변경하려는 방문 손님 수는 최소 0명 이어야 한다.")
	@ParameterizedTest(name = "[{index}] 손님 수 : {0}")
	@ValueSource(ints = {-1, -5, -10})
	void updateNumberOfGuestWithNegativeNumber(int numberOfGuests) {
		// given
		NumberOfGuestsRequest request = new NumberOfGuestsRequest(numberOfGuests);
		given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.ofNullable(주문테이블));

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.id(), request));
	}

	@DisplayName("방문 손님 수를 변경하려는 주문 테이블은 저장되어 있어야 한다.")
	@Test
	void updateNumberOfGuestsWithEmptyOrderTableTest() {
		// given
		NumberOfGuestsRequest request = new NumberOfGuestsRequest(5);
		given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.empty());

		// when
		Throwable throwable = catchThrowable(() -> tableService.changeNumberOfGuests(주문테이블.id(), request));

		// then
		assertThat(throwable).isInstanceOf(NotFoundException.class);
	}

	@DisplayName("방문 손님 수를 변경하려는 주문 테이블은 비어있지 않은 상태여야 한다.")
	@Test
	void updateNumberOfGuestsWithEmptyStatusTest() {
		// given
		NumberOfGuestsRequest request = new NumberOfGuestsRequest(5);
		given(orderTableRepository.findById(주문테이블.id())).willReturn(Optional.of(OrderTableGenerator.빈_두명_테이블()));

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(OrderTableGenerator.비어있는_다섯명_테이블().id(), request));
	}
}
