package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderLineItemFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.application.order.OrderService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class OrderServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderService orderService;

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;
    private OrderTable 주문_개인테이블;
    private OrderTable 빈_개인테이블;
    private Order 불고기_주문;
    private OrderLineItem 불고기_주문항목;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹);

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기, 돼지고기, 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기, 공기밥, 1L);
        불고기.addMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        주문_개인테이블 = OrderTableFixtureFactory.create(1L, false);
        빈_개인테이블 = OrderTableFixtureFactory.create(2L, true);
        불고기_주문 = OrderFixtureFactory.create(1L, 주문_개인테이블.getId(), OrderStatus.COOKING);

        불고기_주문항목 = OrderLineItemFixtureFactory.create(1L, 불고기_주문.getId(), 불고기.getId(), 1L);
        불고기_주문.addOrderLineItems(Arrays.asList(불고기_주문항목));

        고기_메뉴그룹 = menuGroupRepository.save(고기_메뉴그룹);
        돼지고기 = productRepository.save(돼지고기);
        공기밥 = productRepository.save(공기밥);
        불고기 = menuRepository.save(불고기);

        주문_개인테이블 = orderTableRepository.save(주문_개인테이블);
        빈_개인테이블 = orderTableRepository.save(빈_개인테이블);
        불고기_주문 = orderRepository.save(불고기_주문);
    }

    @DisplayName("Order 를 등록한다.")
    @Test
    void create1() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests =
            Arrays.asList(OrderLineItemRequest.of(불고기_주문항목.getMenuId(), 불고기_주문항목.getQuantity().getValue()));
        OrderRequest orderRequest = OrderRequest.of(주문_개인테이블.getId(),
                                                    OrderStatus.COOKING,
                                                    orderLineItemRequests);

        // when
        OrderResponse orderResponse = orderService.create(orderRequest);

        // then
        Order findOrder = orderRepository.findById(orderResponse.getId()).get();
        assertThat(orderResponse).isEqualTo(OrderResponse.from(findOrder));
    }

    @DisplayName("Order 를 등록 시, OrderLineItem 이 없으면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        OrderRequest orderRequest = OrderRequest.of(주문_개인테이블.getId(), OrderStatus.COOKING, Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("Order 를 등록 시, OrderLineItem 의 Menu 가 메뉴에 존재하지 않으면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests =
            Arrays.asList(OrderLineItemRequest.of(0L, 불고기_주문항목.getQuantity().getValue()));
        OrderRequest orderRequest = OrderRequest.of(주문_개인테이블.getId(),
                                                    OrderStatus.COOKING,
                                                    orderLineItemRequests);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> orderService.create(orderRequest));
    }

    @DisplayName("Order 를 등록 시, 주문을 한 OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests =
            Arrays.asList(OrderLineItemRequest.of(불고기_주문항목.getMenuId(), 불고기_주문항목.getQuantity().getValue()));
        OrderRequest orderRequest = OrderRequest.of(0L,
                                                    OrderStatus.COOKING,
                                                    orderLineItemRequests);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> orderService.create(orderRequest));
    }

    @DisplayName("Order 를 등록 시, OrderTable 이 빈(empty) 상태면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests =
            Arrays.asList(OrderLineItemRequest.of(불고기_주문항목.getMenuId(), 불고기_주문항목.getQuantity().getValue()));
        OrderRequest orderRequest = OrderRequest.of(빈_개인테이블.getId(), OrderStatus.COOKING, orderLineItemRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("Order 목록을 조회할 수 있다.")
    @Test
    void findList() {
        // when
        List<OrderResponse> orderResponses = orderService.list();

        // then
        assertThat(orderResponses).contains(OrderResponse.from(불고기_주문));
    }

    @DisplayName("Order 의 상태를 변경한다.")
    @Test
    void update1() {
        // given
        OrderRequest orderRequest = OrderRequest.of(OrderStatus.MEAL, Collections.emptyList());

        // when
        OrderResponse orderResponse = orderService.changeOrderStatus(불고기_주문.getId(), orderRequest);

        // then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("완료된 Order 의 상태를 변경하면 예외가 발생한다.")
    @Test
    void update2() {
        // given
        OrderRequest orderRequest = OrderRequest.of(OrderStatus.MEAL, Collections.emptyList());
        불고기_주문.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(불고기_주문.getId(),
                                                                                             orderRequest));
    }
}