package kitchenpos.application;

import static kitchenpos.application.TableServiceTest.두명;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    public static final boolean 비어있지않음 = false;

    @Mock
    private OrderDao orderDao;
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    private Menu 치즈버거세트;
    private Menu 새우버거세트;
    private OrderTable 주문테이블;
    private OrderLineItem 첫번째_주문항목;
    private OrderLineItem 두번째_주문항목;
    private List<OrderLineItem> 주문_항목_목록;
    private Order 주문;
    private List<Order> 주문_목록;

    @BeforeEach
    void setup() {
        치즈버거세트 = new Menu();
        치즈버거세트.setId(1L);
        새우버거세트 = new Menu();
        새우버거세트.setId(2L);
        주문테이블 = new OrderTable(1L, 두명, 비어있지않음);
        첫번째_주문항목 = new OrderLineItem(1L, 1L, 1L, 1);
        두번째_주문항목 = new OrderLineItem(2L, 1L, 2L, 1);
        주문_항목_목록 = new ArrayList<>(Arrays.asList(첫번째_주문항목, 두번째_주문항목));
        주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), 주문_항목_목록);
        주문_목록 = new ArrayList<>(Arrays.asList(주문));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        // Given
        given(menuDao.countByIdIn(any())).willReturn((long) 주문_항목_목록.size());
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(orderDao.save(주문)).willReturn(주문);
        given(orderLineItemDao.save(첫번째_주문항목)).willReturn(첫번째_주문항목);
        given(orderLineItemDao.save(두번째_주문항목)).willReturn(두번째_주문항목);

        // When
        orderService.create(주문);

        // Then
        verify(menuDao, times(1)).countByIdIn(any());
        verify(orderTableDao, times(1)).findById(any());
        verify(orderDao, times(1)).save(any());
        verify(orderLineItemDao, times(2)).save(any());
    }

    @DisplayName("주문 항목은 1개 이상 이어야한다.")
    @Test
    void create_Fail_01() {
        // Given
        주문.setOrderLineItems(null);

        // When & Then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴는 모두 존재해야 한다.")
    @Test
    void create_Fail_02() {
        // Given
        Long 주문항목의_메뉴가_존재하지_않음 = 0L;
        given(menuDao.countByIdIn(any())).willReturn(주문항목의_메뉴가_존재하지_않음);

        // When & Then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블은 빈 테이블일 수 없다.")
    @Test
    void create_Fail_03() {
        // Given
        주문테이블.setEmpty(true);
        given(menuDao.countByIdIn(any())).willReturn((long) 주문_항목_목록.size());
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        // When & Then
        assertThatThrownBy(() -> orderService.create(주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        // Given
        given(orderDao.findAll()).willReturn(주문_목록);
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(주문_항목_목록);

        // When & Then
        assertThat(orderService.list()).hasSize(1);
        verify(orderDao, times(1)).findAll();
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // Given
        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderDao.save(주문)).willReturn(주문);
        given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(주문_항목_목록);

        // When
        orderService.changeOrderStatus(주문.getId(), 주문);

        // Then
        verify(orderDao, times(1)).findById(any());
        verify(orderDao, times(1)).save(any());
        verify(orderLineItemDao, times(1)).findAllByOrderId(any());
    }

    @DisplayName("주문상태가 계산완료인 주문은 변경할 수 없다.")
    @Test
    void changeOrderStatus_Fail() {
        // Given
        주문.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));

        // When & Then
        Long 변경할_주문_ID = 주문.getId();
        assertThatThrownBy(() -> orderService.changeOrderStatus(변경할_주문_ID, 주문))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
