package kitchenpos.dto;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderLineItemDtos;

@DisplayName("주문항목 전문 관련")
public class OrderLineItemDtosTest {
    @DisplayName("메뉴 아이디만 조회된다.")
    @Test
    void search_menuIds() {
        // given
        OrderLineItemDtos orderLineItemDtos = OrderLineItemDtos.of(List.of(OrderLineItemDto.of(1L, 1L), OrderLineItemDto.of(2L, 2L)));        

        // when 
        List<Long> menuIds = orderLineItemDtos.getMenuIds();

        // then
        Assertions.assertThat(menuIds).isEqualTo(List.of(1L, 2L));
    }
    
    @DisplayName("주문항목이 생성된다.")
    @Test
    void create_orderLineItem() {
        // given
        OrderLineItemDtos orderLineItemDtos = OrderLineItemDtos.of(List.of(OrderLineItemDto.of(1L, 1L), OrderLineItemDto.of(2L, 2L)));        

        Orders order = Orders.of(OrderTable.of(2, false), OrderStatus.COOKING);
        List<Menu> menus = List.of(Menu.of(1L, "메뉴1", Price.of(1_800)), Menu.of(2L, "메뉴2", Price.of(3_800)));

        // when 
        List<OrderLineItem> orderLineItems = orderLineItemDtos.createOrderLineItem(order, menus);

        // then
        assertAll(
            () -> Assertions.assertThat(orderLineItems).hasSize(2),
            () -> Assertions.assertThat(orderLineItems.get(0).getOrder().getOrderTable().getNumberOfGuests()).isEqualTo(2),
            () -> Assertions.assertThat(orderLineItems.get(0).getOrder().getOrderTable().isEmpty()).isFalse(),
            () -> Assertions.assertThat(orderLineItems.get(0).getOrder().getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> Assertions.assertThat(orderLineItems.get(0).getMenu().getName()).isEqualTo("메뉴1"),
            () -> Assertions.assertThat(orderLineItems.get(0).getMenu().getPrice()).isEqualTo(Price.of(1_800)),
            () -> Assertions.assertThat(orderLineItems.get(0).getQuantity()).isEqualTo(1),
            
            () -> Assertions.assertThat(orderLineItems.get(1).getOrder().getOrderTable().getNumberOfGuests()).isEqualTo(2),
            () -> Assertions.assertThat(orderLineItems.get(1).getOrder().getOrderTable().isEmpty()).isFalse(),
            () -> Assertions.assertThat(orderLineItems.get(1).getOrder().getOrderStatus()).isEqualTo(OrderStatus.COOKING),
            () -> Assertions.assertThat(orderLineItems.get(1).getMenu().getName()).isEqualTo("메뉴2"),
            () -> Assertions.assertThat(orderLineItems.get(1).getMenu().getPrice()).isEqualTo(Price.of(3_800)),
            () -> Assertions.assertThat(orderLineItems.get(1).getQuantity()).isEqualTo(2)  
        );
    }
}