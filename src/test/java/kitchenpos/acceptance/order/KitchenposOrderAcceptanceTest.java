package kitchenpos.acceptance.order;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.presentation.MenuTest;
import kitchenpos.presentation.OrderTest;
import kitchenpos.presentation.TableTest;
import kitchenpos.presentation.TableGroupTest;
import kitchenpos.testassistance.config.TestConfig;

public class KitchenposOrderAcceptanceTest extends TestConfig {
    @DisplayName("한테이블대한 손님들이 음식을 주문한다.")
    @Test
    void order_oneTable() {
        // given
        OrderTable 손님있는_테이블 = 테이블에_손님이앉음(2);

        // when
        Menu[] 메뉴들 = MenuTest.메뉴_조회요청().as(Menu[].class);
        List<OrderLineItem> 주문명세서 = 주문명세서_생성(List.of(메뉴들[0], 메뉴들[1]));
        Order 주문 = 테이블_주문요청(손님있는_테이블, 주문명세서);

        // then
        테이블_주문됨(손님있는_테이블, 주문명세서, 주문);
    }

    @DisplayName("여러 테이블의 손님이 음식을 주문한다.")
    @Test
    void order_multiTable() {
        // when
        OrderTable 손님있는_테이블1 = 테이블에_손님이앉음(2);
        OrderTable 손님있는_테이블2 = 테이블에_손님이앉음(4);

        TableGroup 단체결제지정 = new TableGroup();
        단체결제지정.setOrderTables(List.of(손님있는_테이블1, 손님있는_테이블2));

        TableGroupTest.단체지정_저장요청(단체결제지정);

        Menu[] 메뉴들 = MenuTest.메뉴_조회요청().as(Menu[].class);

        List<OrderLineItem> 주문명세서 = 주문명세서_생성(List.of(메뉴들[0], 메뉴들[1]));

        // then
        Order 주문1 = 테이블_주문요청(손님있는_테이블1, 주문명세서);
        Order 주문2 = 테이블_주문요청(손님있는_테이블2, 주문명세서);
        
        // then
        테이블_주문됨(손님있는_테이블1, 주문명세서, 주문1);
        테이블_주문됨(손님있는_테이블2, 주문명세서, 주문2);
    }

    @DisplayName("주문된 음식을 계산한다.")
    @Test
    void order_priceCalculator() {
        // given
        OrderTable 손님있는_테이블 = 테이블에_손님이앉음(2);

        // when
        Menu[] 메뉴들 = MenuTest.메뉴_조회요청().as(Menu[].class);
        List<OrderLineItem> 주문명세서 = 주문명세서_생성(List.of(메뉴들[0], 메뉴들[1]));
        Order 주문 = 테이블_주문요청(손님있는_테이블, 주문명세서);

        // then
        테이블_주문됨(손님있는_테이블, 주문명세서, 주문);

        // when
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        Order 결재된_주문 = OrderTest.주문_상태변경요청(주문).as(Order.class);

        // then
        Assertions.assertThat(결재된_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    private void 테이블_주문됨(OrderTable 손님있는_테이블, List<OrderLineItem> 주문명세서, Order 주문) throws MultipleFailuresError {
        Assertions.assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        Assertions.assertThat(주문.getOrderTableId()).isEqualTo(손님있는_테이블.getId());

        assertAll(
            () -> Assertions.assertThat(주문.getOrderLineItems().get(0).getOrderId()).isEqualTo(주문.getId()),
            () -> Assertions.assertThat(주문.getOrderLineItems().get(0).getMenuId()).isEqualTo(주문명세서.get(0).getMenuId()),
            () -> Assertions.assertThat(주문.getOrderLineItems().get(0).getQuantity()).isEqualTo(주문명세서.get(0).getQuantity())
        );

        assertAll(
            () -> Assertions.assertThat(주문.getOrderLineItems().get(1).getOrderId()).isEqualTo(주문.getId()),
            () -> Assertions.assertThat(주문.getOrderLineItems().get(1).getMenuId()).isEqualTo(주문명세서.get(1).getMenuId()),
            () -> Assertions.assertThat(주문.getOrderLineItems().get(1).getQuantity()).isEqualTo(주문명세서.get(1).getQuantity())
        );
    }

    private List<OrderLineItem> 주문명세서_생성(List<Menu> menus) {
        List<OrderLineItem> 주문명세서 = new ArrayList<>();

        for (final Menu menu : menus) {
            주문명세서.add(주문항목_생성(menu));
        }

        return 주문명세서;
    }

    private OrderLineItem 주문항목_생성(final Menu menu) {
        OrderLineItem 주문항목 = new OrderLineItem();
        주문항목.setMenuId(menu.getId());
        주문항목.setQuantity(1L);
        return 주문항목;
    }

    private Order 테이블_주문요청(OrderTable orderTable, List<OrderLineItem> OrderSpecification) {
        // when
        Order 주문 = 주문_생성(orderTable, OrderSpecification);

        return OrderTest.주문_저장요청(주문).as(Order.class);
    }

    private Order 주문_생성(OrderTable orderTable, List<OrderLineItem> OrderSpecification) {
        Order 주문 = new Order();
        주문.setOrderTableId(orderTable.getId());
        주문.setOrderLineItems(OrderSpecification);
        return 주문;
    }

    private OrderTable 테이블에_손님이앉음(int numberOfGuests) {
        List<OrderTable> 손님없는_테이블들 = 반테이블들_조회됨();

        OrderTable 손님있는_테이블 = 손님있는_테이블_생성(numberOfGuests, 손님없는_테이블들);

        손님있는_테이블 = TableTest.주문테이블_빈테이블_변경요청(손님있는_테이블).as(OrderTable.class);
        손님있는_테이블 = TableTest.주문테이블_고객수_변경요청(손님있는_테이블).as(OrderTable.class);

        return 손님있는_테이블;
    }

    private OrderTable 손님있는_테이블_생성(int numberOfGuests, List<OrderTable> 손님없는_테이블들) {
        OrderTable 손님있는_테이블 = 손님없는_테이블들.get(0);
        손님있는_테이블.setNumberOfGuests(numberOfGuests);
        손님있는_테이블.setEmpty(false);
        return 손님있는_테이블;
    }

    private List<OrderTable> 반테이블들_조회됨() {
        return List.of(TableTest.주문테이블_조회요청().as(OrderTable[].class)).stream()
                    .filter(OrderTable::isEmpty)
                    .collect(Collectors.toList());
    }
}
