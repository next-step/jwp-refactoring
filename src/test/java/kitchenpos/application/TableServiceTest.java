package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @DisplayName("개별 주문 테이블을 생성한다")
    @Test
    void create() {
        // given
        given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable.Builder().id(1L).build());

        // when
        OrderTable created = tableService.create(new OrderTable.Builder(0, true).build());

        // then
        assertThat(created.getId()).isNotNull();

        // verify
        then(orderTableDao).should(times(1)).save(any(OrderTable.class));
    }

    @DisplayName("테이블의 주문 등록 상태를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable.Builder().empty(false).build()));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses)).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable.Builder().empty(true).build());

        // when
        OrderTable changed = tableService.changeEmpty(1L, new OrderTable.Builder().empty(true).build());

        // then
        assertThat(changed.isEmpty()).isTrue();

        // verify
        then(orderTableDao).should(times(1)).findById(anyLong());
        then(orderDao).should(times(1)).existsByOrderTableIdAndOrderStatusIn(1L, orderStatuses);
        then(orderTableDao).should(times(1)).save(any(OrderTable.class));
    }

    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable.Builder().numberOfGuests(4).empty(false).build()));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable.Builder().numberOfGuests(8).build());

        // when
        OrderTable changed = tableService.changeNumberOfGuests(1L, new OrderTable.Builder().numberOfGuests(8).build());

        // then
        assertThat(changed.getNumberOfGuests()).isEqualTo(8);

        // verify
        then(orderTableDao).should(times(1)).findById(anyLong());
        then(orderTableDao).should(times(1)).save(any(OrderTable.class));
    }
}
