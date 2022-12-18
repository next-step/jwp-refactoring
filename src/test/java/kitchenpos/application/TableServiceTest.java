package kitchenpos.application;

import static kitchenpos.generator.OrderTableGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
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

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@InjectMocks
	private TableService tableService;

	private OrderTable 주문테이블;

	@BeforeEach
	void setUp() {
		주문테이블 = 주문테이블(null, 2, false);
	}

	@DisplayName("테이블을 등록할 수 있다.")
	@Test
	void createTableTest() {
		// given
		given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

		// when
		OrderTable savedOrderTable = tableService.create(주문테이블);

		// then
		assertThat(savedOrderTable)
			.satisfies(orderTable -> {
				assertThat(orderTable.getId()).isEqualTo(주문테이블.getId());
				assertThat(orderTable.getNumberOfGuests()).isEqualTo(주문테이블.getNumberOfGuests());
				assertThat(orderTable.isEmpty()).isEqualTo(주문테이블.isEmpty());
			});
	}

	@DisplayName("테이블 목록을 조회할 수 있다.")
	@Test
	void tableListTest() {
		// given
		OrderTable 주문테이블2 = 주문테이블(null, 5, false);
		given(orderTableDao.findAll()).willReturn(Arrays.asList(주문테이블, 주문테이블2));

		// when
		List<OrderTable> list = tableService.list();

		// then
		assertThat(list).containsExactly(주문테이블, 주문테이블2);
	}

	@DisplayName("테이블의 상태를 빈 테이블로 변경할 수 있다.")
	@Test
	void updateTableStatusTest() {
		// given
		주문테이블.setEmpty(true);
		given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(주문테이블));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.getId(),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
		given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

		// when
		OrderTable orderTable = tableService.changeEmpty(주문테이블.getId(), 주문테이블);

		// then
		assertThat(orderTable.isEmpty()).isTrue();
	}

	@DisplayName("빈 테이블 여부를 변경하려는 테이블의 정보가 이미 저장되어 있어야 한다.")
	@Test
	void updateTableStatusWithNullTableTest() {
		// given
		주문테이블.setEmpty(true);
		given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), 주문테이블));
	}

	@DisplayName("테이블 그룹에 속해있으면 빈 테이블로 변경할 수 없다.")
	@Test
	void updateTableStatusWithExistGroupTableTest() {
		// given
		주문테이블.setTableGroupId(1L);
		주문테이블.setEmpty(true);
		given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(주문테이블));

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), 주문테이블));
	}

	@DisplayName("주문 상태가 조리중, 식사중이면 빈 테이블로 변경할 수 없다.")
	@Test
	void updateTableStatusWithCookingOrMealOrderStatusTest() {
		// given
		주문테이블.setEmpty(true);
		given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(주문테이블));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.getId(),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), 주문테이블));
	}

	@DisplayName("방문한 손님 수를 변경할 수 있다.")
	@Test
	void updateNumberOfGuestsTest() {
		// given
		int guestNumbersToChange = 4;
		주문테이블.setNumberOfGuests(guestNumbersToChange);
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
		given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

		// when
		OrderTable orderTable = tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블);

		// then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(guestNumbersToChange);
	}

	@DisplayName("변경하려는 방문 손님 수는 최소 0명 이어야 한다.")
	@ParameterizedTest(name = "[{index}] 손님 수 : {0}")
	@ValueSource(ints = {-1, -5, -10})
	void updateNumberOfGuestWithNegativeNumber(int numberOfGuests) {
		// given
		주문테이블.setNumberOfGuests(numberOfGuests);

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블));
	}

	@DisplayName("방문 손님 수를 변경하려는 주문 테이블은 저장되어 있어야 한다.")
	@Test
	void updateNumberOfGuestsWithEmptyOrderTableTest() {
		// given
		주문테이블.setNumberOfGuests(5);
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.empty());

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블));
	}

	@DisplayName("방문 손님 수를 변경하려는 주문 테이블은 비어있지 않은 상태여야 한다.")
	@Test
	void updateNumberOfGuestsWithEmptyStatusTest() {
		// given
		주문테이블.setNumberOfGuests(5);
		주문테이블.setEmpty(true);
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));

		// when, then
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블));
	}
}
