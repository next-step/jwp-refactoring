package kitchenpos.ordertable.validator;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.order.domain.OrderLineItemTestFixture.generateOrderLineItemRequest;
import static kitchenpos.order.domain.OrderMenuTestFixture.generateOrderMenu;
import static kitchenpos.order.domain.OrderTestFixture.generateOrder;
import static kitchenpos.order.domain.OrderTestFixture.generateOrderRequest;
import static kitchenpos.ordertable.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductTestFixture;
import kitchenpos.menu.domain.MenuTestFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 validator 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderValidatorImplTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidatorImpl orderValidator;

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 치킨버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 치킨버거상품;
    private MenuProduct 콜라상품;
    private Menu 불고기버거세트;
    private Menu 치킨버거세트;
    private OrderMenu 불고기버거세트주문메뉴;
    private OrderMenu 치킨버거세트주문메뉴;
    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private OrderLineItemRequest 불고기버거세트주문요청;
    private OrderLineItemRequest 치킨버거세트주문요청;
    private Order 주문A;
    private Order 주문B;

    @BeforeEach
    void setUp() {
        감자튀김 = ProductTestFixture.generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = ProductTestFixture.generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = ProductTestFixture.generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        치킨버거 = ProductTestFixture.generateProduct(4L, "치킨버거", BigDecimal.valueOf(4500L));
        햄버거세트 = MenuGroupTestFixture.generateMenuGroup(1L, "햄버거세트");
        감자튀김상품 = MenuProductTestFixture.generateMenuProduct(1L, null, 감자튀김, 1L);
        콜라상품 = MenuProductTestFixture.generateMenuProduct(2L, null, 콜라, 1L);
        불고기버거상품 = MenuProductTestFixture.generateMenuProduct(3L, null, 불고기버거, 1L);
        치킨버거상품 = MenuProductTestFixture.generateMenuProduct(3L, null, 치킨버거, 1L);
        불고기버거세트 = MenuTestFixture.generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(8500L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 불고기버거상품));
        치킨버거세트 = MenuTestFixture.generateMenu(2L, "치킨버거세트", BigDecimal.valueOf(9000L), 햄버거세트,
                Arrays.asList(감자튀김상품, 콜라상품, 치킨버거상품));
        불고기버거세트주문메뉴 = generateOrderMenu(불고기버거세트);
        치킨버거세트주문메뉴 = generateOrderMenu(치킨버거세트);
        주문테이블A = generateOrderTable(1L, 5, false);
        주문테이블B = generateOrderTable(2L, 7, false);
        불고기버거세트주문요청 = generateOrderLineItemRequest(불고기버거세트.getId(), 2);
        치킨버거세트주문요청 = generateOrderLineItemRequest(치킨버거세트.getId(), 1);
        주문A = generateOrder(주문테이블A, Arrays.asList(불고기버거세트주문요청.toOrderLineItem(불고기버거세트주문메뉴), 치킨버거세트주문요청.toOrderLineItem(치킨버거세트주문메뉴)));
        주문B = generateOrder(주문테이블B, singletonList(불고기버거세트주문요청.toOrderLineItem(불고기버거세트주문메뉴)));
    }

    @DisplayName("주문 테이블이 비어있을 경우 주문은 생성될 수 없다.")
    @Test
    void validateOrderRequestThrowErrorWhenOrderTableIsEmpty() {
        // given
        OrderTable orderTable = generateOrderTable(4L, 6, true);
        OrderRequest orderRequest = generateOrderRequest(orderTable.getId(), OrderStatus.COOKING, singletonList(불고기버거세트주문요청));
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.validator(orderRequest))
                .withMessage(ErrorCode.주문_테이블은_비어있으면_안됨.getErrorMessage());
    }

    @DisplayName("주문 테이블이 존재하지 않다면 주문은 생성될 수 없다.")
    @Test
    void validateOrderRequestThrowErrorWhenOrderTableIsNotExists() {
        // given
        OrderRequest orderRequest = generateOrderRequest(10L, OrderStatus.COOKING, singletonList(불고기버거세트주문요청));
        given(orderTableRepository.findById(10L)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.validator(orderRequest))
                .withMessage(ErrorCode.존재하지_않는_주문_테이블.getErrorMessage());
    }

    @DisplayName("주문들 중 완료되지 않은 주문이 있으면 검증은 실패한다.")
    @Test
    void validateIfNotCompletionOrdersThrowErrorWhenOrderIsNotComplete() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderValidator.validateIfNotCompletionOrders(Arrays.asList(주문A, 주문B)))
                .withMessage(ErrorCode.완료되지_않은_주문.getErrorMessage());
    }
}
