package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@InjectMocks
	private TableService tableService;

	@DisplayName("주문 테이블: 주문 테이블 생성 테스트")
	@Test
	void createTest() {
		// given
		OrderTable orderTable = OrderTable.of(1L, null, 0, true);
		given(orderTableDao.save(any())).willReturn(orderTable);

		// when
		final OrderTable actual = tableService.create(orderTable);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTableGroupId()).isNull()
		);
	}

	@DisplayName("주문 테이블: 빈테이블 설정 변경 테스트")
	@Test
	void changeEmptyTest() {
		// given
		OrderTable orderTable = OrderTable.of(1L, null, 0, true);
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
		given(orderTableDao.save(any())).willReturn(OrderTable.of(1L, null, 0, false));

		// when
		final OrderTable actual = tableService.changeEmpty(1L, OrderTable.of(null, null, 0, false));

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.isEmpty()).isFalse()
		);
	}

	@DisplayName("주문 테이블[예외]: 빈테이블 설정 변경 테스트(1. 주문테이블이 존재해야 한다.)")
	@Test
	void errorNotfoundOrderTableTest() {
		// given
		OrderTable param = OrderTable.of(null, null, 0, false);

		// when
		when(orderTableDao.findById(any())).thenThrow(IllegalArgumentException.class);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableService.changeEmpty(1L, param)
		);
	}

	@DisplayName("주문 테이블[예외]: 빈테이블 설정 변경 테스트(2. 테이블 그룹이 null 이여야한다.)")
	@Test
	void errorNonNullTableGroupTest() {
		// given
		OrderTable orderTable = OrderTable.of(1L, 1L, 0, false);

		// when
		when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

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
		given(orderTableDao.findById(any())).willReturn(Optional.of(param));

		// when
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableService.changeEmpty(1L, param)
		);
	}

	@DisplayName("주문 테이블: 손님 인원수 변경 테스트")
	@Test
	void changeNumberOfGuestsTest() {
		// given
		OrderTable orderTable = OrderTable.of(1L, null, 5, false);
		OrderTable parameter = OrderTable.of(1L, null, 10, false);
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
		given(orderTableDao.save(any())).willReturn(parameter);

		// when
		final OrderTable actual = tableService.changeNumberOfGuests(1L, parameter);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getNumberOfGuests()).isEqualTo(parameter.getNumberOfGuests())
		);
	}

	@DisplayName("주문 테이블[예외]: 손님 인원수 변경 테스트(1. 변경 인원수아 0 이하일 수 없다.)")
	@Test
	void errorChangeNumberTest() {
		// given // when
		OrderTable parameter = OrderTable.of(1L, null, -10, false);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableService.changeNumberOfGuests(1L, parameter)
		);
	}

	@DisplayName("주문 테이블[예외]: 손님 인원수 변경 테스트(2. 테이블이 비어 있다면 변경 할 수 없다.)")
	@Test
	void errorEmptyTableTest() {
		// given // when
		OrderTable orderTable = OrderTable.of(1L, null, 5, true);
		OrderTable parameter = OrderTable.of(1L, null, 10, false);
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> tableService.changeNumberOfGuests(1L, parameter)
		);
	}
}
