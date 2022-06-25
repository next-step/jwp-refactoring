package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private Order order;
    private MenuProduct chicken_menuProduct;
    private MenuProduct ham_menuProduct;
    private OrderLineItem chickenOrder;
    private OrderLineItem hamOrder;
    private OrderTable orderTable;

    @BeforeEach
    public void init() {
        setMenu();
        setOrderLineItem();
        setOrderTable();
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        order = new Order();

    }

    private void setOrderTable() {
        orderTable = new OrderTable();
        orderTable.setEmpty(true);
    }

    private void setMenu() {
        Product chicken = new Product();
        chicken.setPrice(BigDecimal.valueOf(5000));
        chicken_menuProduct = new MenuProduct();
        chicken_menuProduct.setProductId(1L);
        chicken_menuProduct.setQuantity(1);
        chicken_menuProduct.setMenuId(1L);

        Product ham = new Product();
        ham.setPrice(BigDecimal.valueOf(4000));
        ham_menuProduct = new MenuProduct();
        ham_menuProduct.setProductId(2L);
        ham_menuProduct.setQuantity(1);
        ham_menuProduct.setMenuId(1L);
    }

    private void setOrderLineItem() {
        chickenOrder = new OrderLineItem();
        chickenOrder.setMenuId(chicken_menuProduct.getMenuId());
        chickenOrder.setQuantity(1);

        hamOrder = new OrderLineItem();
        hamOrder.setMenuId(ham_menuProduct.getMenuId());
        hamOrder.setQuantity(2);
    }

    @Test
    @DisplayName("주문 상품 없이 주문을 하면 에러 발생")
    void createWithoutItemsThrowError() {
        //when && then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상품이 존재하지 않는것이 있다면 에러 발생")
    void createWithoutOrderThrowError() {
        //given
        order.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));
        when(menuDao.countByIdIn(
            Arrays.asList(chickenOrder.getMenuId(), hamOrder.getMenuId())))
            .thenReturn(1L);

        //when && then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문할 테이블이 존재하지 않다면 에러 발생")
    void createWithNotSavedTableThrowError() {
        //given
        order.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));
        when(menuDao.countByIdIn(
            Arrays.asList(chickenOrder.getMenuId(), hamOrder.getMenuId())))
            .thenReturn(2L);

        //when && then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문할 테이블이 비어있다면 에러 발생")
    void createWithEmptyTableThrowError() {
        //given
        order.setOrderLineItems(Arrays.asList(chickenOrder, hamOrder));
        when(menuDao.countByIdIn(
            Arrays.asList(chickenOrder.getMenuId(), hamOrder.getMenuId()))).thenReturn(2L);
        order.setOrderTableId(1L);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));

        //when && then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 완료된 오더 수정시 에러 발생")
    void changeOrderStatusAlreadyCompleteThrowError() {
        //given
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));
        order.setOrderStatus("COMPLETION");

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }
}