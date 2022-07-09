package kitchenpos.domain;

import kitchenpos.dao.*;
import kitchenpos.dto.OrderLineItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("주문 테스트")
public class OrderTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;

    OrderTable 주문_테이블;

    Product 마늘치킨;
    Product 양념치킨;
    MenuGroup 점심메뉴;
    MenuProduct 점심특선_마늘치킨;
    MenuProduct 점심특선_양념치킨;
    Menu 점심특선;

    @BeforeEach
    void beforeEach() {
        주문_테이블 = orderTableRepository.save(new OrderTable(0, true));

        마늘치킨 = productRepository.save(new Product("마늘치킨", 1000));
        양념치킨 = productRepository.save(new Product("양념치킨", 1000));
        점심메뉴 = menuGroupRepository.save(MenuGroup.builder()
                                                 .name("두마리메뉴")
                                                 .build());
        점심특선_마늘치킨 = new MenuProduct(마늘치킨, 1);
        점심특선_양념치킨 = new MenuProduct(양념치킨, 1);

        점심특선 = menuRepository.save(new Menu("점심특선", 2000, 점심메뉴, Arrays.asList(점심특선_마늘치킨, 점심특선_양념치킨)));
    }

    @DisplayName("주문을 생성하면 최초의 주문 상태는 조리로 설정된다.")
    @Test
    void create() {
        // given
        List<OrderLineItemRequest> OrderLineItems = Arrays.asList(new OrderLineItemRequest(점심특선.getId(), 1));
        주문_테이블.changeEmpty(false);

        // when
        Order 주문 = orderRepository.save(new Order(주문_테이블, OrderStatus.COOKING, toOrderLineItems(Arrays.asList(점심특선), OrderLineItems)));

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Disabled
    @DisplayName("빈 테이블에는 주문할 수 없다.")
    @Test
    void create_throwException_givenEmptyTable() {
        // given
        List<OrderLineItemRequest> OrderLineItems = Arrays.asList(new OrderLineItemRequest(점심특선.getId(), 1));

        // when
        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(주문_테이블, OrderStatus.COOKING, toOrderLineItems(Arrays.asList(점심특선), OrderLineItems)));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        List<OrderLineItemRequest> OrderLineItems = Arrays.asList(new OrderLineItemRequest(점심특선.getId(), 1));
        주문_테이블.changeEmpty(false);
        Order 주문 = orderRepository.save(new Order(주문_테이블, OrderStatus.COOKING, toOrderLineItems(Arrays.asList(점심특선), OrderLineItems)));

        // when
        주문.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(orderRepository.findById(주문.getId()).get().getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("최종 주문 상태는 계산 완료에서 변경될 수 없다.")
    @Test
    void changeOrderStatus_throwException_givenCompletion() {
        // given
        List<OrderLineItemRequest> OrderLineItems = Arrays.asList(new OrderLineItemRequest(점심특선.getId(), 1));
        주문_테이블.changeEmpty(false);
        Order 주문 = orderRepository.save(new Order(주문_테이블, OrderStatus.COMPLETION, toOrderLineItems(Arrays.asList(점심특선), OrderLineItems)));

        // when
        // then
        assertThatIllegalArgumentException().isThrownBy(() -> 주문.changeOrderStatus(OrderStatus.MEAL));
    }

    private List<OrderLineItem> toOrderLineItems(List<Menu> menus, List<OrderLineItemRequest> OrderLineItems) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItem : OrderLineItems) {
            menus.stream()
                 .filter(menu -> menu.getId().equals(orderLineItem.getMenuId()))
                 .findFirst()
                 .ifPresent(menu -> orderLineItems.add(new OrderLineItem(menu, orderLineItem.getQuantity())));
        }
        return orderLineItems;
    }
}
