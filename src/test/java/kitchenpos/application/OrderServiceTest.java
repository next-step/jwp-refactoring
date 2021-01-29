package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

@DisplayName("애플리케이션 테스트 보호 - 주문 서비스")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private Product 후라이드치킨;
    private Product 양념치킨;
    private MenuGroup 치킨세트;
    private Menu 후라이드한마리_양념치킨한마리;
    private MenuProduct 후라이드치킨한마리;
    private MenuProduct 양념치킨한마리;

    private Order 주문;
    private OrderLineItem 주문_항목;
    private OrderTable 주문테이블;
    private List<OrderLineItem> orderLineItems;
    private List<Long> menuIds;

    @Mock
    private MenuRepository menuRepository;

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

        후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000));
        양념치킨 = new Product(2L, "양념치킨", new BigDecimal(16000));

        치킨세트 = new MenuGroup("후라이드앙념치킨");

        후라이드한마리_양념치킨한마리 = new Menu("후라이드+양념", BigDecimal.valueOf(32000), 치킨세트);

        후라이드치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 후라이드치킨, 1L);
        양념치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 양념치킨, 1L);

        후라이드한마리_양념치킨한마리.addAllMenuProduct(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));

        주문테이블 = new OrderTable();
        주문테이블.setId(1L);
        주문테이블.setNumberOfGuests(0);
        주문테이블.setEmpty(false);

        주문_항목 = new OrderLineItem();
        주문_항목.setSeq(1L);
        주문_항목.setMenuId(후라이드한마리_양념치킨한마리.getId());
        주문_항목.setQuantity(1);

        주문 = new Order();
        주문.setId(1L);
        주문.setOrderTableId(주문테이블.getId());
        주문.setOrderedTime(now);

        orderLineItems = new ArrayList<>();
        orderLineItems.add(주문_항목);
        주문.setOrderLineItems(orderLineItems);

        menuIds = 주문.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        given(menuRepository.countByIdIn(menuIds)).willReturn(Long.valueOf(주문.getOrderLineItems().size()));
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
        given(menuRepository.countByIdIn(menuIds)).willReturn(Long.valueOf(주문.getOrderLineItems().size()));
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        given(orderDao.findAll()).willReturn(Collections.singletonList(주문));
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(orderLineItems);

        List<Order> orders = orderService.list();

        assertThat(orders).containsExactly(주문);
        assertThat(orders.get(0).getOrderLineItems()).containsAll(orderLineItems);
    }

}
