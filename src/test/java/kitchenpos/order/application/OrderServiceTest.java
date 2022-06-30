package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

//    @DisplayName("주문 목록을 조회하면 정상 조회되어야 한다")
//    @Test
//    void findAllOrderTest() {
//        // given
//        List<Order> 주문_리스트 = Arrays.asList(
//                주문_생성(0L, OrderStatus.COOKING, Collections.emptyList()),
//                주문_생성(0L, OrderStatus.COOKING, Collections.emptyList()),
//                주문_생성(0L, OrderStatus.COOKING, Collections.emptyList()),
//                주문_생성(0L, OrderStatus.COOKING, Collections.emptyList())
//        );
//        when(orderRepository.findAll()).thenReturn(주문_리스트);
//
//        // when
//        List<Order> 주문_목록_조회_리스트 = orderService.list();
//
//        // then
//        assertThat(주문_목록_조회_리스트.size()).isGreaterThanOrEqualTo(주문_리스트.size());
//        assertThat(주문_목록_조회_리스트).containsAll(주문_리스트);
//    }
//
//    @DisplayName("없는 주문의 주문 상태를 변경하면 예외가 발생해야 한다")
//    @Test
//    void changeOrderStateByNotSavedOrderTest() {
//        주문_상태_변경_실패됨(() -> orderService.changeOrderStatus(0L, any()));
//    }
//
//    @DisplayName("완료된 주문의 상태를 변경하면 예외가 발생해야 한다")
//    @Test
//    void changeOrderStatueByCompletionOrderTest() {
//        // given
//        Order 주문 = 주문_생성(0L, OrderStatus.COMPLETION, Collections.emptyList());
//        when(orderRepository.findById(any())).thenReturn(Optional.of(주문));
//
//        // then
//        주문_상태_변경_실패됨(() -> orderService.changeOrderStatus(주문.getId(), 주문));
//    }
//
//    @DisplayName("주문 상태 변경 시 변경한 상태로 정상 변경되어야 한다")
//    @Test
//    void changeOrderStatusTest() {
//        // given
//        Order 주문 = 주문_생성(0L, OrderStatus.COOKING, Collections.emptyList());
//        Order 변경할_주문 = 주문_생성(0L, OrderStatus.MEAL, Collections.emptyList());
//        when(orderRepository.findById(any())).thenReturn(Optional.of(주문));
//        when(orderRepository.save(any())).then(it -> it.getArgument(0));
//
//        // when
//        Order 주문_상태_변경_결과 = orderService.changeOrderStatus(0L, 변경할_주문);
//
//        // then
//        주문_상태_변경_성공됨(주문_상태_변경_결과.getOrderStatus(), 변경할_주문.getOrderStatus());
//    }
//
    void 주문_생성_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }

    void 주문_정상_생성됨(Order order, OrderCreateRequest request) {
        assertThat(order.getOrderTable().getId()).isEqualTo(request.getOrderTable());
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderLineItems().size()).isEqualTo(request.getOrderLineItems().size());
        assertThat(order.getOrderLineItems().stream().map(orderLineItem -> orderLineItem.getMenu().getId()).collect(Collectors.toList()))
                .containsAll(request.getOrderLineItems().stream().map(OrderLineItemRequest::getMenu).collect(Collectors.toList()));
    }

//    void 주문_상태_변경_실패됨(Runnable runnable) {
//        assertThatIllegalArgumentException().isThrownBy(runnable::run);
//    }
//
//    void 주문_상태_변경_성공됨(OrderStatus sourceStatus, OrderStatus targetSource) {
//        assertThat(sourceStatus).isEqualTo(targetSource);
//    }
}
