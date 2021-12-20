package kitchenpos.application;

import static kitchenpos.ordertable.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@InjectMocks
	private TableService tableService;

	@DisplayName("주문 테이블을 등록한다.")
	@Test
	void create() {
		// given
		given(orderTableDao.save(any())).willReturn(빈_주문_테이블());
		OrderTable request = 빈_주문_테이블_요청().toOrderTable();

		// when
		OrderTable orderTable = tableService.create(request);

		// then
		assertAll(
			() -> assertThat(orderTable).isNotNull(),
			() -> assertThat(orderTable.getId()).isNotNull(),
			() -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
			() -> assertThat(orderTable.isEmpty()).isEqualTo(request.isEmpty()));
	}

	@DisplayName("주문 테이블 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		given(orderTableDao.findAll()).willReturn(Collections.singletonList(빈_주문_테이블()));

		// when
		List<OrderTable> orderTables = tableService.list();

		// then
		List<Long> actualIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
		List<Long> expectIds = Collections.singletonList(빈_주문_테이블().getId());
		assertThat(actualIds).containsAll(expectIds);
	}

	@DisplayName("주문 테이블의 빈 상태를 변경할 수 있다.")
	@Test
	void changEmpty() {
		// given
		given(orderTableDao.findById(any())).willReturn(Optional.of(빈_주문_테이블()));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
		given(orderTableDao.save(any())).willReturn(빈_주문_테이블_채워짐());

		// when
		OrderTable orderTable = tableService.changeEmpty(빈_주문_테이블().getId(), 채우기_요청().toOrderTable());

		// then
		assertThat(orderTable.isEmpty()).isEqualTo(채우기_요청().isEmpty());
	}

	@DisplayName("주문 테이블 그룹에 속해 있는 경우 주문 테이블의 빈 상태를 변경할 수 없다.")
	@Test
	void changEmptyFailOnBelongToOrderTableGroup() {
		// given
		given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블_그룹에_속한_주문_테이블()));

		// when
		ThrowingCallable throwingCallable = () ->
			tableService.changeEmpty(주문_테이블_그룹에_속한_주문_테이블().getId(), 채우기_요청().toOrderTable());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블에 완료되지 않은 주문이 있는 경우 주문 테이블의 빈 상태를 변경할 수 없다.")
	@Test
	void changEmptyFailOnNotCompletedOrderExist() {
		// given
		given(orderTableDao.findById(any())).willReturn(Optional.of(비어있지않은_주문_테이블()));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

		// when
		ThrowingCallable throwingCallable = () ->
			tableService.changeEmpty(비어있지않은_주문_테이블().getId(), 비우기_요청().toOrderTable());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블에 손님 수를 변경할 수 있다")
	@Test
	void changeNumberOfGuests() {
		// given
		int numberOfGuests = 6;
		given(orderTableDao.findById(any())).willReturn(Optional.of(비어있지않은_주문_테이블()));
		given(orderTableDao.save(any())).willReturn(비어있지않은_주문_테이블_손님_수_변경됨(numberOfGuests));

		// when
		OrderTable orderTable = tableService.changeNumberOfGuests(
			비어있지않은_주문_테이블().getId(), 손님_수_변경_요청(numberOfGuests).toOrderTable());

		// then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@DisplayName("손님 수가 0보다 작은 경우 주문 테이블에 손님 수를 변경할 수 없다")
	@Test
	void changeNumberOfGuestsFailOnNegative() {
		// given
		int numberOfGuests = -1;

		// when
		ThrowingCallable throwingCallable = () ->
			tableService.changeNumberOfGuests(비어있지않은_주문_테이블().getId(), 손님_수_변경_요청(numberOfGuests).toOrderTable());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("빈 주문 테이블인 경우 주문 테이블에 손님 수를 변경할 수 없다")
	@Test
	void changeNumberOfGuestsFailOnEmpty() {
		// given
		int numberOfGuests = 6;
		given(orderTableDao.findById(any())).willReturn(Optional.of(빈_주문_테이블()));

		// when
		ThrowingCallable throwingCallable = () ->
			tableService.changeNumberOfGuests(빈_주문_테이블().getId(), 손님_수_변경_요청(numberOfGuests).toOrderTable());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}
}
