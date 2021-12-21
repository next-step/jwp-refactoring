package kitchenpos.fixtures;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemSaveRequest;
import kitchenpos.dto.OrderSaveRequest;
import kitchenpos.dto.OrderStatusUpdateRequest;
import org.assertj.core.util.Lists;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.fixtures.OrderLineItemFixtures.주문정보_1개_등록요청;
import static kitchenpos.fixtures.OrderTableFixtures.주문가능_다섯명테이블;
import static kitchenpos.fixtures.ProductFixtures.양념치킨;
import static kitchenpos.fixtures.ProductFixtures.후라이드;

/**
 * packageName : kitchenpos.fixtures
 * fileName : OrderFixtures
 * author : haedoang
 * date : 2021/12/18
 * description :
 */
public class OrderFixtures {
    public static Order 후라이드반양념반_두개_주문() {
        BigDecimal 메뉴가격 = new BigDecimal(32000);
        MenuProduct 양념치킨메뉴상품 = new MenuProduct(양념치킨(), 1L);
        MenuProduct 후라이드메뉴상품 = new MenuProduct(후라이드(), 1L);
        Menu 후라이드반양념반메뉴 = new Menu("후라이드반양념반메뉴", 메뉴가격, 메뉴그룹("반반메뉴"), Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품));
        OrderLineItem 후라이드양념반두개 = new OrderLineItem(후라이드반양념반메뉴, 2L);

        return new Order(주문가능_다섯명테이블(), Lists.newArrayList(후라이드양념반두개));
    }

    public static OrderSaveRequest 주문등록요청() {
        return new OrderSaveRequest(1L, Lists.newArrayList(주문정보_1개_등록요청()));
    }

    public static OrderSaveRequest 주문등록요청(Long orderTableId, List<OrderLineItemSaveRequest> orderLineItemSaveRequests) {
        return new OrderSaveRequest(orderTableId, orderLineItemSaveRequests);
    }

    public static OrderStatusUpdateRequest 식사중으로_변경요청() {
        return new OrderStatusUpdateRequest(OrderStatus.MEAL);
    }

    public static OrderStatusUpdateRequest 식사완료로_변경요청() {
        return new OrderStatusUpdateRequest(OrderStatus.COMPLETION);
    }
}
