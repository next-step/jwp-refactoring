package kitchenpos.utils.generator;

import static kitchenpos.ui.OrderRestControllerTest.ORDER_API_BASE_URL;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class OrderFixtureGenerator {

    public static Order generateOrder(
        final OrderTable savedOrderTable,
        final Menu... savedMenus
    ) {
        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(generateOrderLineItems(savedMenus));
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
}
