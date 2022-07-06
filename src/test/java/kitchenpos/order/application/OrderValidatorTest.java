package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.exception.OrderLineItemException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.factory.OrderFixture.*;
import static kitchenpos.factory.OrderFixture.주문_메뉴_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @InjectMocks
    OrderValidator orderValidator;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    MenuRepository menuRepository;

    Order 내주문;

    OrderTable 주문테이블;

    OrderLineItem 주문상품1;
    OrderLineItem 주문상품2;

    MenuGroup 메뉴그룹;

    Menu 주문메뉴1;
    Menu 주문메뉴2;

    @Test
    @DisplayName("주문 생성 시 유효한 테이블 조회 (Happy Path)")
    void tableValidIsEmpty() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹.getId());
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹.getId());
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2.getId(), 2L);
        내주문 = new Order(1L, 주문테이블, new ArrayList<OrderLineItem>());
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), Arrays.asList(주문상품1Request, 주문상품2Request));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));

        //then
        orderValidator.tableValidIsEmpty(내주문Request.getOrderTableId());
    }

    @Test
    @DisplayName("주문 생성 시 해당 테이블 ID로 유효한 테이블이 없으면 안된다")
    void tableValidIsEmptyInvalidTable() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹.getId());
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹.getId());
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2.getId(), 2L);
        내주문 = new Order(1L, 주문테이블, new ArrayList<OrderLineItem>());
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), Arrays.asList(주문상품1Request, 주문상품2Request));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            orderValidator.tableValidIsEmpty(내주문Request.getOrderTableId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 생성 시 테이블이 비어있으면 안된다.")
    void tableValidIsEmptyIsEmpty() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹.getId());
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹.getId());
        주문테이블 = 주문테이블_생성(1L, 2, true);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2.getId(), 2L);
        내주문 = new Order(1L, 주문테이블, new ArrayList<OrderLineItem>());
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), Arrays.asList(주문상품1Request, 주문상품2Request));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));

        //then
        assertThatThrownBy(() -> {
            orderValidator.tableValidIsEmpty(내주문Request.getOrderTableId());
        }).isInstanceOf(OrderTableException.class)
        .hasMessageContaining(OrderTableException.ORDER_TABLE_IS_EMPTY_MSG);
    }

    @Test
    @DisplayName("주문상품 유효성 검사 (Happy Path)")
    void orderLineItemsValidation() {
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(1L, 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(2L, 1L);
        given(menuRepository.countByIdIn(anyList())).willReturn(2);

        orderValidator.orderLineItemsValidation(Arrays.asList(주문상품1Request, 주문상품2Request));
    }

    @Test
    @DisplayName("주문상품이 존재하지 않은 경우")
    void orderLineItemsInvalidEmptyList() {
        assertThatThrownBy(() -> {
            orderValidator.orderLineItemsValidation(Arrays.asList());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효한 주문상품의 갯수가 맞지 않으면 안된다.")
    void orderLineItemsInvalidProducts() {
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(1L, 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(2L, 1L);
        given(menuRepository.countByIdIn(anyList())).willReturn(1);

        assertThatThrownBy(() -> {
            orderValidator.orderLineItemsValidation(Arrays.asList(주문상품1Request, 주문상품2Request));
        }).isInstanceOf(OrderLineItemException.class)
            .hasMessageContaining(OrderLineItemException.ORDER_LINE_ITEM_SIZE_INVALID);
    }
}