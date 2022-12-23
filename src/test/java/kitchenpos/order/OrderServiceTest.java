package kitchenpos.order;

import static kitchenpos.menu.MenuFixture.더블강정치킨;
import static kitchenpos.order.OrderFixture.주문;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;
import kitchenpos.menu.MenuFixture;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 생성")
    void createOrder() {
        //given
        when(menuDao.findById(any()))
            .thenReturn(Optional.of(더블강정치킨));
        when(orderTableDao.findById(any()))
            .thenReturn(Optional.of(일번테이블));
        when(orderDao.save(any())).then(returnsFirstArg());

        //when
        OrderResponse orderResponse = orderService.create(from(주문));

        //then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(orderResponse.getOrderedTime()).isNotNull();
    }

    @Test
    void 주문_항목이_없으면_주문중_에러_발생() {
        //when & then
        Assertions.assertThatThrownBy(() -> orderService.create(from(new Order())))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 항목 정보 없음");
    }

    @Test
    void 주문_항목중_메뉴에_없는항목이_있으면_에러_발생() {
        //given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(new OrderLineItem()));
        when(menuDao.findById(any())).thenReturn(Optional.empty());

        //when & then
        Assertions.assertThatThrownBy(() -> orderService.create(from(order)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_없으면_에러_발생() {
        //given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(new OrderLineItem()));
        when(menuDao.findById(any())).thenReturn(Optional.of(더블강정치킨));
        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        //when & then
        Assertions.assertThatThrownBy(() -> orderService.create(from(order)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이면_에러_발생() {
        //given
        Order order = new Order();
        order.setOrderLineItems(Collections.singletonList(new OrderLineItem()));
        when(menuDao.findById(any())).thenReturn(Optional.of(더블강정치킨));

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        //when & then
        Assertions.assertThatThrownBy(() -> orderService.create(from(order)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    public static final OrderLineItem 주문항목 = new OrderLineItem(1L, null, 더블강정치킨,
        2L);
    public static final Order 주문 = new Order(1L, 일번테이블.getId(), null, null,
        Collections.singletonList(주문항목));

    private OrderRequest from(Order order){
        return new OrderRequest(order.getOrderTableId(), order.getOrderStatus(), from(order.getOrderLineItems()));
    }

    private List<OrderLineItemRequest> from(List<OrderLineItem> orderLineItem){
        if(Objects.isNull(orderLineItem)){
            return null;
        }
        return orderLineItem.stream()
            .map(domain -> new OrderLineItemRequest(domain.menuId(), domain.getQuantity()))
            .collect(Collectors.toList());
    }
}
