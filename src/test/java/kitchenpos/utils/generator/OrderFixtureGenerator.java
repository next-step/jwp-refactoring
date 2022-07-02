package kitchenpos.utils.generator;

import static kitchenpos.ui.OrderRestControllerTest.ORDER_API_BASE_URL;
import static kitchenpos.ui.OrderRestControllerTest.UPDATE_ORDER_STATUS_API_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;
import static kitchenpos.utils.MockMvcUtil.putRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class OrderFixtureGenerator {

    public static Order generateOrder(
        final OrderTable savedOrderTable,
        final Menu... savedMenus
    ) {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(generateOrderLineItems(savedMenus));
        order.setOrderStatus(OrderStatus.COOKING.name());
        return order;
    }

    private static List<OrderLineItem> generateOrderLineItems(Menu... menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(menu.getId());
            orderLineItem.setQuantity(1);
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    public static MockHttpServletRequestBuilder 주문_생성_요청(
        final OrderTable savedOrderTable,
        final Menu... savedMenus
    ) throws Exception {
        return postRequestBuilder(ORDER_API_BASE_URL, generateOrder(savedOrderTable, savedMenus));
    }

    public static MockHttpServletRequestBuilder 주문_상태_변경_요청(
        final Order updateOrderStatusRequest,
        final Long savedOrderId
    ) throws Exception {
        return putRequestBuilder(UPDATE_ORDER_STATUS_API_URL_TEMPLATE, updateOrderStatusRequest, savedOrderId);
    }
}
