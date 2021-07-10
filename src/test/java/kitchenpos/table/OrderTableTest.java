package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;

@DisplayName("주문 테이블 도메인 테스트")
public class OrderTableTest {

	@DisplayName("주문 테이블 생성 테스트")
	@Test
	void 주문_테이블_생성_테스트() {
		NumberOfGuests numberOfGuests = new NumberOfGuests(1);
		OrderTable actual = new OrderTable(1L, numberOfGuests);

		주문_테이블_생성_확인(actual, 1L);
	}

	@DisplayName("주문 테이블 비우거나 채우기 테스트")
	@Test
	void 주문_테이블_비우거나_채우기_테스트() {
		NumberOfGuests numberOfGuests = new NumberOfGuests(1);
		OrderTable actual = new OrderTable(1L, numberOfGuests);
		Order order = new Order(); //todo
		actual.changeEmpty(false, order);

		주문_테이블_비우거나_채우기_확인(actual);
	}

	@DisplayName("주문 테이블의 손님 수를 변경하기 테스트")
	@Test
	void 주문_테이블의_손님_수를_변경하기_테스트() {
		NumberOfGuests numberOfGuests = new NumberOfGuests(1);
		OrderTable actual = new OrderTable(1L, numberOfGuests);
		actual.changeNumberOfGuests(new NumberOfGuests((2)));

		주문_테이블의_손님_수_변경_확인(actual, 2);
	}

	private void 주문_테이블의_손님_수_변경_확인(OrderTable actual, int changeNumberOfGuests) {
		assertThat(actual.getNumberOfGuests().value()).isEqualTo(changeNumberOfGuests);
	}

	private void 주문_테이블_비우거나_채우기_확인(OrderTable actual) {
		assertThat(actual.isEmpty()).isEqualTo(false);
	}

	private void 주문_테이블_생성_확인(OrderTable actual, Long expectedId) {
		assertThat(actual).isNotNull();
		assertThat(actual.getId()).isEqualTo(expectedId);
		assertThat(actual.getTableGroup()).isNull();
	}
}
