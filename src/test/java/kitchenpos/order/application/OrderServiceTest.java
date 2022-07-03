package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.common.domain.PriceTest.가격_생성;
import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.order.OrderGenerator.주문_물품_생성_요청;
import static kitchenpos.order.OrderGenerator.주문_생성_요청;
import static kitchenpos.product.ProductGenerator.상품_생성;
import static kitchenpos.table.TableGenerator.주문_테이블_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private OrderTable 주문_테이블;
    private MenuGroup 메뉴_그룹;
    private Product 상품;
    private MenuProduct 메뉴_상품;
    private Menu 메뉴;

    @BeforeEach
    void setUp() {
        주문_테이블 = orderTableRepository.save(주문_테이블_생성(손님_수_생성(10)));
        메뉴_그룹 = menuGroupRepository.save(메뉴_그룹_생성("메뉴 그룹"));
        상품 = productRepository.save(상품_생성("상품", 가격_생성(1_000)));

        MenuProductRequest 메뉴_상품_요청 = 메뉴_상품_생성_요청(상품.getId(), 1L);
        MenuCreateRequest 메뉴_생성_요청 = 메뉴_생성_요청("메뉴", 1_000, 메뉴_그룹.getId(), Collections.singletonList(메뉴_상품_요청));

        메뉴 = menuService.create(메뉴_생성_요청);
    }

    @DisplayName("주문에 주문 목록이 포함되어 있지 않으면 예외가 발생해야 한다")
    @Test
    void createOrderByNotIncludeOrderLinesTest() {
        주문_생성_실패됨(() -> orderService.create(주문_생성_요청(주문_테이블.getId(), Collections.emptyList())));
    }

    @DisplayName("주문 목록의 저장된 메뉴 수와 요청한 주문의 주문 목록의 수가 다르면 예외가 발생해야 한다")
    @Test
    void createOrderByNotMatchedMenuCountAndOrderLineCountTest() {
        // given
        Long 없는_메뉴_아이디 = -1L;
        List<OrderLineItemRequest> 주문_목록_생성_요청 = Arrays.asList(
                주문_물품_생성_요청(메뉴.getId(), 1L),
                주문_물품_생성_요청(없는_메뉴_아이디, 1L)
        );

        // then
        주문_생성_실패됨(() -> orderService.create(주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청)));
    }

    @DisplayName("저장되지 않은 주문 테이블 정보로 주문이 생성되면 예외가 발생해야 한다")
    @Test
    void createOrderByNotSavedOrderTableTest() {
        // given
        Long 없는_테이블_아이디 = -1L;
        List<OrderLineItemRequest> 주문_목록_생성_요청 = Arrays.asList(
                주문_물품_생성_요청(메뉴.getId(), 1L),
                주문_물품_생성_요청(메뉴.getId(), 1L)
        );

        // then
        주문_생성_실패됨(() -> orderService.create(주문_생성_요청(없는_테이블_아이디, 주문_목록_생성_요청)));
    }

    @DisplayName("빈 주문 테이블에 주문을 추가하면 예외가 발생해야 한다")
    @Test
    void createOrderByEmptyOrderTableTest() {
        // given
        List<OrderLineItemRequest> 주문_목록_생성_요청 = Arrays.asList(
                주문_물품_생성_요청(메뉴.getId(), 1L),
                주문_물품_생성_요청(메뉴.getId(), 1L)
        );
        tableService.changeEmpty(주문_테이블.getId(), true);

        // then
        주문_생성_실패됨(() -> orderService.create(주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청)));
    }

    @DisplayName("정상 상태의 주문을 저장하면 정상 저장되어야 한다")
    @Test
    void createOrderTest() {
        // given
        List<OrderLineItemRequest> 주문_목록_생성_요청 = Arrays.asList(
                주문_물품_생성_요청(메뉴.getId(), 1L),
                주문_물품_생성_요청(메뉴.getId(), 1L)
        );
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청);
        tableService.changeEmpty(주문_테이블.getId(), false);

        // then
        Order 주문 = orderService.create(주문_생성_요청);

        // then
        주문_정상_생성됨(주문, 주문_생성_요청);
    }

    @DisplayName("주문 목록을 조회하면 정상 조회되어야 한다")
    @Test
    void findAllOrderTest() {
        // given
        tableService.changeEmpty(주문_테이블.getId(), false);
        List<OrderLineItemRequest> 주문_목록_생성_요청 = Arrays.asList(
                주문_물품_생성_요청(메뉴.getId(), 1L),
                주문_물품_생성_요청(메뉴.getId(), 1L)
        );
        List<Long> 생성된_주문_아이디들 = Arrays.asList(
                orderService.create(주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청)).getId(),
                orderService.create(주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청)).getId(),
                orderService.create(주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청)).getId(),
                orderService.create(주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청)).getId()
        );

        // when
        Orders 주문_목록_조회_리스트 = orderService.list();

        // then
        주문_목록_조회됨(주문_목록_조회_리스트, 생성된_주문_아이디들);
    }

    @DisplayName("없는 주문의 주문 상태를 변경하면 예외가 발생해야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL", "COMPLETION" })
    void changeOrderStateByNotSavedOrderTest(OrderStatus status) {
        주문_상태_변경_실패됨(() -> orderService.changeOrderStatus(0L, status));
    }

    @DisplayName("완료된 주문의 상태를 변경하면 예외가 발생해야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL", "COMPLETION" })
    void changeOrderStatueByCompletionOrderTest(OrderStatus status) {
        // given
        List<OrderLineItemRequest> 주문_목록_생성_요청 = Arrays.asList(
                주문_물품_생성_요청(메뉴.getId(), 1L),
                주문_물품_생성_요청(메뉴.getId(), 1L)
        );
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청);
        tableService.changeEmpty(주문_테이블.getId(), false);
        Order 주문 = orderService.create(주문_생성_요청);
        orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);

        // then
        주문_상태_변경_실패됨(() -> orderService.changeOrderStatus(주문.getId(), status));
    }

    @DisplayName("주문 상태 변경 시 변경한 상태로 정상 변경되어야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL", "COMPLETION" })
    void changeOrderStatusTest(OrderStatus orderStatus) {
        // given
        List<OrderLineItemRequest> 주문_목록_생성_요청 = Arrays.asList(
                주문_물품_생성_요청(메뉴.getId(), 1L),
                주문_물품_생성_요청(메뉴.getId(), 1L)
        );
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블.getId(), 주문_목록_생성_요청);
        tableService.changeEmpty(주문_테이블.getId(), false);
        Order 주문 = orderService.create(주문_생성_요청);

        // when
        Order 주문_상태_변경_결과 = orderService.changeOrderStatus(주문.getId(), orderStatus);

        // then
        주문_상태_변경_성공됨(주문_상태_변경_결과, orderStatus);
    }

    void 주문_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 주문_정상_생성됨(Order order, OrderCreateRequest request) {
        assertThat(order.getOrderTable().getId()).isEqualTo(request.getOrderTable());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems().getValue().size()).isEqualTo(request.getOrderLineItems().size());
        assertThat(order.getOrderLineItems().getValue().stream().map(orderLineItem -> orderLineItem.getMenu().getId()).collect(Collectors.toList()))
                .containsAll(request.getOrderLineItems().stream().map(OrderLineItemRequest::getMenu).collect(Collectors.toList()));
    }

    void 주문_목록_조회됨(Orders orders, List<Long> containIds) {
        assertThat(orders.getValue().size()).isGreaterThanOrEqualTo(containIds.size());
        assertThat(orders.getValue().stream().map(Order::getId).collect(Collectors.toList())).containsAll(containIds);
    }

    void 주문_상태_변경_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 주문_상태_변경_성공됨(Order order, OrderStatus orderStatus) {
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }
}
