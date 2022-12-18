package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.testfixture.OrderTableTestFixture;
import kitchenpos.ordertable.validator.OrderTableValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderTableValidator orderTableValidator;

    @InjectMocks
    private TableService tableService;

    private Product 하와이안피자;
    private MenuProduct 하와이안피자상품;
    private MenuGroup 피자;
    private Menu 하와이안피자세트;
    private OrderLineItemRequest 하와이안피자세트주문요청;
    private Order 주문;
    private OrderTable 주문테이블;
    private OrderMenu 주문메뉴;

    @BeforeEach
    void setUp() {
        하와이안피자 = new Product(1L, "하와이안피자", BigDecimal.valueOf(15_000));
        피자 = new MenuGroup(1L, "피자");
        하와이안피자상품 = new MenuProduct(1L, 하와이안피자세트, 하와이안피자, 1L);
        하와이안피자세트 = Menu.of(1L, "하와이안피자세트", BigDecimal.valueOf(15_000L), 피자.getId(),
            Arrays.asList(하와이안피자상품));
        주문메뉴 = OrderMenu.from(하와이안피자세트);
        하와이안피자세트주문요청 = OrderLineItemRequest.from(하와이안피자세트.getId(), 1);
        주문테이블 = OrderTable.of(1L, 0, false);
        주문 = Order.of(주문테이블.getId(), OrderLineItems.from(Collections.singletonList(하와이안피자세트주문요청.toOrderLineItem(주문메뉴))));
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.save(orderTableRequest.toOrderTable())).thenReturn(orderTableRequest.toOrderTable());

        // when
        OrderTableResponse result = tableService.create(orderTableRequest);

        // then
        assertAll(
            () -> assertThat(result.getTableGroupId()).isNull(),
            () -> assertThat(result.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAllTables() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTableRequest.toOrderTable()));

        // when
        List<OrderTableResponse> result = tableService.list();

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.stream().map(OrderTableResponse::getId)).containsExactly(orderTableRequest.toOrderTable().getId())
        );
    }

    @DisplayName("주문 테이블의 비어있는지 상태를 변경한다.")
    @Test
    void updateOrderTableEmpty() {
        OrderTable orderTable = OrderTableTestFixture.create(1L, 4, false);
        OrderTableRequest request = OrderTableRequest.of(4, true);

        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        OrderTableResponse result = tableService.changeEmpty(orderTable.getId(), request);

        assertThat(result.isEmpty()).isEqualTo(true);
    }

    @DisplayName("등록되지 않은 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableNotExistException() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableRequest.toOrderTable().getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블의 비어있는 상태를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableAssignGroupException() {
        // given
        OrderTable orderTable1 = OrderTable.of(4, true);
        OrderTable orderTable2 = OrderTable.of(4, true);

        TableGroup tableGroup = TableGroup.from(1L);

        OrderTable orderTable = OrderTable.of(4, false);
        orderTable.registerTableGroup(tableGroup.getId());

        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

//    @DisplayName("주문 테이블의 상태가 조리또는 식사 중이면 비어있는 상태 변경시 예외가 발생한다.")
//    @Test
//    void updateOrderTableStatusException() {
//        // given
//        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
//        Order order = 주문.changeOrderStatus(OrderStatus.COOKING);
//        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));
//        when(orderTableRepository.save(orderTableRequest.toOrderTable())).thenReturn(orderTableRequest.toOrderTable());
//
//        // when & then
//        assertThatThrownBy(() -> tableService.changeEmpty(orderTableRequest.toOrderTable().getId(), orderTableRequest))
//            .isInstanceOf(IllegalArgumentException.class);
//    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경한다.")
    @ParameterizedTest
    @ValueSource(ints = { 6, 8, 12 })
    void updateOrderTableNumberOfGuest(int numberOfGuest) {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, false);
        OrderTableRequest updateOrderTableRequest = OrderTableRequest.of(numberOfGuest, false);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));

        // when
        OrderTableResponse result = tableService.changeNumberOfGuests(orderTableRequest.toOrderTable().getId(), updateOrderTableRequest);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(numberOfGuest);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경 시 0명보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -4, -6 })
    void updateOrderTableUnderZeroNumberOfGuestException(int numberOfGuest) {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, false);
        OrderTableRequest updateOrderTableRequest = OrderTableRequest.of(numberOfGuest, false);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.toOrderTable().getId(), updateOrderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableNotExistNumberOfGuestException() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, false);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.toOrderTable().getId(), orderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블의 손님 수를 변경하면 예외가 발생한다.")
    @Test
    void updateOrderTableEmptyNumberOfGuestException() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);
        OrderTableRequest updateOrderTableRequest = OrderTableRequest.of(6, true);
        when(orderTableRepository.findById(orderTableRequest.toOrderTable().getId())).thenReturn(Optional.of(orderTableRequest.toOrderTable()));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableRequest.toOrderTable().getId(), updateOrderTableRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
