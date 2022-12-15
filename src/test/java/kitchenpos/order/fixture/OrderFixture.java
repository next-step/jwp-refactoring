package kitchenpos.order.fixture;

import static kitchenpos.menu.fixture.MenuFixture.샘플_메뉴;
import static kitchenpos.table.fixture.OrderTableFixture.채워진_테이블;

import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;

public class OrderFixture {

    public static Order 샘플_주문(){
        OrderTable 채워진_테이블 = 채워진_테이블();
        Menu 샘플_메뉴 = 샘플_메뉴();

        OrderLineItem 주문상품 = OrderLineItem.of(샘플_메뉴, 1);
        OrderLineItems 주문상품_목록 = OrderLineItems.of(Arrays.asList(주문상품));
        return Order.of(채워진_테이블, 주문상품_목록);
    }

    public static Order 테이블에_샘플_주문(OrderTable 채워진_테이블){
        Menu 샘플_메뉴 = 샘플_메뉴();

        OrderLineItem 주문상품 = OrderLineItem.of(샘플_메뉴, 1);
        OrderLineItems 주문상품_목록 = OrderLineItems.of(Arrays.asList(주문상품));
        return Order.of(채워진_테이블, 주문상품_목록);
    }

}
