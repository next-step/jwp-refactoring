package kitchenpos.application;


import static kitchenpos.application.fixture.OrderFixture.주문_생성;
import static kitchenpos.application.fixture.OrderLineItemFixture.주문항목_생성;
import static kitchenpos.application.fixture.OrderTableFixture.주문테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관리 기능")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

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

    private OrderTable 손님_있는_주문테이블;
    private OrderLineItem 주문항목;
    private Order 요청된_주문;
    private Order 계산완료_주문;


    @BeforeEach
    void setUp() {
        //given
        손님_있는_주문테이블 = 주문테이블_생성(1L, 1L, false, 1);
        주문항목 = 주문항목_생성(1L, 1L);
        요청된_주문 = 주문_생성(1L, 1L, OrderStatus.COOKING, Collections.singletonList(주문항목));
        계산완료_주문 = 주문_생성(3L, 1L, OrderStatus.COMPLETION, Collections.singletonList(주문항목));
    }

    @Test
    @DisplayName("`주문 항목`은 필수 이다.")
    void create_fail1() {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.create(요청된_주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문 항목` 모두 등록되어 있어야한다.")
    void 주문항목_미등록_실패() {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.create(요청된_주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문`이 속할 `주문 테이블`은 `빈 테이블`상태가 아니어야 한다.")
    void 주문테이블_빈테이블_상태면_실패() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(OrderTable.EMPTY_TABLE));
        given(menuDao.countByIdIn(any())).willReturn(1L);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.create(요청된_주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문`이 속할 `주문 테이블`이 등록되어있어야한다.")
    void 주문테이블_등록되어있지_않으면_실패() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(1L);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.create(요청된_주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문` 최초 상태는 `조리`다.")
    void 주문상태_검증() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(손님_있는_주문테이블));
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderDao.save(요청된_주문)).willReturn(요청된_주문);

        // when
        Order 등록된_주문 = orderService.create(요청된_주문);

        // then
        assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("`주문`목록을 조회 할 수 있다.")
    void 주문목록_조회() {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(요청된_주문));

        // when
        List<Order> 주문목록 = orderService.list();

        // then
        assertThat(주문목록).contains(요청된_주문);
    }

    @Test
    @DisplayName("`주문 상태`가 `계산 완료`이면 상태를 변경 할 수 없다.")
    void 주문상태_변경_실패() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.of(계산완료_주문));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.changeOrderStatus(anyLong(),
            계산완료_주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문`의 `주문 상태`를 변경 할 수 있다.")
    void 주문상태_변경() {
        // given
        요청된_주문.setOrderStatus(OrderStatus.MEAL.name());
        given(orderDao.findById(anyLong())).willReturn(Optional.of(요청된_주문));

        // when
        Order 변경된_주문 = orderService.changeOrderStatus(anyLong(), 요청된_주문);

        // then
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(요청된_주문.getOrderStatus());
    }
}
