package kitchenpos.order.application;


import static kitchenpos.fixture.MenuFactory.createMenu;
import static kitchenpos.fixture.MenuProductFactory.createMenuProduct;
import static kitchenpos.fixture.OrderFactory.createOrder;
import static kitchenpos.fixture.OrderFactory.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static kitchenpos.fixture.ProductFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableAlreadyEmptyException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderValidator orderValidator;

    private OrderTable 주문테이블;
    private Menu 빅맥버거;

    @BeforeEach
    void setUp() {
        Product 토마토 = createProduct(2L, "토마토", 1000);
        Product 양상추 = createProduct(3L, "양상추", 500);

        주문테이블 = createOrderTable(1L, null, 5, false);
        빅맥버거 = createMenu(1L, "빅맥버거", 3000, 1L,
                Arrays.asList(createMenuProduct(1L, null, 토마토, 1), createMenuProduct(2L, null, 양상추, 4)));


    }

    @Test
    void 없는_메뉴_예외() {
        // given
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(menuRepository.countByIdIn(Arrays.asList(100L))).willReturn(0);

        Order 없는메뉴주문 = createOrder(1L, 주문테이블, OrderStatus.COOKING.name(), null,
                Arrays.asList(createOrderLineItem(1L, null, 100L, 1)));

        // when, then
        assertThatThrownBy(
                () -> orderValidator.validate(없는메뉴주문)
        ).isInstanceOf(NotFoundMenuException.class);
    }

    @Test
    void 존재하지_않는_주문_테이블_예외() {
        // given
        given(orderTableRepository.findById(주문테이블.getId())).willThrow(NotFoundOrderTableException.class);

        // when, then
        Order 없는메뉴주문 = createOrder(1L, 주문테이블, OrderStatus.COOKING.name(), null,
                Arrays.asList(createOrderLineItem(1L, null, 빅맥버거.getId(), 1)));
        assertThatThrownBy(
                () -> orderValidator.validate(없는메뉴주문)
        ).isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 주문_생성_빈_테이블_예외() {
        // given
        주문테이블.changeEmpty(true);
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));

        // when, then
        Order 주문 = createOrder(1L, 주문테이블, OrderStatus.COOKING.name(), null,
                Arrays.asList(createOrderLineItem(1L, null, 빅맥버거.getId(), 1)));
        assertThatThrownBy(
                () -> orderValidator.validate(주문)
        ).isInstanceOf(OrderTableAlreadyEmptyException.class);
    }
}
