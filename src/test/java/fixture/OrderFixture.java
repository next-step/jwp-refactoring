package fixture;

import static fixture.OrderTableFixture.*;

import order.domain.*;

public class OrderFixture {
    public static final Order 주문_첫번째 = Order.from(주문테이블_4명);
    public static final Order 주문_두번째 = Order.from(주문테이블_4명);
}
