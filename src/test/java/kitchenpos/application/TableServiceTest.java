package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;

@DisplayName("주문테이블 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private TableService tableService;

	@DisplayName("주문테이블을 등록할 수 있다. ")
	@Test
	void createTest() {
		// given
		OrderTableRequest orderTableRequest = mock(OrderTableRequest.class);
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getNumberOfGuests()).thenReturn(1);
		when(orderTableRequest.toEntity()).thenReturn(orderTable);
		when(orderTableRepository.save(any(OrderTable.class))).thenReturn(orderTable);

		// when
		tableService.create(orderTableRequest);

		// then
		verify(orderTableRepository).save(orderTable);
	}

	@DisplayName("주문테이블 목록을 조회할 수 있다.")
	@Test
	void listTest() {
		// given
		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getId()).thenReturn(1L);
		when(orderTable.getNumberOfGuests()).thenReturn(1);
		when(orderTableRepository.findAll()).thenReturn(asList(orderTable));

		// when
		List<OrderTableResponse> orderTableResponses = tableService.list();

		// then
		assertThat(orderTableResponses.size()).isEqualTo(1);
		assertThat(orderTableResponses.get(0).getId()).isEqualTo(1L);
	}

	@DisplayName("등록된 주문테이블만 상태를 바꿀 수 있다.")
	@Test
	void changeEmptyWithUnknownOrderTableTest() {
		// given
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> tableService.changeEmpty(1L, mock(OrderTableRequest.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 되지 않은 주문테이블은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("등록된 주문 테이블만 방문 손님 수를 수정할 수 없다.")
	@Test
	void changeNumberOfGuestsUnknownOrderTableTest() {
		// given
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, mock(OrderTableRequest.class)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 안된 주문테이블은 방문 손님 수를 수정할 수 없습니다.");
	}
}
