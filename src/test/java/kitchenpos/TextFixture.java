package kitchenpos;

import static java.time.LocalDateTime.*;
import static java.util.Arrays.*;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

public class TextFixture {
	public static final OrderMenu 주문메뉴_후라이드 = OrderMenu.of(1L, Name.valueOf("후라이드"), Price.wonOf(10000));
	public static final OrderMenu 주문메뉴_양념 = OrderMenu.of(2L, Name.valueOf("양념"), Price.wonOf(10000));

	public static final Product 후라이드치킨 = new Product(Name.valueOf("후라이드치킨"), Price.wonOf(1000));
	public static final Product 피자 = new Product(Name.valueOf("피자"), Price.wonOf(2000));

	public static final OrderLineItem 주문항목_후라이드_1개 = new OrderLineItem(주문메뉴_후라이드, 1);
	public static final OrderLineItem 주문항목_양념_1개 = new OrderLineItem(주문메뉴_양념, 1);
	public static final OrderLineItems 주문항목들_후라이드_1개_양념_1개 = OrderLineItems.of(asList(주문항목_후라이드_1개, 주문항목_양념_1개));

	public static final OrderTable 주문테이블_주문가능_그룹X = new OrderTable(NumberOfGuests.valueOf(1), false);
	public static final Order 주문_후라이드_1개_양념_1개_조리중 = 주문테이블_주문가능_그룹X.createOrder(주문항목들_후라이드_1개_양념_1개, now());
	public static final Order 주문_후라이드_1개_양념_1개_계산완료;
	static {
		주문_후라이드_1개_양념_1개_계산완료 = 주문테이블_주문가능_그룹X.createOrder(주문항목들_후라이드_1개_양념_1개, now());
		주문_후라이드_1개_양념_1개_계산완료.complete();
	}

	public static final OrderTable 주문테이블_그룹O = new OrderTable(NumberOfGuests.valueOf(1), true);
	private static final OrderTable 주문테이블_그룹o = new OrderTable(NumberOfGuests.valueOf(1), true);
	private static final TableGroup 테이블그룹 = new TableGroup(now());
	static {
		테이블그룹.group(asList(주문테이블_그룹O, 주문테이블_그룹o));
	}
}
