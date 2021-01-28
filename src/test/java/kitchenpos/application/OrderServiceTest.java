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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@DisplayName("애플리케이션 테스트 보호 - 주문 서비스")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuGroup 치킨세트;
    private Menu 후라이드한마리양념치킨한마리;
    private MenuProduct 후라이드치킨한마리;
    private MenuProduct 양념치킨한마리;

    private Order 주문;
    private OrderLineItem 주문_항목;
    private OrderTable 주문테이블;
    private List<OrderLineItem> orderLineItems;
    private List<Long> menuIds;

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

    @BeforeEach
    public void setup() {
        LocalDateTime now = LocalDateTime.now();

        후라이드치킨 = new Product();
        후라이드치킨.setId(1L);
        후라이드치킨.setName("후라이드");
        후라이드치킨.setPrice(BigDecimal.valueOf(10000));

        양념치킨 = new Product();
        양념치킨.setId(2L);
        양념치킨.setName("양념");
        양념치킨.setPrice(BigDecimal.valueOf(11000));

        치킨세트 = new MenuGroup();
        치킨세트.setId(1L);
        치킨세트.setName("후라이드앙념치킨");

        후라이드한마리양념치킨한마리 = new Menu();
        후라이드한마리양념치킨한마리.setId(1L);
        후라이드한마리양념치킨한마리.setName("후라이드+양념");
        후라이드한마리양념치킨한마리.setMenuGroupId(치킨세트.getId());
        후라이드한마리양념치킨한마리.setPrice(BigDecimal.valueOf(21000));

        후라이드치킨한마리 = new MenuProduct();
        후라이드치킨한마리.setProductId(후라이드치킨.getId());
        후라이드치킨한마리.setQuantity(1);

        양념치킨한마리 = new MenuProduct();
        양념치킨한마리.setProductId(양념치킨.getId());
        양념치킨한마리.setQuantity(1);

        후라이드한마리양념치킨한마리.setMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));

        주문테이블 = new OrderTable();
        주문테이블.setId(1L);
        주문테이블.setNumberOfGuests(0);
        주문테이블.setEmpty(false);

        주문_항목 = new OrderLineItem();
        주문_항목.setSeq(1L);
        주문_항목.setMenuId(후라이드한마리양념치킨한마리.getId());
        주문_항목.setQuantity(1);

        주문 = new Order();
        주문.setId(1L);
        주문.setOrderTableId(주문테이블.getId());
        주문.setOrderedTime(now);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(주문_항목);
        주문.setOrderLineItems(orderLineItems);

        menuIds = 주문.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        given(menuDao.countByIdIn(menuIds)).willReturn(Long.valueOf(주문.getOrderLineItems().size()));
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(orderDao.save(주문)).willReturn(주문);
        given(orderLineItemDao.save(주문_항목)).willReturn(주문_항목);

        Order savedOrder = orderService.create(주문);

        assertThat(savedOrder).isEqualTo(주문);

    }

    @DisplayName("주문 생성 예외: 주문 항목 목록이 비어있음")
    @Test
    void createThrowExceptionWhenOrderLineItemsEmpty() {
        주문.setOrderLineItems(null);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 생성 예외: 주문 항목의 메뉴 갯수와 저장된 메뉴 갯수가 다름")
    @Test
    void createThrowExceptionWhenMenuCountDifferenceWithSavedMenuCount() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(2L);
        orderLineItem.setMenuId(2L);
        주문.getOrderLineItems().add(orderLineItem);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 생성 예외: 주문테이블이 빈 테이블임")
    @Test
    void createThrowExceptionWhenOrderTableIsEmpty() {
        주문테이블.setEmpty(true);
        given(menuDao.countByIdIn(menuIds)).willReturn(Long.valueOf(주문.getOrderLineItems().size()));
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

}
