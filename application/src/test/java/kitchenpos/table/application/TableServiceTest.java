package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.exception.OrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;

@DisplayName("주문 테이블 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

	@Mock
	OrderTableRepository orderTableRepository;

	@Mock
	TableValidator tableValidator;

	@Mock
	OrderTable orderTable;

	@InjectMocks
	private TableService tableService;

	private OrderTableRequest orderTableRequest;

	@DisplayName("테이블을 생성하는 테스트")
	@Test
	void createTable() {
		// given
		orderTableRequest = OrderTableRequest.of(10, true);

		// when
		when(orderTableRepository.save(any())).thenReturn(orderTable);

		// then
		assertThat(tableService.create(orderTableRequest)).isEqualTo(orderTable);
	}

	@DisplayName("테이블 목록을 조회하는 테스트")
	@Test
	void getList() {
		// when
		when(orderTableRepository.findAll()).thenReturn(Collections.singletonList(orderTable));

		// then
		assertThat(tableService.list()).containsExactly(orderTable);
	}

	@DisplayName("상태를 변경하려는 테이블이 존재하지 않을 경우 예외처리 테스트")
	@Test
	void changeEmptyUnknownOrderTable() {
		// given
		orderTableRequest = OrderTableRequest.of(10, true);

		// when // then
		assertThatThrownBy(() -> {
			tableService.changeEmpty(anyLong(), orderTableRequest);
		}).isInstanceOf(OrderException.class)
			.hasMessageContaining(ErrorCode.ORDER_TABLE_IS_NULL.getMessage());
	}

	@DisplayName("테이블의 상태를 변경하는 테스트")
	@Test
	void changeEmpty() {
		// given
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		given(orderTable.isEmpty()).willReturn(true);
		orderTableRequest = OrderTableRequest.of(10, true);
		doNothing()
			.when(tableValidator)
			.validate(orderTable);

		// when
		when(orderTableRepository.save(orderTable)).thenReturn(orderTable);

		// then
		assertThat(tableService.changeEmpty(anyLong(), orderTableRequest).isEmpty()).isTrue();
	}

	@DisplayName("변경하려는 손님 인원이 0미만일 경우 예외처리 테스트")
	@Test
	void changeNumberOfGuestUnderZeroGuest() {
		// given
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		orderTableRequest = OrderTableRequest.of(-1, true);

		// when
		doThrow(OrderException.class)
			.when(orderTable)
			.changeNumberOfGuests(orderTableRequest.getNumberOfGuest());

		// then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("손님 인원을 변경하려는 테이블이 존재하지 않은 경우 예외처리 테스트")
	@Test
	void changeNumberOfGuestUnknownOrderTable() {
		// given
		orderTableRequest = OrderTableRequest.of(10, true);

		// when
		when(orderTableRepository.findById(anyLong())).thenThrow(OrderException.class);

		// then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("손님 인원을 변경하려는 테이블이 비어있는 상태인 경우 예외처리 테스트")
	@Test
	void changeNumberOfGuestEmptyOrderTable() {
		// given
		orderTableRequest = OrderTableRequest.of(10, true);

		// when // then
		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("테이블의 손님 인원을 변경하는 테스트")
	@Test
	void changeNumberOfGuests() {
		// given
		given(orderTable.getNumberOfGuests()).willReturn(2);
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		orderTableRequest = OrderTableRequest.of(10, true);

		// when
		when(orderTableRepository.save(orderTable)).thenReturn(orderTable);

		// then
		assertThat(
			tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest).getNumberOfGuests()).isEqualTo(2);
	}
}
