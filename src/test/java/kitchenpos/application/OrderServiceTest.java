package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
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

    private Product 후라이드;
    private MenuProduct 후라이드두마리구성;
    private MenuGroup 치킨류;
    private Menu 후라이드두마리세트;
    private OrderTable 테이블1번;
    private OrderLineItem 후라이드두마리세트_2개_주문함;
    private Order 총주문;

    @BeforeEach
    void setUp() {
        후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setPrice(new BigDecimal("5000"));

        후라이드두마리구성 = new MenuProduct();
        후라이드두마리구성.setSeq(1L);
        후라이드두마리구성.setProductId(1L);
        후라이드두마리구성.setQuantity(2L);

        치킨류 = new MenuGroup();
        치킨류.setId(1L);
        치킨류.setName("치킨");

        후라이드두마리세트 = new Menu();
        후라이드두마리세트.setId(1L);
        후라이드두마리세트.setMenuGroupId(1L);
        후라이드두마리세트.setPrice(new BigDecimal("10000"));
        후라이드두마리세트.setMenuProducts(Arrays.asList(후라이드두마리구성));

        테이블1번 = new OrderTable();
        테이블1번.setId(1L);

        후라이드두마리세트_2개_주문함 = new OrderLineItem();
        후라이드두마리세트_2개_주문함.setSeq(1L);
        후라이드두마리세트_2개_주문함.setMenuId(1L);
        후라이드두마리세트_2개_주문함.setQuantity(2L);

        총주문 = new Order();
        총주문.setId(1L);
        총주문.setOrderTableId(테이블1번.getId());
        총주문.setOrderLineItems(Arrays.asList(후라이드두마리세트_2개_주문함));
    }

    @DisplayName("주문 생성")
    @Test
    void create() {
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(테이블1번));
        given(orderDao.save(총주문)).willReturn(총주문);
        given(orderLineItemDao.save(후라이드두마리세트_2개_주문함)).willReturn(후라이드두마리세트_2개_주문함);

        Order createOrder = orderService.create(총주문);

        assertAll(
                () -> assertThat(createOrder).isNotNull(),
                () -> assertThat(createOrder. getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(createOrder. getOrderLineItems().contains(후라이드두마리세트_2개_주문함)).isTrue()
        );

    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        given(orderDao.findAll()).willReturn(Arrays.asList(총주문));

        List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders.size()).isEqualTo(1),
                () -> assertThat(orders.contains(총주문)).isTrue()
        );
    }

    @DisplayName("주문 상태를 식사로 변경 할 수 있다.")
    @Test
    void changeMealStatus() {
        Order 주문 = new Order();
        주문.setId(2L);
        주문.setOrderTableId(테이블1번.getId());
        주문.setOrderLineItems(Arrays.asList(후라이드두마리세트_2개_주문함));
        Order 식사_상태_주문 = new Order();
        식사_상태_주문.setOrderStatus("MEAL");
        given(orderDao.findById(2L)).willReturn(java.util.Optional.ofNullable(주문));
        given(orderLineItemDao.findAllByOrderId(2L)).willReturn(Arrays.asList(후라이드두마리세트_2개_주문함));

        orderService.changeOrderStatus(2L, 식사_상태_주문);

        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태를 계산 완료로 변경 할 수 있다.")
    @Test
    void changeCompletionStatus() {
        Order 주문 = new Order();
        주문.setId(2L);
        주문.setOrderTableId(테이블1번.getId());
        주문.setOrderLineItems(Arrays.asList(후라이드두마리세트_2개_주문함));
        Order 계산_완료_주문 = new Order();
        계산_완료_주문.setOrderStatus("COMPLETION");
        given(orderDao.findById(2L)).willReturn(java.util.Optional.ofNullable(주문));
        given(orderLineItemDao.findAllByOrderId(2L)).willReturn(Arrays.asList(후라이드두마리세트_2개_주문함));

        orderService.changeOrderStatus(2L, 계산_완료_주문);

        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문 완료 상태가 아닌 주문만 변경 가능하다.")
    @Test
    void changeStatusError() {
        Order 주문 = new Order();
        주문.setId(2L);
        주문.setOrderTableId(테이블1번.getId());
        주문.setOrderLineItems(Arrays.asList(후라이드두마리세트_2개_주문함));
        주문.setOrderStatus("COMPLETION");
        Order 계산_완료_주문 = new Order();
        계산_완료_주문.setOrderStatus("COMPLETION");
        given(orderDao.findById(2L)).willReturn(java.util.Optional.ofNullable(주문));

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(2L, 계산_완료_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
