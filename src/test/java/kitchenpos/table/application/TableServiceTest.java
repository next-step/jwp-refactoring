package kitchenpos.table.application;

import static kitchenpos.util.TestDataSet.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.product.constant.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문_테이블의 손님 수 정상 업데이트 케이스")
    void changeNumberOfGuests() {
        //given
        OrderTableRequest chagneRequest = new OrderTableRequest(10, true);
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(테이블_1번));
        given(orderTableDao.save(any())).willReturn(change);

        //when
        OrderTable result = tableService.changeNumberOfGuests(테이블_1번.getId(), change);

        // then
        assertThat(result.getId()).isEqualTo(change.getId());
        assertThat(result.getNumberOfGuests()).isEqualTo(change.getNumberOfGuests());

        verify(orderTableDao, times(1)).findById(any());
        verify(orderTableDao, times(1)).save(any());

    }

    @Test
    @DisplayName("손님은 0명 미만일 경우 실패한다.")
    void underZero() {
        //given
        OrderTable change = new OrderTable(테이블_1번.getId(), -1, true);

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeNumberOfGuests(테이블_1번.getId(), change);
        });
    }

    @Test
    @DisplayName("주문_테이블의 빈 여부 업데이트 수정 성공 케이스")
    void changeEmpty() {
        //given
        OrderTableRequest chagneRequest = new OrderTableRequest(10, false);
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(테이블_1번));

        //when
        OrderTableResponse result = tableService.changeEmpty(테이블_1번.getId(), chagneRequest);

        // then
        assertThat(result.getId()).isEqualTo(change.getId());
        assertThat(result.isEmpty()).isEqualTo(false);

        verify(orderTableRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("존재 하지 않는 테이블을 업데이트 할 수 없다.")
    void noTable() {
        //given
        OrderTableRequest chagneRequest = new OrderTableRequest(10, false);
        OrderTable change = new OrderTable(테이블_1번.getId(), 10, false);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeEmpty(테이블_1번.getId(), chagneRequest);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeNumberOfGuests(테이블_1번.getId(), change);
        });

        verify(orderTableDao, times(1)).findById(any());
        verify(orderTableRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("주문_테이블에 속한 주문들이 현재 조리중이거나, 식사 중일 시 테이블을 업데이트 할 수 없다.")
    void isEating() {
        //given
        Order 쿠킹_주문 = new Order(1L, OrderStatus.COOKING.name(), 1L, null);
        Order 먹는상태_주문 = new Order(1L, OrderStatus.MEAL.name(), 1L, null);
        OrderTable 쿠킹_테이블 = new OrderTable(1L, null, 10, false, Arrays.asList(쿠킹_주문));
        OrderTable 먹는상태_테이블 = new OrderTable(1L, null, 10, false, Arrays.asList(먹는상태_주문));
        OrderTableRequest chagneRequest = new OrderTableRequest(10, true);
        given(orderTableRepository.findById(테이블_1번.getId())).willReturn(Optional.of(쿠킹_테이블));
        given(orderTableRepository.findById(테이블_2번.getId())).willReturn(Optional.of(먹는상태_테이블));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeEmpty(테이블_1번.getId(), chagneRequest);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            tableService.changeEmpty(테이블_2번.getId(), chagneRequest);
        });

        verify(orderTableRepository, times(1)).findById(테이블_1번.getId());
        verify(orderTableRepository, times(1)).findById(테이블_2번.getId());
    }

}
