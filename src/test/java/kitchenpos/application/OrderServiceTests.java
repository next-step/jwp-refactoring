package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@DisplayName("주문 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private Menu 코스A;
    private OrderTable 주문테이블;
    private Order 새주문;

    @BeforeEach
    public void setUp() {
        MenuGroup 코스메뉴그룹 = new MenuGroup(1L, "코스메뉴그룹");
        Product 시저_샐러드 = new Product.builder()
                .id(1L)
                .name("시저 샐러드")
                .price(new BigDecimal(10_000))
                .build();
        MenuProduct 시저_샐러드_메뉴 = new MenuProduct.builder()
                .seq(1L)
                .productId(시저_샐러드.getId())
                .quantity(5L)
                .build();
        List<MenuProduct> 메뉴상품목록 = Arrays.asList(시저_샐러드_메뉴);
        코스A = new Menu.builder()
                .id(1L)
                .name("코스A")
                .price(new BigDecimal(56_000))
                .menuGroupId(코스메뉴그룹.getId())
                .menuProducts(메뉴상품목록)
                .build();

        주문테이블 = new OrderTable.builder()
                .id(1L)
                .tableGroupId(1L)
                .numberOfGuests(3)
                .empty(false)
                .build();
        OrderLineItem orderLineItem = new OrderLineItem.builder()
                .seq(1L)
                .orderId(1L)
                .menuId(코스A.getId())
                .quantity(2)
                .build();
        새주문 = new Order.builder()
                .id(1L)
                .orderTableId(주문테이블.getId())
                .orderLineItems(Arrays.asList(orderLineItem))
                .build();
    }

    @Test
    public void 주문_생성() {
        lenient().when(menuDao.countByIdIn(Arrays.asList(코스A.getId())))
                .thenReturn(1L);
        lenient().when(orderTableDao.findById(새주문.getOrderTableId()))
                .thenReturn(Optional.of(주문테이블));
        lenient().when(orderDao.save(새주문))
                .thenReturn(새주문);

        assertThat(orderService.create(새주문))
                .isNotNull()
                .isInstanceOf(Order.class)
                .isEqualTo(새주문);
    }

    @Test
    public void 빈OrderLineItem로_생성실패() {
        Order 새주문 = new Order.builder()
                .id(1L)
                .orderTableId(1L)
                .build();

        assertThatThrownBy(() -> orderService.create(새주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 orderLineItems를 입력할 수 없습니다.");
    }

    @Test
    public void 없는_메뉴로_주문생성할_경우_생성실패() {
        OrderLineItem 주문아이템 = new OrderLineItem.builder()
                .seq(1L)
                .menuId(1L)
                .quantity(2)
                .build();
        Order 새주문 = new Order.builder()
                .id(1L)
                .orderTableId(1L)
                .orderLineItems(Arrays.asList(주문아이템))
                .build();

        assertThatThrownBy(() -> orderService.create(새주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴가 있습니다.");
    }

    @Test
    public void 없는_주문테이블로_주문생성할_경우_생성실패() {
        OrderLineItem orderLineItem = new OrderLineItem.builder()
                .seq(1L)
                .orderId(1L)
                .menuId(1L)
                .quantity(2)
                .build();
        Order 새주문 = new Order.builder()
                .id(1L)
                .orderTableId(-1L)
                .orderLineItems(Arrays.asList(orderLineItem))
                .build();

        lenient().when(menuDao.countByIdIn(Arrays.asList(코스A.getId())))
                .thenReturn(1L);

        assertThatThrownBy(() -> orderService.create(새주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("찾을 수 없는 주문테이블입니다.");
    }

    @Test
    public void 주문목록_조회() {
        lenient().when(orderDao.findAll())
                .thenReturn(Arrays.asList(새주문));

        assertThat(orderService.list())
                .isNotNull()
                .isInstanceOf(List.class)
                .hasSize(1)
                .isEqualTo(Arrays.asList(새주문));
    }

    @Test
    public void 주문_상태값_변경() {
        lenient().when(orderDao.findById(새주문.getId()))
                .thenReturn(Optional.of(새주문));
        새주문.setOrderStatus(OrderStatus.MEAL.name());
        lenient().when(orderDao.save(새주문))
                .thenReturn(새주문);

        assertThat(orderService.changeOrderStatus(새주문.getId(), 새주문))
                .isNotNull()
                .isInstanceOf(Order.class)
                .isEqualTo(새주문);
    }

    @Test
    public void 존재하지_않는_주문의_상태값_변경시_오류발생() {
        lenient().when(orderDao.findById(0L))
                .thenThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, 새주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문입니다.");
    }

    @Test
    public void 이미완료된_주문의_상태값_변경시_오류발생() {
        새주문.setOrderStatus(OrderStatus.COMPLETION.name());
        lenient().when(orderDao.findById(새주문.getId()))
                        .thenReturn(Optional.of(새주문));
        assertThatThrownBy(() -> orderService.changeOrderStatus(새주문.getId(), 새주문))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }

}
