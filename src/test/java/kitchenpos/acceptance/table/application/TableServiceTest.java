package kitchenpos.acceptance.table.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableRepository orderTableRepository;
	@InjectMocks
	private TableService tableService;

	@DisplayName("주문 테이블: 주문 테이블 생성 테스트")
	@Test
	void createTest() {
		// given
		OrderTable orderTable = OrderTable.of(1L, 0, true);
		OrderTableRequest request = OrderTableRequest.of(0, true);
		given(orderTableRepository.save(any())).willReturn(orderTable);

		// when
		final OrderTableResponse actual = tableService.create(request);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull()
		);
	}

	@DisplayName("주문 테이블: 빈테이블 설정 변경 테스트")
	@Test
	void changeEmptyTest() {
		// given
		OrderTable orderTable = OrderTable.of(1L, 0, true);
		OrderTableRequest request = OrderTableRequest.of(0, true);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
		//given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

		// when
		final OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), request);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.isEmpty()).isEqualTo(orderTable.isEmpty())
		);
	}

	@DisplayName("주문 테이블[예외]: 빈테이블 설정 변경 테스트(1. 주문테이블이 존재해야 한다.)")
	@Test
	void errorNotfoundOrderTableTest() {
		// given
		OrderTable orderTable = OrderTable.of(1L, 0, true);
		OrderTableRequest request = OrderTableRequest.of(0, true);

		// when
		when(orderTableRepository.findById(any())).thenThrow(EntityNotFoundException.class);

		// then
		assertThatThrownBy(
			() -> tableService.changeEmpty(orderTable.getId(), request)
		).isInstanceOf(EntityNotFoundException.class);
	}

	/*
		@DisplayName("주문 테이블[예외]: 빈테이블 설정 변경 테스트(2. 테이블 그룹이 null 이여야한다.)")
		@Test
		void errorNonNullTableGroupTest() {
			// given
			OrderTable orderTable = OrderTable.of(1L, 1L, 0, false);
			OrderTable orderTable = OrderTable.of(1L, 0, true);
			OrderTableRequest request = OrderTableRequest.of(0, true);

			// when
			when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

			// then
			assertThatIllegalArgumentException().isThrownBy(
				() -> tableService.changeEmpty(1L, orderTable)
			);
		}

		@DisplayName("주문 테이블[예외]: 빈테이블 설정 변경 테스트(3. 주문 상태가 COOKING, MEAL 상태가 아니여야 한다.)")
		@Test
		void errorOrderStatusTypeTest() {
			// given
			OrderTable param = OrderTable.of(1L, null, 0, true);
			given(orderTableRepository.findById(any())).willReturn(Optional.of(param));

			// when
			when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

			// then
			assertThatIllegalArgumentException().isThrownBy(
				() -> tableService.changeEmpty(1L, param)
			);
		}
	*/

	@DisplayName("주문 테이블: 손님 인원수 변경 테스트")
	@Test
	void changeNumberOfGuestsTest() {
		// given
		OrderTable orderTable = OrderTable.of(1L, 3, false);
		OrderTableRequest request = OrderTableRequest.of(6, false);
		given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

		// when
		final OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests())
		);
	}

	@DisplayName("주문 테이블[예외]: 손님 인원수 변경 테스트(1. 변경 인원수가 0 이하일 수 없다.)")
	@Test
	void errorChangeNumberTest() {
		// given // when
		OrderTable orderTable = OrderTable.of(1L, 3, false);
		OrderTableRequest request = OrderTableRequest.of(-6, false);
		given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableService.changeNumberOfGuests(orderTable.getId(), request)
		);
	}

	@DisplayName("주문 테이블[예외]: 손님 인원수 변경 테스트(2. 테이블이 비어 있다면 변경 할 수 없다.)")
	@Test
	void errorEmptyTableTest() {
		// given // when
		OrderTable orderTable = OrderTable.of(1L, 0, true);
		OrderTableRequest request = OrderTableRequest.of(6, false);
		given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableService.changeNumberOfGuests(orderTable.getId(), request)
		);
	}
}
