package kitchenpos.ordertable.application;

import kitchenpos.common.exception.NegativeNumberOfGuestsException;
import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.common.exception.OrderStatusNotProcessingException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuId;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("테이블 그룹 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTableService orderTableService;

    private OrderTable 주문_테이블_1번;
    private OrderTable 빈_주문_테이블;
    private Order 주문;
    private Menu 짜장면;
    private MenuId 짜장면_ID;
    private OrderLineItem 짜장면_주문1;
    private OrderLineItem 짜장면_주문2;
    private OrderTableRequest 주문_테이블_1번_요청;
    private OrderTableRequest 빈_주문_테이블_요청;
    private MenuProduct 짜장면_곱배기;
    private MenuProduct 짜장면_보통;
    private Product 짜장면_상품;

    @BeforeEach
    void setUp() {
        orderTableService = new OrderTableService(orderTableRepository);

        짜장면_상품 = new Product("짜장면", new BigDecimal(1000));
        짜장면_곱배기 = new MenuProduct(1L, 짜장면, 짜장면_상품, 2);
        짜장면_보통 = new MenuProduct(1L, 짜장면, 짜장면_상품, 1);
        짜장면 = new Menu("짜장면", 10000, new MenuGroup(), Lists.newArrayList(짜장면_곱배기, 짜장면_보통));
        짜장면_ID = new MenuId(짜장면.getId());

        짜장면_주문1 = new OrderLineItem(주문, 짜장면_ID, 10);
        짜장면_주문2 = new OrderLineItem(주문, 짜장면_ID, 3);

        주문_테이블_1번 = new OrderTable(3);
        주문 = new Order(주문_테이블_1번, Lists.newArrayList(짜장면_주문1, 짜장면_주문2));
        주문테이블에_주문_추가(주문_테이블_1번, 주문);

        빈_주문_테이블 = new OrderTable(0);

        주문_테이블_1번_요청 = new OrderTableRequest(3);
        빈_주문_테이블_요청 = new OrderTableRequest(0);
    }

    @DisplayName("새로운 테이블을 등록한다.")
    @Test
    void createTableTest() {
        when(orderTableRepository.save(any())).thenReturn(주문_테이블_1번);

        // when
        final OrderTableResponse createdOrderTable = 주문_테이블_요청한다(주문_테이블_1번_요청);

        // then
        assertAll(
                () -> assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(3),
                () -> assertThat(createdOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void getListTableTest() {
        // given
        List<OrderTable> 주문_테이블_목록 = Lists.newArrayList(new OrderTable(new TableGroup(), 3), new OrderTable(new TableGroup(), 7));
        when(orderTableRepository.findAll()).thenReturn(주문_테이블_목록);

        // when
        final List<OrderTableResponse> createdOrderTables = 주문_테이블_목록을_조회한다();

        // then
        assertAll(
                () -> assertThat(createdOrderTables.size()).isPositive()
        );
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmptyTableTest() {
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(빈_주문_테이블));
        when(orderTableRepository.save(any())).thenReturn(빈_주문_테이블);
        // given
        final OrderTableResponse createdOrderTable = 주문_테이블_요청한다(빈_주문_테이블_요청);


        // when
        final OrderTableResponse changeEmptyTable = 주문_테이블을_비운다(createdOrderTable.getId());

        // then
        assertAll(
                () -> assertThat(changeEmptyTable.isEmpty()).isEqualTo(true)
        );
    }

    @DisplayName("주문 테이블이 반드시 존재한다.")
    @Test
    void changeEmptyTableExistTableExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            final OrderTableResponse changeEmptyTable = 주문_테이블을_비운다(null);

            // then
        }).isInstanceOf(NotFoundEntityException.class);
    }

    @DisplayName("주문 상태는 cooking이나 meal이 아니어야 한다. ")
    @Test
    void changeEmptyTableOrderStatusExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderTableRepository.save(any())).thenReturn(주문_테이블_1번);
            when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블_1번));

            // given
            final OrderTableResponse createdOrderTable = 주문_테이블_요청한다(주문_테이블_1번_요청);
            주문_상태를_변경한다(주문, OrderStatus.COOKING);

            // when
            final OrderTableResponse changeEmptyTable = 주문_테이블을_비운다(createdOrderTable.getId());

            // then
        }).isInstanceOf(OrderStatusNotProcessingException.class);
    }

    @DisplayName("테이블 게스트 숫자를 변경한다.")
    @Test
    void changeNumberOfGuestsTest() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(주문_테이블_1번));

        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(7);

        // when
        OrderTableResponse changedOrderTable = 주문_테이블_손님수를_변경한다(1L, orderTableRequest);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(7);
    }

    @DisplayName("테이블 게스트 숫자는 0 미만일 수 없다.")
    @Test
    void changeNumberOfGuestsNegativeNumberExceptionTest() {
        assertThatThrownBy(() -> {
            // given
            final OrderTableRequest orderTableRequest = new OrderTableRequest(-1);
            final OrderTableResponse createdOrderTable = 주문_테이블_요청한다(orderTableRequest);

            // when
            주문_테이블_손님수를_변경한다(createdOrderTable.getId(), orderTableRequest);

            // then
        }).isInstanceOf(NegativeNumberOfGuestsException.class);
    }

    @DisplayName("주문 테이블이 반드시 존재해야 한다.")
    @Test
    void changeNumberOfGuestsExistOrderTableExceptionTest() {
        assertThatThrownBy(() -> {
            when(orderTableRepository.save(any())).thenReturn(new OrderTable(3));

            // given
            final OrderTableRequest orderTableRequest = new OrderTableRequest(3);
            final OrderTableResponse createdOrderTable = 주문_테이블_요청한다(orderTableRequest);

            // when
            주문_테이블_손님수를_변경한다(createdOrderTable.getId(), orderTableRequest);

            // then
        }).isInstanceOf(NotFoundEntityException.class);
    }

    public static void 주문테이블에_주문_추가(OrderTable orderTable, Order order) {
        orderTable.addOrder(order);
    }

    private OrderTableResponse 주문_테이블_요청한다(OrderTableRequest orderTableRequest) {
        return orderTableService.create(orderTableRequest);
    }

    private List<OrderTableResponse> 주문_테이블_목록을_조회한다() {
        return orderTableService.list();
    }

    private OrderTableResponse 주문_테이블을_비운다(Long orderTableId) {
        return orderTableService.changeEmpty(orderTableId);
    }

    private OrderTableResponse 주문_테이블_손님수를_변경한다(Long orderTableId, OrderTableRequest orderTableRequest) {
        return orderTableService.changeNumberOfGuests(orderTableId, orderTableRequest);
    }

    private void 주문_상태를_변경한다(Order order, OrderStatus orderStatus) {
        order.changeOrderStatus(orderStatus);
    }

}
