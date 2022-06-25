package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuTestFixture menuTestFixture;

    private OrderTable 테이블_A;
    private OrderLineItem 후라이드_양념_세트_주문;

    @BeforeEach
    void setUp() {
        super.setUp();

        Menu 후라이드_양념_세트 = menuTestFixture.후라이드_양념_세트_가져오기();

        테이블_A = this.orderTableRepository.save(new OrderTable(4, false));
        후라이드_양념_세트_주문 = new OrderLineItem(후라이드_양념_세트.getId(), 1);
    }

    @Test
    @DisplayName("주문시 주문정보가 생성된디.")
    void create() {
        OrderResponse orderResponse = createOrder(테이블_A.getId(), 후라이드_양념_세트_주문);

        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(orderResponse.getOrderedTime()).isNotNull();
        assertThat(orderResponse.getOrderLineItems()).hasSize(1);
    }

    @TestFactory
    @DisplayName("주문 생성시 예외처리를 검증한다.")
    Stream<DynamicTest> createFail() {
        return Stream.of(
            DynamicTest.dynamicTest("주문한 메뉴가 0개일 경우", () -> {
                assertThatIllegalArgumentException()
                    .isThrownBy(() -> createOrder(테이블_A.getId()));
            }),
            DynamicTest.dynamicTest("주문한 메뉴가 실제 존재하지 않는 경우", () -> {
                assertThatIllegalArgumentException()
                    .isThrownBy(() -> createOrder(테이블_A.getId(), new OrderLineItem(Long.MAX_VALUE, 1)));
            }),
            DynamicTest.dynamicTest("테이블 정보가 없는 경우", () -> {
                assertThatExceptionOfType(InvalidDataAccessApiUsageException.class)
                    .isThrownBy(() -> createOrder(null, 후라이드_양념_세트_주문));
            })
        );
    }

    @Test
    @DisplayName("주문을 모두 조회한다.")
    void list() {
        OrderResponse 주문 = createOrder(테이블_A.getId(), 후라이드_양념_세트_주문);
        OrderResponse 주문_추가 = createOrder(테이블_A.getId(), 후라이드_양념_세트_주문);

        List<OrderResponse> list = this.orderService.list();

        assertThat(list).containsAll(Arrays.asList(주문, 주문_추가));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        OrderResponse order = createOrder(테이블_A.getId(), 후라이드_양념_세트_주문);

        OrderResponse response = this.orderService.changeOrderStatus(order.getId(), new OrderRequest(OrderStatus.MEAL));

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }


    @Test
    @DisplayName("주문 상태가 완료된 건 변경할 수 없다.")
    void changeOrderStatusFail() {
        OrderResponse order = createOrder(테이블_A.getId(), 후라이드_양념_세트_주문);
        this.orderService.changeOrderStatus(order.getId(), new OrderRequest(OrderStatus.COMPLETION));

        assertThatIllegalArgumentException()
            .isThrownBy(() -> this.orderService.changeOrderStatus(order.getId(), new OrderRequest(OrderStatus.COOKING)));
    }

    private OrderResponse createOrder(Long tableId, OrderLineItem... orderLineItem) {
        return this.orderService.create(new OrderRequest(tableId, OrderLineItemRequest.of(Arrays.asList(orderLineItem))));
    }

}
