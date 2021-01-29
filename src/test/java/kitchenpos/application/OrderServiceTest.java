package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    public static final String COOKING = "COOKING";
    public static final String COMPLETION = "COMPLETION";
    public static final BigDecimal DEFAULT_PRICE = BigDecimal.valueOf(12_000);
    public static final String 알리오올리오 = "알리오올리오";
    public static final long DEFAULT_QUANTITY = 1L;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private TableService tableService;
    @Autowired
    private MenuService menuService;

    private MenuResponse createdMenu;

    private OrderTableResponse orderTableWithGuests;
    private OrderTableResponse orderTableWithGuests2;

    @BeforeEach
    void setUp() {
        MenuGroupResponse createMenuGroup = menuGroupService.create(new MenuGroupRequest("양식"));

        ProductResponse createdProduct = productService.create(new ProductRequest(알리오올리오, DEFAULT_PRICE));

        MenuProductRequest menuProduct = new MenuProductRequest(createdProduct.getId(), DEFAULT_QUANTITY);

        createdMenu = menuService.create(
            new MenuRequest(알리오올리오, DEFAULT_PRICE, createMenuGroup.getId(), Arrays.asList(menuProduct)));

        OrderTableResponse orderTableResponse = tableService.create(new OrderTableRequest(true));
        tableService.changeEmpty(orderTableResponse.getId(),new OrderTableRequest(orderTableResponse.getId(), orderTableResponse.getTableGroupId(), 0, false));
        orderTableWithGuests = tableService.changeNumberOfGuests(orderTableResponse.getId(),
            new OrderTableRequest(orderTableResponse.getId(), orderTableResponse.getTableGroupId(), 3, false));
        OrderTableResponse orderTableResponse2 = tableService.create(new OrderTableRequest(true));
        tableService.changeEmpty(orderTableResponse2.getId(),new OrderTableRequest(orderTableResponse2.getId(), orderTableResponse2.getTableGroupId(), 0, false));
        orderTableWithGuests2 = tableService.changeNumberOfGuests(orderTableResponse2.getId(),
            new OrderTableRequest(orderTableResponse2.getId(), orderTableResponse2.getTableGroupId(), 3, false));

    }

    @Test
    @DisplayName("order 생성")
    void order_create_test() {
        //given
        OrderLineItemRequest orderLineItem = ORDER_LINE_ITEM_생성(createdMenu.getId(), DEFAULT_QUANTITY);
        OrderRequest orderRequest = ORDER_REQUEST_생성(orderTableWithGuests, orderLineItem);

        //when
        OrderResponse createdOrder = ORDER_생성_테스트(orderRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdOrder.getId()).isNotNull();
        });
    }

    @Test
    @DisplayName("order에는 order line item이 1개 이상 존재해야한다.")
    void order_create_order_item_null_test() {
        //given
        OrderRequest orderRequest = ORDER_REQUEST_생성(orderTableWithGuests.getId(),
            Collections.emptyList());

        //when
        //then
        assertThatThrownBy(() -> {
            OrderResponse createdOrder = ORDER_생성_테스트(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("order에는 order table이 비어있으면 안된다..")
    void order_create_order_table_null_test() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(orderTableWithGuests.getId(),
            orderTableWithGuests.getTableGroupId(), 0, true);
        OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTableWithGuests.getId(), orderTableRequest);
        //when
        //then
        OrderRequest orderRequest = ORDER_REQUEST_생성(orderTableWithGuests, ORDER_LINE_ITEM_생성(createdMenu.getId(),
            DEFAULT_QUANTITY));
        assertThatThrownBy(() -> {
            OrderResponse createdOrder = ORDER_생성_테스트(orderRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("order 리스트 조회")
    void order_show_test() {
        //given
        OrderLineItemRequest orderLineItem1 = ORDER_LINE_ITEM_생성(createdMenu.getId(), DEFAULT_QUANTITY);
        OrderResponse order1 = ORDER_생성_테스트(ORDER_REQUEST_생성(orderTableWithGuests, orderLineItem1));
        OrderLineItemRequest orderLineItem2 = ORDER_LINE_ITEM_생성(createdMenu.getId(), DEFAULT_QUANTITY);
        OrderResponse order2 = ORDER_생성_테스트(ORDER_REQUEST_생성(orderTableWithGuests2, orderLineItem2));

        //when
        List<OrderResponse> list = orderService.list();

        //then
        Assertions.assertAll(() -> {
            assertThat(list)
                .extracting(OrderResponse::getId)
                .containsExactly(order1.getId(), order2.getId());

            List<OrderLineItemResponse> orderLineItemResponses = list.stream()
                .flatMap(order -> order.getOrderLineItems().stream())
                .collect(Collectors.toList());

            long count = orderLineItemResponses.stream()
                .filter(orderLineItem -> orderLineItem.getOrderId().equals(order1.getId()) &&
                    orderLineItem.getMenuId().equals(orderLineItem1.getMenuId()))
                .count();
            assertThat(count).isGreaterThan(0L);

            long count2 = orderLineItemResponses.stream()
                .filter(orderLineItem -> orderLineItem.getOrderId().equals(order1.getId()) &&
                    orderLineItem.getMenuId().equals(orderLineItem2.getMenuId()))
                .count();
            assertThat(count2).isGreaterThan(0L);
        });

    }

    @Test
    @DisplayName("특정 order의 status 변경하기")
    public void change_order_status() throws Exception {
        //Given
        OrderLineItemRequest orderLineItem1 = ORDER_LINE_ITEM_생성(createdMenu.getId(), DEFAULT_QUANTITY);
        OrderResponse order1 = ORDER_생성_테스트(ORDER_REQUEST_생성(orderTableWithGuests, orderLineItem1));
        OrderRequest cooking = ORDER_REQUEST_생성(order1.getOrderTableId(), Collections.singletonList(orderLineItem1),
            "COOKING");
        //When
        OrderResponse order = orderService.changeOrderStatus(order1.getId(), cooking);

        //Then
        assertThat(order.getOrderStatus()).isEqualTo(COOKING);
    }

    @Test
    @DisplayName("order의 status가 Completeion일 경우 변경할 수 없다.")
    void change_order_fail_status() throws Exception {
        //Given
        OrderLineItemRequest orderLineItem1 = ORDER_LINE_ITEM_생성(createdMenu.getId(), DEFAULT_QUANTITY);
        OrderResponse order1 = ORDER_생성_테스트(ORDER_REQUEST_생성(orderTableWithGuests, orderLineItem1));
        OrderRequest completion = ORDER_REQUEST_생성(order1.getOrderTableId(), Collections.singletonList(orderLineItem1),
            COMPLETION);
        orderService.changeOrderStatus(order1.getId(), completion);

        //When
        OrderRequest cooking = ORDER_REQUEST_생성(order1.getOrderTableId(), Collections.singletonList(orderLineItem1),
            COOKING);
        //Then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order1.getId(), cooking))
            .isInstanceOf(IllegalArgumentException.class);

    }

    private OrderLineItemRequest ORDER_LINE_ITEM_생성(long menuId, long defaultQuantity) {
        return new OrderLineItemRequest(menuId, defaultQuantity);
    }


    private OrderResponse ORDER_생성_테스트(OrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    private OrderRequest ORDER_REQUEST_생성(OrderTableResponse createdOrderTable, OrderLineItemRequest orderLineItem) {
        return ORDER_REQUEST_생성(createdOrderTable.getId(), Collections.singletonList(orderLineItem));
    }

    private OrderRequest ORDER_REQUEST_생성(Long orderTableId, List<OrderLineItemRequest> orderLineItem,
        String orderStatus) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItem);
    }

    private OrderRequest ORDER_REQUEST_생성(Long orderTableId, List<OrderLineItemRequest> orderLineItem) {
        return ORDER_REQUEST_생성(orderTableId, orderLineItem, null);
    }
}
