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

    @Test
    @DisplayName("`주문 항목`은 필수 이다.")
    void create_fail1() {
        // given
        Order order = new Order();

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.create(order);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문 항목` 모두 등록되어 있어야한다.")
    void 주문항목_미등록_실패() {
        // given
        OrderLineItem 주문항목 = 주문항목_생성(1L, 1L);
        Order 주문 = 주문_생성(1L, Collections.singletonList(주문항목));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.create(주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문`이 속할 `주문 테이블`은 `빈 테이블`상태가 아니어야 한다.")
    void 주문테이블_빈테이블_상태면_실패() {
        // given
        OrderTable 주문테이블 = 주문테이블_생성(1L);
        OrderLineItem 주문항목 = 주문항목_생성(1L, 1L);
        Order 주문 = 주문_생성(1L, Collections.singletonList(주문항목));

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(menuDao.countByIdIn(any())).willReturn(1L);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.create(주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문`이 속할 `주문 테이블`이 등록되어있어야한다.")
    void 주문테이블_등록되어있지_않으면_실패() {
        // given
        OrderLineItem 주문항목 = 주문항목_생성(1L, 1L);
        Order 주문 = 주문_생성(1L, Collections.singletonList(주문항목));

        given(menuDao.countByIdIn(any())).willReturn(1L);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.create(주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("`주문` 최초 상태는 `조리`다.")
    void 주문상태_검증() {
        // given
        OrderTable 주문테이블 = 주문테이블_생성(1L);
        주문테이블.setEmpty(false);
        OrderLineItem 주문항목 = 주문항목_생성(1L, 1L);
        Order 주문 = 주문_생성(1L, Collections.singletonList(주문항목));

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderDao.save(주문)).willReturn(주문);

        // when
        Order 등록된_주문 = orderService.create(주문);

        // then
        assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("`주문`목록을 조회 할 수 있다.")
    void 주문목록_조회() {
        // given
        Order 주문 = new Order();

        given(orderDao.findAll()).willReturn(Collections.singletonList(주문));

        // when
        List<Order> 주문목록 = orderService.list();

        // then
        assertThat(주문목록).contains(주문);
    }

    @Test
    @DisplayName("`주문 상태`가 `계산 완료`이면 상태를 변경 할 수 없다.")
    void 주문상태_변경_실패() {
        // given
        Long 주문번호 = 1L;
        Order 주문 = new Order();
        주문.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(주문번호)).willReturn(Optional.of(주문));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderService.changeOrderStatus(주문번호, 주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

}
