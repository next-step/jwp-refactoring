package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

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

    @Test
    void changeEmpty() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable.Builder().build()));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable.Builder().id(1L).empty(true).build());

        OrderTable created = tableService.create(new OrderTable.Builder(0, true).build());

        // when
        OrderTable changed = tableService.changeEmpty(created.getId(), new OrderTable.Builder().empty(true).build());

        // then
        assertThat(changed.isEmpty()).isTrue();

        // verify
        then(orderTableDao).should(times(1)).findById(anyLong());
        then(orderDao).should(times(1)).existsByOrderTableIdAndOrderStatusIn(
                1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
        then(orderTableDao).should(times(2)).save(any(OrderTable.class)); // create, changeEmpty
    }

    @Test
    void changeNumberOfGuests() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable.Builder(4, false).id(1L).build()));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable.Builder(8, false).id(1L).build());

        // when
        OrderTable changed = tableService.changeNumberOfGuests(1L, new OrderTable.Builder().numberOfGuests(8).build());

        // then
        assertThat(changed.getNumberOfGuests()).isEqualTo(8);

        // verify
        then(orderTableDao).should(times(1)).findById(anyLong());
        then(orderTableDao).should(times(1)).save(any(OrderTable.class));
    }
}
