package kitchenpos.ordertablegroup.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;

@DisplayName("주문 테이블 그룹")
class OrderTableGroupTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.of(4), true);

		// when
		OrderTableGroup orderTableGroup = OrderTableGroup.of(Arrays.asList(
			orderTable1,
			orderTable2));

		// then
		assertThat(orderTableGroup).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 테이블이 2개 미만인 경우")
	@Test
	void ofFailOnLessThenTwo() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.of(4), true);

		// when
		ThrowingCallable throwingCallable = () -> OrderTableGroup.of(Collections.singletonList(orderTable));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성 실패 - 이미 주문 테이블 그룹이 있는 경우")
	@Test
	void ofFailOnAlreadyHavingOrderTableGroup() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTable orderTable3 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTableGroup.of(Arrays.asList(orderTable1, orderTable2));

		// when
		ThrowingCallable throwingCallable = () -> OrderTableGroup.of(Arrays.asList(orderTable1, orderTable3));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성 실패 - 주문 테이블이 비어있지 않은 경우")
	@Test
	void ofFailOnNotEmpty() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.of(4), false);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.of(4), false);

		// when
		ThrowingCallable throwingCallable = () -> OrderTableGroup.of(Arrays.asList(orderTable1, orderTable2));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("그룹 해제")
	@Test
	void ungroup() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTableGroup orderTableGroup = OrderTableGroup.of(Arrays.asList(orderTable1, orderTable2));

		// when
		orderTableGroup.ungroup();

		// then
		assertAll(
			() -> assertThat(orderTableGroup.getOrderTables()).isEmpty(),
			() -> assertThat(orderTable1.getOrderTableGroup()).isNull(),
			() -> assertThat(orderTable2.getOrderTableGroup()).isNull());
	}

	@DisplayName("그룹 해제 실패 - 주문 테이블에 완료되지 않은 주문이 있는 경우")
	@Test
	void ungroupFailOnHavingNotCompletedOrder() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.of(4), true);
		OrderTableGroup orderTableGroup = OrderTableGroup.of(Arrays.asList(orderTable1, orderTable2));
		Order.of(orderTable1,
			OrderLineItems.of(Collections.singletonList(OrderLineItem.of(
				Menu.of(
					Name.of("후라이드+후라이드"),
					Price.of(BigDecimal.valueOf(25000)),
					MenuGroup.of(Name.of("추천메뉴")),
					MenuProducts.of(Collections.singletonList(
						MenuProduct.of(
							Product.of(Name.of("후라이드치킨"), Price.of(BigDecimal.valueOf(17000))),
							Quantity.of(2L))))),
				Quantity.of(1L)))));

		// when
		ThrowingCallable throwingCallable = orderTableGroup::ungroup;

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}
}
