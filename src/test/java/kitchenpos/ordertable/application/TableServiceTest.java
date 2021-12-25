package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableAddRequest;
import kitchenpos.ordertable.dto.OrderTableEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.exception.NotFoundOrderTableException;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

	@InjectMocks
	private TableService tableService;

	@Mock
	private OrderService orderService;
	@Mock
	private OrderTableRepository orderTableRepository;

	@DisplayName("주문테이블 생성")
	@Test
	void create() {
		given(orderTableRepository.save(any())).willReturn(
			OrderTable.of(1L, null, 3, true)
		);

		final OrderTableResponse createdOrderTable = tableService.create(
			OrderTableAddRequest.of(3, true)
		);

		assertThat(createdOrderTable.getId()).isNotNull();
		assertThat(createdOrderTable.getTableGroupId()).isNull();
	}

	@DisplayName("주문테이블 목록조회")
	@Test
	void list() {
		final OrderTable 테이블4명 = OrderTable.of(1L, null, 4, false);
		final OrderTable 테이블3명 = OrderTable.of(2L, null, 3, true);
		given(orderTableRepository.findAll()).willReturn(Arrays.asList(테이블4명, 테이블3명));

		assertThat(tableService.list().size()).isEqualTo(2);
	}

	@DisplayName("주문테이블 비어있음 유무 수정")
	@Test
	void changeEmpty() {
		final OrderTable 비어있지않은_테이블 = OrderTable.of(1L, null, 2, false);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(비어있지않은_테이블));
		given(orderService.existsOrderStatusCookingOrMeal(비어있지않은_테이블.getId())).willReturn(false);

		final OrderTableResponse changedOrderTable = tableService.changeEmpty(
			비어있지않은_테이블.getId(),
			OrderTableEmptyRequest.of(true)
		);

		assertThat(changedOrderTable.isEmpty()).isTrue();
	}

	@DisplayName("주문테이블 비어있음 유무 수정: 주문테이블이 존재하지 않으면 예외발생")
	@Test
	void changeEmpty_not_found_order_table() {
		final Long orderTableId = 1L;
		given(orderTableRepository.findById(any())).willReturn(Optional.empty());

		assertThatExceptionOfType(NotFoundOrderTableException.class)
			.isThrownBy(() -> tableService.changeEmpty(orderTableId,
				OrderTableEmptyRequest.of(false)
			));
	}

	@DisplayName("주문테이블 비어있음 유무 수정: 주문이 `조리` 혹은 `식사` 상태면 예외발생")
	@Test
	void changeEmpty_order_table_status_cooking_or_meal() {
		final OrderTable 비어있지않은_테이블 = OrderTable.of(1L, null, 2, false);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(비어있지않은_테이블));
		given(orderService.existsOrderStatusCookingOrMeal(비어있지않은_테이블.getId())).willReturn(true);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(비어있지않은_테이블.getId(),
				OrderTableEmptyRequest.of(true)
			));
	}

	@DisplayName("주문테이블의 손님 수 수정")
	@Test
	void changeNumberOfGuests() {
		final OrderTable 손님_1명_테이블 = OrderTable.of(1L, null, 1, false);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(손님_1명_테이블));

		final OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(손님_1명_테이블.getId(),
			OrderTableNumberOfGuestsRequest.of(2)
		);

		assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
	}

	@DisplayName("주문테이블의 손님 수 수정: 주문테이블이 존재하지 않으면 예외발생")
	@Test
	void changeNumberOfGuests_not_found_order_table() {
		final Long orderTableId = 1L;
		given(orderTableRepository.findById(any())).willReturn(Optional.empty());

		assertThatExceptionOfType(NotFoundOrderTableException.class)
			.isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId,
				OrderTableNumberOfGuestsRequest.of(3)
			));
	}
}
