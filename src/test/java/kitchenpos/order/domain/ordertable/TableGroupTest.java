package kitchenpos.order.domain.ordertable;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menu.MenuGroup;
import kitchenpos.menu.domain.menu.MenuProduct;
import kitchenpos.menu.domain.product.Product;
import kitchenpos.order.domain.order.Order;
import kitchenpos.order.domain.order.OrderLineItem;

@DisplayName("테이블 그룹 테스트")
public class TableGroupTest {

	@DisplayName("테이블 그룹 생성")
	@Test
	void create() {
		final int numberOfGuests = 1;
		final boolean empty = true;
		OrderTable orderTable1 = new OrderTable(numberOfGuests, empty);
		OrderTable orderTable2 = new OrderTable(numberOfGuests, empty);
		TableGroup tableGroup = TableGroup.createTableGroup(Arrays.asList(orderTable1, orderTable2));

		assertThat(tableGroup).isNotNull();
	}

	@DisplayName("테이블 그룹 생성 예외: 주문 테이블 목록이 비어있음")
	@Test
	void createThrowExceptionWhenOrderTablesEmpty() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.createTableGroup(new ArrayList<>()));
	}

	@DisplayName("테이블 그룹 생성 예외: 주문 테이블 목록이 2보다 적음")
	@Test
	void createThrowExceptionWhenOrderTablesSizeLessThen2() {
		final int numberOfGuests = 1;
		final boolean empty = true;
		OrderTable orderTable1 = new OrderTable(numberOfGuests, empty);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.createTableGroup(Collections.singletonList(orderTable1)));
	}

	@DisplayName("테이블 그룹 생성 예외: 주문 테이블 중에 빈 테이블이 아닌 것이 있음")
	@Test
	void createThrowExceptionWhenHasOccupiedOrderTable() {
		final int numberOfGuests = 1;
		OrderTable orderTable1 = new OrderTable(numberOfGuests, true);
		OrderTable orderTable2 = new OrderTable(numberOfGuests, false);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.createTableGroup(Arrays.asList(orderTable1, orderTable2)));
	}

	@DisplayName("테이블 그룹 생성 예외: 주문 테이블 중에 이미 단체 지정된 것이 있음")
	@Test
	void createThrowExceptionWhenHasOrderTableAlreadyGrouped() {
		final int numberOfGuests = 1;
		OrderTable orderTable1 = new OrderTable(numberOfGuests, true);
		OrderTable orderTable2 = new OrderTable(numberOfGuests, true);
		OrderTable orderTable3 = new OrderTable(numberOfGuests, true);

		TableGroup.createTableGroup(Arrays.asList(orderTable1, orderTable2));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> TableGroup.createTableGroup(Arrays.asList(orderTable2, orderTable3)));
	}

	@DisplayName("주문 테이블의 단체 지정 삭제")
	@Test
	void ungroup() {
		final int numberOfGuests = 1;
		final boolean empty = true;
		OrderTable orderTable1 = new OrderTable(numberOfGuests, empty);
		OrderTable orderTable2 = new OrderTable(numberOfGuests, empty);
		TableGroup tableGroup = TableGroup.createTableGroup(Arrays.asList(orderTable1, orderTable2));

		tableGroup.ungroup();

		assertThat(tableGroup.getOrderTables()).isEmpty();
	}

	@DisplayName("주문 테이블의 단체 지정 삭제 예외: 주문 상태가 조리 또는 식사인 테이블이 있음")
	@Test
	void ungroupThrowExceptionOrderStatusIsCookingOrMeal() {
		final int numberOfGuests = 1;
		final boolean empty = true;
		OrderTable orderTable1 = new OrderTable(numberOfGuests, empty);
		OrderTable orderTable2 = new OrderTable(numberOfGuests, empty);
		TableGroup tableGroup = TableGroup.createTableGroup(Arrays.asList(orderTable1, orderTable2));

		OrderLineItem 주문_항목;
		Product 후라이드치킨 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
		Product 양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));
		MenuGroup 치킨세트 = new MenuGroup("후라이드앙념치킨");
		Menu 후라이드한마리_양념치킨한마리 = new Menu("후라이드+양념", BigDecimal.valueOf(32000), 치킨세트);
		MenuProduct 후라이드치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 후라이드치킨, 1);
		MenuProduct 양념치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 양념치킨, 1);
		후라이드한마리_양념치킨한마리.addMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));

		주문_항목 = new OrderLineItem(후라이드한마리_양념치킨한마리.getId(), 1L);
		List<OrderLineItem> 주문_항목_목록 = new ArrayList<>();
		주문_항목_목록.add(주문_항목);

		Order.createToCook(orderTable1, 주문_항목_목록);

		assertThatIllegalArgumentException()
			.isThrownBy(tableGroup::ungroup);
	}

}
