package kitchenpos.order.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductTestFixture;
import kitchenpos.menu.domain.MenuTestFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderMenuTestFixture;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTestFixture;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableTestFixture;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    private Product 하와이안피자;
    private Product 콜라;
    private Product 피클;
    private MenuGroup 피자;
    private Menu 하와이안피자세트;
    private MenuProduct 하와이안피자상품;
    private MenuProduct 콜라상품;
    private MenuProduct 피클상품;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItemRequest 하와이안피자세트주문;
    private OrderMenu 주문메뉴;

    @BeforeEach
    void setUp() {
        하와이안피자 = ProductTestFixture.create(1L, "하와이안피자", BigDecimal.valueOf(15_000));
        콜라 = ProductTestFixture.create(2L, "콜라", BigDecimal.valueOf(2_000));
        피클 = ProductTestFixture.create(3L, "피클", BigDecimal.valueOf(1_000));

        피자 = MenuGroupTestFixture.create(1L, "피자");

        하와이안피자상품 = MenuProductTestFixture.create(1L, 하와이안피자세트, 하와이안피자, 1L);
        콜라상품 = MenuProductTestFixture.create(2L, 하와이안피자세트, 콜라, 1L);
        피클상품 = MenuProductTestFixture.create(3L, 하와이안피자세트, 피클, 1L);

        하와이안피자세트 = MenuTestFixture.create(1L, "하와이안피자세트", BigDecimal.valueOf(18_000L), 피자,
            Arrays.asList(하와이안피자상품, 콜라상품, 피클상품));
        주문메뉴 = OrderMenuTestFixture.create(하와이안피자세트);

        주문테이블 = OrderTableTestFixture.create(1L,  0, false);
        하와이안피자세트주문 = OrderLineItemRequest.from(하와이안피자세트.getId(), 1);
        주문 = OrderTestFixture.create(주문테이블, Arrays.asList(하와이안피자세트주문.toOrderLineItem(주문메뉴)));
    }

    @DisplayName("주문 테이블이 등록되지 않은 경우 에러가 발생한다.")
    @Test
    void validateOrderTableNotEmptyException() {
        OrderTable orderTable = OrderTableTestFixture.create(4L, 6, true);
        OrderRequest orderRequest = OrderRequest.of(orderTable.getId(), OrderStatus.COOKING,
            Collections.singletonList(하와이안피자세트주문));
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderValidator.validator(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않다면 에러가 발생한다.")
    @Test
    void validateOrderTableNotExistsException() {
        // given
        OrderRequest orderRequest = OrderRequest.of(10L, OrderStatus.COOKING, Collections.singletonList(하와이안피자세트주문));
        when(orderTableRepository.findById(10L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderValidator.validator(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }
}