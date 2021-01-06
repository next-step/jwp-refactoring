package kitchenpos.domain;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import kitchenpos.exception.AlreadyOrderCompleteException;
import kitchenpos.exception.EmptyTableException;

@DisplayName("Order 도메인 테스트")
class OrderTest {

	@DisplayName("create메서드는 주문테이블, 메뉴, 수량을 받아 Order 객체를 생성한다.")
	@Test
	void create() {
		Order result = Order.create(비어있지않은_테이블_객체(), Arrays.asList(일반_메뉴1(), 일반_메뉴2()), Arrays.asList(1L, 2L));
		assertThat(result).isInstanceOf(Order.class);
	}

	@DisplayName("create메서드는 빈 테이블이 전달될 경우 EmptyTableException이 발생한다.")
	@Test
	void createThrow() {
		assertThatExceptionOfType(EmptyTableException.class)
			.isThrownBy(() -> {
				Order.create(빈_테이블_객체(), Arrays.asList(일반_메뉴1(), 일반_메뉴2()), Arrays.asList(1L, 2L));
			});
	}

	@DisplayName("changeOrderStatus 주문상태를 변경한다.")
	@ParameterizedTest
	@MethodSource("paramChangeOrder")
	void changeOrderStatus(OrderStatus orderStatus) {
		Order order = 일반_주문();
		order.changeOrderStatus(orderStatus);

		assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
	}

	@DisplayName("changeOrderStatus메서드는 완료된 주문 상태의 주문상태를 변경할 경우 AlreadyOrderCompleteException이 발생한다.")
	@ParameterizedTest
	@MethodSource("paramChangeOrder")
	void changeOrderStatusThrow(OrderStatus orderStatus) {
		assertThatExceptionOfType(AlreadyOrderCompleteException.class)
			.isThrownBy(() -> {
				Order order = 완료된_주문();
				order.changeOrderStatus(orderStatus);
			});
	}

	public static Stream<Arguments> paramChangeOrder() {
		return Stream.of(
			Arguments.of(OrderStatus.COOKING),
			Arguments.of(OrderStatus.MEAL),
			Arguments.of(OrderStatus.COMPLETION)
		);
	}

}