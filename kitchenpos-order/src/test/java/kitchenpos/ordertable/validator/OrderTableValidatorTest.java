package kitchenpos.ordertable.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.testfixture.OrderTableTestFixture;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableValidator orderTableValidator;

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
        주문테이블 = OrderTable.of(1L, 4, false);
        주문 = Order.of(주문테이블.getId(), OrderLineItems.from(Collections.singletonList(하와이안피자세트주문요청.toOrderLineItem(주문메뉴))));
    }

    @DisplayName("주문 테이블의 상태가 조리또는 식사 중이면 비어있는 상태 변경시 예외가 발생한다.")
    @Test
    void validateNotCompleteOrdersException() {
        when(orderRepository.findAllByOrderTableId(주문테이블.getId())).thenReturn(Arrays.asList(주문));

        assertThatThrownBy(() -> orderTableValidator.validatorTable(주문테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되지 않은 경우 에러가 발생한다.")
    @Test
    void validateOrderTableNotEmptyException() {
        OrderTable orderTable = OrderTableTestFixture.create(4L, 6, true);
        OrderRequest orderRequest = OrderRequest.of(orderTable.getId(), OrderStatus.COOKING,
            Collections.singletonList(하와이안피자세트주문요청));
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderTableValidator.validatorOrder(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않다면 에러가 발생한다.")
    @Test
    void validateOrderTableNotExistsException() {
        OrderRequest orderRequest = OrderRequest.of(10L, OrderStatus.COOKING, Collections.singletonList(하와이안피자세트주문요청));
        when(orderTableRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderTableValidator.validatorOrder(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}