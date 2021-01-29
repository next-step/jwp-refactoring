package kitchenpos.order;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderLineTest {

    private Order order;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    public void setup() {
        this.order = this.createOrder(OrderStatus.COOKING);
    }


    private OrderLineItem createOrderLineItem(Order persistOrder) {
        OrderLineItem orderLineItem = new OrderLineItem(4);
        orderLineItem.changeOrder(persistOrder);
        Menu menu = new Menu("테스트메뉴", BigDecimal.valueOf(10000));
        menu.changeMenuGroup(this.menuGroupRepository.save(new MenuGroup("테스트메뉴그룹")));
        orderLineItem.changeMenu(this.menuRepository.save(menu));

        // when
        return this.orderLineItemRepository.save(orderLineItem);
    }

    public Order createOrder(OrderStatus orderStatus) {
        Order order = new Order();
        order.changeOrderStatus(orderStatus.name());
        order.changeOrderedTime(LocalDateTime.now());
        order.changeOrderTable(this.orderTableRepository.save(new OrderTable(3, false)));

        return this.orderRepository.save(order);
    }


    @Test
    @DisplayName("주문항목을 등록합니다.")
    void save() {
        // given
        OrderLineItem persistOrderLineItem = createOrderLineItem(this.order);

        // then
        assertThat(persistOrderLineItem.getSeq()).isNotNull();
        assertThat(persistOrderLineItem.getOrder().getId()).isEqualTo(this.order.getId());
    }

    @Test
    @DisplayName("특정 주문항목을 조회합니다.")
    void findById() {
        // given
        OrderLineItem persistOrderLineItem = createOrderLineItem(this.order);

        // when
        OrderLineItem foundOrderLineItem = this.orderLineItemRepository.findById(persistOrderLineItem.getSeq()).get();

        // then
        assertThat(foundOrderLineItem.getSeq()).isEqualTo(persistOrderLineItem.getSeq());
        assertThat(foundOrderLineItem.getOrder().getId()).isEqualTo(persistOrderLineItem.getOrder().getId());
    }


    @Test
    @DisplayName("전체 주문항목을 조회합니다.")
    void findAll() {
        // given
        createOrderLineItem(this.order);
        createOrderLineItem(this.order);

        // when
        List<OrderLineItem> foundOrderLineItems = this.orderLineItemRepository.findAll();

        // then
        assertThat(foundOrderLineItems).hasSize(2);
    }


    @Test
    @DisplayName("주문ID로 특정 주문항목을 조회합니다.")
    void findAllByOrderId() {
        // given
        createOrderLineItem(this.order);
        createOrderLineItem(this.order);

        // when
        List<OrderLineItem> foundOrderLineItems = this.orderLineItemRepository.findAllByOrderId(this.order.getId());

        // then
        assertThat(foundOrderLineItems).hasSize(2);
    }

}
