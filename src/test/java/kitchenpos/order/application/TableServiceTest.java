package kitchenpos.order.application;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItemRequest;
import static kitchenpos.order.domain.OrderMenuTestFixture.generateOrderMenu;
import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTableRequest;
import static kitchenpos.order.domain.OrderTestFixture.generateOrder;
import static kitchenpos.order.domain.TableGroupTestFixture.generateTableGroup;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.domain.Product;
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
public class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private Product 불고기버거;
    private MenuProduct 불고기버거상품;
    private MenuGroup 햄버거단품;
    private Menu 불고기버거단품;
    private OrderMenu 불고기버거단품주문상품;
    private OrderLineItemRequest 불고기버거세트주문요청;
    private Order 주문;
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;

    @BeforeEach
    void setUp() {
        불고기버거 = generateProduct("불고기버거", BigDecimal.valueOf(4000L));
        불고기버거상품 = generateMenuProduct(불고기버거, 1L);
        햄버거단품 = generateMenuGroup("햄버거단품");
        불고기버거단품 = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(4000L), 햄버거단품, singletonList(불고기버거상품));
        불고기버거단품주문상품 = generateOrderMenu(불고기버거단품);
        불고기버거세트주문요청 = generateOrderLineItemRequest(불고기버거단품.getId(), 2);
        주문테이블A = generateOrderTable(1L, 5, false);
        주문테이블B = generateOrderTable(2L, 4, false);
        주문 = generateOrder(주문테이블B, singletonList(불고기버거세트주문요청.toOrderLineItem(불고기버거단품주문상품)));
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        int numberOfGuests = 5;
        boolean empty = false;
        OrderTableRequest orderTableRequest = generateOrderTableRequest(numberOfGuests, empty);
        given(orderTableRepository.save(orderTableRequest.toOrderTable())).willReturn(주문테이블A);

        // when
        OrderTableResponse saveOrderTableResponse = tableService.create(orderTableRequest);

        // then
        assertAll(
                () -> assertThat(saveOrderTableResponse.getTableGroupId()).isNull(),
                () -> assertThat(saveOrderTableResponse.getNumberOfGuests()).isEqualTo(주문테이블A.getNumberOfGuests().value())
        );
    }

    @DisplayName("주문 테이블 전체 목록을 조회한다.")
    @Test
    void findAllTables() {
        // given
        List<OrderTable> orderTables = Arrays.asList(주문테이블A, 주문테이블B);
        given(orderTableRepository.findAll()).willReturn(orderTables);

        // when
        List<OrderTableResponse> findOrderTables = tableService.list();

        // then
        assertAll(
                () -> assertThat(findOrderTables).hasSize(orderTables.size()),
                () -> assertThat(findOrderTables.stream().map(OrderTableResponse::getId)).containsExactly(주문테이블A.getId(), 주문테이블B.getId())
        );
    }

    @DisplayName("주문 테이블의 비어있는지 여부를 변경한다.")
    @Test
    void changeTableEmpty() {
        // given
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        boolean isEmpty = 주문테이블A.isEmpty();
        int numberOfGuests = 주문테이블A.getNumberOfGuests().value();
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(numberOfGuests, !isEmpty);
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderRepository.findAllByOrderTableId(주문테이블A.getId())).willReturn(singletonList(주문));

        // when
        OrderTableResponse resultOrderTable = tableService.changeEmpty(주문테이블A.getId(), changeOrderTableRequest);

        // then
        assertThat(resultOrderTable.isEmpty()).isEqualTo(!isEmpty);
    }

    @DisplayName("존재하지 않는 주문 테이블의 비어있는지 여부는 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenOrderTableIsNotExists() {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(4, false);
        given(orderTableRepository.findById(10L)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(10L, changeOrderTableRequest))
                .withMessage(ErrorCode.존재하지_않는_주문_테이블.getErrorMessage());
    }

    @DisplayName("단체 지정이 되어 있는 테이블은 비어있는지 여부를 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenTableGroupIdIsNotNull() {
        // given
        TableGroup tableGroup = generateTableGroup(1L, Arrays.asList(주문테이블A, 주문테이블B));
        OrderTable orderTable = generateOrderTable(5L, 4, false);
        orderTable.registerTableGroup(tableGroup.getId());
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(4, true);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), changeOrderTableRequest))
                .withMessage(ErrorCode.단체_그룹_지정되어_있음.getErrorMessage());
    }

    @DisplayName("주문 테이블의 주문 상태가 조리중이거나 식사중이면 비어있는지 여부를 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenOrderStatusIsCooking() {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(3, true);
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderRepository.findAllByOrderTableId(주문테이블A.getId())).willReturn(singletonList(주문));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(주문테이블A.getId(), changeOrderTableRequest))
                .withMessage(ErrorCode.완료되지_않은_주문.getErrorMessage());
    }

    @ParameterizedTest(name = "주문 테이블에 방문한 손님 수를 변경한다. (변경된 손님 수: {0})")
    @ValueSource(ints = {6, 10, 13})
    void changeNumberOfGuestsInTable(int expectNumberOfGuests) {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(expectNumberOfGuests, false);
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));

        // when
        OrderTableResponse resultOrderTable = tableService.changeNumberOfGuests(주문테이블A.getId(), changeOrderTableRequest);

        // then
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(expectNumberOfGuests);
    }

    @ParameterizedTest(name = "주문 테이블에 방문한 손님 수를 변경하고자 할 때, 변경하고자 하는 손님 수가 0보다 작으면 예외가 발생한다. (변경하고자 하는 손님 수: {0})")
    @ValueSource(ints = {-1, -3, -5})
    void changeNumberOfGuestsInTableThrowErrorWhenNumberOfGuestIsSmallerThanZero(int expectNumberOfGuests) {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(expectNumberOfGuests, false);
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(주문테이블A.getId(), changeOrderTableRequest))
                .withMessage(ErrorCode.방문한_손님_수는_0보다_작을_수_없음.getErrorMessage());
    }

    @DisplayName("존재하지 않는 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestInTableThrowErrorWhenTableIsNotExists() {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(1, false);
        OrderTable orderTable = generateOrderTable(5L, 4, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTableRequest))
                .withMessage(ErrorCode.존재하지_않는_주문_테이블.getErrorMessage());
    }

    @DisplayName("주문 테이블이 비어있는 경우, 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestInTableThrowErrorWhenTableIsEmpty() {
        // given
        OrderTable orderTable = generateOrderTable(5L, 4, true);
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(1, true);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTableRequest))
                .withMessage(ErrorCode.비어있는_주문_테이블.getErrorMessage());
    }
}
