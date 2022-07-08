package kitchenpos.utils.generator;

import static kitchenpos.ui.OrderRestControllerTest.ORDER_API_BASE_URL;
import static kitchenpos.ui.OrderRestControllerTest.UPDATE_ORDER_STATUS_API_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;
import static kitchenpos.utils.MockMvcUtil.putRequestBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.CreateOrderTableItemRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class OrderFixtureGenerator {

    public static Order 주문_생성(
        final OrderTable savedOrderTable,
        final Menu... savedMenus
    ) {
        return new Order(savedOrderTable.getId(), 주문_항목_목록_생성(savedMenus), LocalDateTime.now());
    }

    private static List<OrderLineItem> 주문_항목_목록_생성(Menu... menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Menu menu : menus) {
            OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    public static CreateOrderRequest 주문_생성_요청_객체(
        final OrderTable savedOrderTable,
        final Menu... savedMenus
    ) {
        return new CreateOrderRequest(savedOrderTable.getId(), 주문_항목_목록_생성_요청_객체(savedMenus));
    }

    private static List<CreateOrderTableItemRequest> 주문_항목_목록_생성_요청_객체(Menu... menus) {
        List<CreateOrderTableItemRequest> orderLineItemsRequests = new ArrayList<>();
        for (Menu menu : menus) {
            CreateOrderTableItemRequest createOrderTableItemRequest = new CreateOrderTableItemRequest(menu.getId(), 1L);
            orderLineItemsRequests.add(createOrderTableItemRequest);
        }
        return orderLineItemsRequests;
    }

    public static CreateOrderRequest 주문_생성_요청_객체(
        final OrderTableResponse savedOrderTable,
        final MenuResponse... savedMenus
    ) {
        return new CreateOrderRequest(savedOrderTable.getId(), 주문_항목_목록_생성_요청_객체(savedMenus));
    }

    private static List<CreateOrderTableItemRequest> 주문_항목_목록_생성_요청_객체(MenuResponse... menus) {
        List<CreateOrderTableItemRequest> orderLineItemsRequests = new ArrayList<>();
        for (MenuResponse menu : menus) {
            CreateOrderTableItemRequest createOrderTableItemRequest = new CreateOrderTableItemRequest(menu.getId(), 1L);
            orderLineItemsRequests.add(createOrderTableItemRequest);
        }
        return orderLineItemsRequests;
    }

    public static MockHttpServletRequestBuilder 주문_생성_요청(
        final OrderTableResponse savedOrderTable,
        final MenuResponse... savedMenus
    ) throws Exception {
        return postRequestBuilder(ORDER_API_BASE_URL, 주문_생성_요청_객체(savedOrderTable, savedMenus));
    }

    public static MockHttpServletRequestBuilder 주문_상태_변경_요청(
        final ChangeOrderStatusRequest updateOrderStatusRequest,
        final Long savedOrderId
    ) throws Exception {
        return putRequestBuilder(UPDATE_ORDER_STATUS_API_URL_TEMPLATE, updateOrderStatusRequest, savedOrderId);
    }
}
