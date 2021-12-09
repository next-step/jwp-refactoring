package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private Menu 뿌링클콤보;

    private MenuProduct 뿌링클콤보_뿌링클치킨;
    private MenuProduct 뿌링클콤보_치킨무;
    private MenuProduct 뿌링클콤보_코카콜라;

    private Product 뿌링클치킨;
    private Product 치킨무;
    private Product 코카콜라;

    private OrderTable 치킨_주문_단체테이블;
    private Order 치킨주문;
    private OrderLineItem 치킨_주문항목;

    @BeforeEach
    void setUp() {
        뿌링클치킨 = new Product();
        뿌링클치킨.setId(1L);
        뿌링클치킨.setName("뿌링클치킨");
        뿌링클치킨.setPrice(BigDecimal.valueOf(15_000));

        치킨무 = new Product();
        치킨무.setId(2L);
        치킨무.setName("치킨무");
        치킨무.setPrice(BigDecimal.valueOf(1_000));

        코카콜라 = new Product();
        코카콜라.setId(3L);
        코카콜라.setName("코카콜라");
        코카콜라.setPrice(BigDecimal.valueOf(3_000));

        뿌링클콤보 = new Menu();
        뿌링클콤보.setId(1L);
        뿌링클콤보.setName("뿌링클콤보");
        뿌링클콤보.setPrice(BigDecimal.valueOf(18_000));
   
        뿌링클콤보_뿌링클치킨 = new MenuProduct();
        뿌링클콤보_뿌링클치킨.setSeq(1L);
        뿌링클콤보_뿌링클치킨.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_뿌링클치킨.setProductId(뿌링클치킨.getId());
        뿌링클콤보_뿌링클치킨.setQuantity(1L);

        뿌링클콤보_치킨무 = new MenuProduct();
        뿌링클콤보_치킨무.setSeq(2L);
        뿌링클콤보_치킨무.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_치킨무.setProductId(치킨무.getId());
        뿌링클콤보_치킨무.setQuantity(1L);

        뿌링클콤보_코카콜라 = new MenuProduct();
        뿌링클콤보_코카콜라.setSeq(3L);
        뿌링클콤보_코카콜라.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_코카콜라.setProductId(코카콜라.getId());
        뿌링클콤보_코카콜라.setQuantity(1L);

        뿌링클콤보.setMenuProducts(List.of(뿌링클콤보_뿌링클치킨, 뿌링클콤보_치킨무, 뿌링클콤보_코카콜라));

        치킨_주문_단체테이블 = new OrderTable();
        치킨_주문_단체테이블.setId(1L);
        치킨_주문_단체테이블.setEmpty(true);

        치킨_주문항목 = new OrderLineItem();
        치킨_주문항목.setSeq(1L);

        치킨주문 = new Order();
        치킨주문.setId(1L);
        치킨주문.setOrderTableId(치킨_주문_단체테이블.getId());
        치킨주문.setOrderStatus("");
        치킨주문.setOrderedTime(LocalDateTime.now());
        치킨주문.setOrderLineItems(List.of(치킨_주문항목));
        
        치킨_주문항목.setOrderId(치킨주문.getId());
        치킨_주문항목.setMenuId(뿌링클콤보.getId());
        치킨_주문항목.setQuantity(1L);
    }

    @DisplayName("주문이 저장된다.")
    @Test
    void create_order() {
        // given
        when(menuDao.countByIdIn(List.of(this.뿌링클콤보.getId()))).thenReturn(1L);
        when(orderTableDao.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(this.치킨_주문_단체테이블));
        when(orderDao.save(any(Order.class))).thenReturn(this.치킨주문);
        when(orderLineItemDao.save(this.치킨_주문항목)).thenReturn(this.치킨_주문항목);


        this.치킨주문.setOrderLineItems(List.of(this.치킨_주문항목));

        치킨_주문_단체테이블.setEmpty(false);
        this.치킨주문.setOrderTableId(this.치킨_주문_단체테이블.getId());

        // when
        Order savedOrder = orderService.create(this.치킨주문);

        // then
        Assertions.assertThat(savedOrder).isEqualTo(this.치킨주문);
    }

    @DisplayName("주문에속하는 수량있는 메뉴가 없는 주문은 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderedMenu() {
        // given
        when(menuDao.countByIdIn(List.of(this.뿌링클콤보.getId()))).thenReturn(0L);

        this.치킨주문.setOrderLineItems(List.of(this.치킨_주문항목));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.create(this.치킨주문));
    }

    @DisplayName("미등록된 주문테이블에서 주문 시 예외가 발생된다.")
    @Test
    void exception_createOrder_notExistedOrderTable() {
        // given
        when(menuDao.countByIdIn(List.of(this.뿌링클콤보.getId()))).thenReturn(1L);
        when(orderTableDao.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.empty());

        this.치킨주문.setOrderLineItems(List.of(this.치킨_주문항목));

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.create(this.치킨주문));
    }

    @DisplayName("주문테이블이 빈테이블일 시 예외가 발생된다.")
    @Test
    void exception_createOrder_emptyOrderTable() {
        // given
        when(menuDao.countByIdIn(List.of(this.뿌링클콤보.getId()))).thenReturn(1L);
        when(orderTableDao.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(this.치킨_주문_단체테이블));

        this.치킨주문.setOrderLineItems(List.of(this.치킨_주문항목));

        치킨_주문_단체테이블.setEmpty(true);
        this.치킨주문.setOrderTableId(this.치킨_주문_단체테이블.getId());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.create(this.치킨주문));
    }

    @DisplayName("주문이 조회된다.")
    @Test
    void search_order() {
        // when
        when(orderDao.findAll()).thenReturn(List.of(this.치킨주문));
        when(orderLineItemDao.findAllByOrderId(this.치킨주문.getId())).thenReturn(List.of(this.치킨_주문항목));

        List<Order> orders = orderService.list();

        // then
        Assertions.assertThat(orders).isEqualTo(List.of(this.치킨주문));
    }

    @DisplayName("주문의 상태가 변경된다.")
    @Test
    void update_orderStatus() {
        // given
        when(orderDao.findById(this.치킨주문.getId())).thenReturn(Optional.of(this.치킨주문));

        this.치킨주문.setOrderStatus(OrderStatus.MEAL.name());

        // when
        Order chagedOrder = orderService.changeOrderStatus(this.치킨주문.getId(), this.치킨주문);

        // then
        Assertions.assertThat(chagedOrder).isEqualTo(this.치킨주문);
    }

    @DisplayName("계산이 완료된 주문의 상태를 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderStatus_afterOrderStatusCompletion() {
        // given
        when(orderDao.findById(this.치킨주문.getId())).thenReturn(Optional.of(this.치킨주문));

        this.치킨주문.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> orderService.changeOrderStatus(this.치킨주문.getId(), this.치킨주문));
    }
}
