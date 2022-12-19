package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 validation service 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    Long 주문_id;
    OrderTable 주문_테이블;
    OrderLineItem 주문_아이템_1;
    OrderLineItem 주문_아이템_2;
    List<OrderLineItem> 주문_아이템_목록;
    Order 주문;

    @BeforeEach
    void setUp() {

        Long 떡튀순_id = 1L;
        Long 떡튀순_곱배기_id = 2L;
        Product 떡볶이 = new Product(1L, "떡볶이", new ProductPrice(4500));
        Product 튀김 = new Product(2L, "튀김", new ProductPrice(2500));
        Product 순대 = new Product(3L, "순대", new ProductPrice(4000));

        MenuProduct 떡튀순_상품_떡볶이 = new MenuProduct(1L, null, 떡볶이.getId(), 1);
        MenuProduct 떡튀순_상품_튀김 = new MenuProduct(2L, null, 튀김.getId(), 1);
        MenuProduct 떡튀순_상품_순대 = new MenuProduct(3L, null, 순대.getId(), 1);
        MenuProduct 떡튀순_곱배기_상품_떡볶이 = new MenuProduct(4L, null, 떡볶이.getId(), 2);

        List<MenuProduct> 떡튀순_상품_목록 = Arrays.asList(떡튀순_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        List<MenuProduct> 떡튀순_곱배기_상품_목록 = Arrays.asList(떡튀순_곱배기_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);

        MenuGroup 세트 = new MenuGroup(1L, "세트");
        Menu 떡튀순 = new Menu(떡튀순_id, "떡튀순", new MenuPrice(10000), 세트.getId(), new MenuProducts(떡튀순_상품_목록));
        Menu 떡튀순_곱배기 = new Menu(떡튀순_곱배기_id, "떡튀순_곱배기", new MenuPrice(10000), 세트.getId(),
                new MenuProducts(떡튀순_곱배기_상품_목록));

        주문_id = 1L;
        주문_테이블 = new OrderTable(1L, null, new NumberOfGuests(0), false);
        주문_아이템_1 = new OrderLineItem(떡튀순.getId(), 2);
        주문_아이템_2 = new OrderLineItem(떡튀순_곱배기.getId(), 1);
        주문_아이템_목록 = Arrays.asList(주문_아이템_1, 주문_아이템_2);
        주문 = new Order(주문_테이블.getId(), OrderStatus.COOKING);
        주문_아이템_목록.forEach(orderLineItem -> 주문.addOrderLineItem(orderLineItem));
    }

    @DisplayName("빈 테이블에 대한 주문 등록 요청 시 예외처리")
    @Test
    void 빈테이블_주문_등록_예외처리() {
        OrderTable 빈_주문_테이블 = new OrderTable(1L, null, new NumberOfGuests(0), true);
        Order 빈_테이블_주문 = new Order(빈_주문_테이블.getId(), OrderStatus.COOKING);
        주문_아이템_목록.stream().forEach(빈_테이블_주문::addOrderLineItem);
        when(menuRepository.countByIdIn(메뉴_Id_목록(주문_아이템_목록))).thenReturn(주문_아이템_목록.size());
        when(orderTableRepository.findById(빈_주문_테이블.getId())).thenReturn(Optional.of(빈_주문_테이블));

        assertThatThrownBy(() -> orderValidator.validateBeforeCreateOrder(빈_테이블_주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("생성되지 않은 주문 테이블에 대한 주문 등록 요청 시 예외처리")
    @Test
    void 생성안된_주문_테이블_주문_등록_예외처리() {
        when(menuRepository.countByIdIn(메뉴_Id_목록(주문_아이템_목록))).thenReturn(주문_아이템_목록.size());
        when(orderTableRepository.findById(주문_테이블.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderValidator.validateBeforeCreateOrder(주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("메뉴와 수량 정보 없이 주문 등록 요청 시 예외처리")
    @Test
    void 메뉴와_수량_누락_주문_등록_예외처리() {
        List<OrderLineItem> 빈_아이템_목록 = new ArrayList<>();
        Order 주문_아이템_누락된_주문 = new Order(주문_id, 주문_테이블.getId(), OrderStatus.COOKING, null, 빈_아이템_목록);

        assertThatThrownBy(() -> orderValidator.validateBeforeCreateOrder(주문_아이템_누락된_주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("계산 완료 상태인 주문 상태변경 시 예외처리")
    @Test
    void 계산_완료_주문_상태변경_예외처리() {
        Order 계산_완료된_주문 = new Order(2L, 주문_테이블.getId(), OrderStatus.COMPLETION, null, 주문_아이템_목록);

        assertThatThrownBy(
                () -> orderValidator.checkOrderStatusChangeAble(계산_완료된_주문)).isInstanceOf(
                IllegalArgumentException.class);
    }

    private List<Long> 메뉴_Id_목록(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }
}
