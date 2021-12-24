package kitchenpos.ordertablegroup.domain;

import static kitchenpos.menugroup.MenuGroupFixture.*;
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
	void from() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.from(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.from(4), true);

		// when
		OrderTableGroup orderTableGroup = OrderTableGroup.from(Arrays.asList(
			orderTable1,
			orderTable2));

		// then
		assertThat(orderTableGroup).isNotNull();
	}

	@DisplayName("생성 실패 - 주문 테이블이 2개 미만인 경우")
	@Test
	void fromFailOnLessThenTwo() {
		// given
		OrderTable orderTable = OrderTable.of(NumberOfGuests.from(4), true);

		// when
		ThrowingCallable throwingCallable = () -> OrderTableGroup.from(Collections.singletonList(orderTable));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성 실패 - 이미 주문 테이블 그룹이 있는 경우")
	@Test
	void fromFailOnAlreadyHavingOrderTableGroup() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.from(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.from(4), true);
		OrderTable orderTable3 = OrderTable.of(NumberOfGuests.from(4), true);
		OrderTableGroup.from(Arrays.asList(orderTable1, orderTable2));

		// when
		ThrowingCallable throwingCallable = () -> OrderTableGroup.from(Arrays.asList(orderTable1, orderTable3));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성 실패 - 주문 테이블이 비어있지 않은 경우")
	@Test
	void fromFailOnNotEmpty() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.from(4), false);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.from(4), false);

		// when
		ThrowingCallable throwingCallable = () -> OrderTableGroup.from(Arrays.asList(orderTable1, orderTable2));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("그룹 해제")
	@Test
	void ungroup() {
		// given
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.from(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.from(4), true);
		OrderTableGroup orderTableGroup = OrderTableGroup.from(Arrays.asList(orderTable1, orderTable2));

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
		OrderTable orderTable1 = OrderTable.of(NumberOfGuests.from(4), true);
		OrderTable orderTable2 = OrderTable.of(NumberOfGuests.from(4), true);
		OrderTableGroup orderTableGroup = OrderTableGroup.from(Arrays.asList(orderTable1, orderTable2));
		Order.of(orderTable1,
			OrderLineItems.from(Collections.singletonList(OrderLineItem.of(
				Menu.of(
					Name.from("후라이드+후라이드"),
					Price.from(BigDecimal.valueOf(25000)),
					추천_메뉴_그룹().getId(),
					MenuProducts.from(Collections.singletonList(
						MenuProduct.of(
							Product.of(Name.from("후라이드치킨"), Price.from(BigDecimal.valueOf(17000))),
							Quantity.from(2L))))),
				Quantity.from(1L)))));

		// when
		ThrowingCallable throwingCallable = orderTableGroup::ungroup;

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}
}
