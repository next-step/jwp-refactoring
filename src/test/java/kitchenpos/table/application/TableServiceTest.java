package kitchenpos.table.application;

import static common.OrderTableFixture.두번째_주문테이블;
import static common.OrderTableFixture.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.table.application.TableValidation;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.ChangeNumberOfGuestRequest;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Mock
    TableValidation tableValidator;

    @Test
    void 주문가능_테이블생성() {
        // given
        OrderTable 첫번째_테이블 = 첫번째_주문테이블();

        // mocking
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(첫번째_테이블);

        // when
        tableService.create(new OrderTableRequest(5, true));

        // then 
        verify(orderTableDao, atMostOnce()).save(any());
    }

    @Test
    void 테이블_전체조회() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();
        OrderTable 두번째_주문테이블 = 두번째_주문테이블();

        // mocking
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(첫번째_주문테이블, 두번째_주문테이블));

        // when
        List<OrderTable> list = tableService.list();

        // then 
        assertThat(list).containsExactly(첫번째_주문테이블, 두번째_주문테이블);
    }

    @Test
    void 요리중이거나_식사중이_아니면_빈테이블로_변경_가능() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();

        // mocking
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(첫번째_주문테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
        when(orderTableDao.save(첫번째_주문테이블)).thenReturn(첫번째_주문테이블);

        // when
        tableService.changeEmpty(첫번째_주문테이블.getId(), new ChangeEmptyRequest(true));

        verify(orderTableDao, atMostOnce()).save(any());
    }

    @Test
    void 요리중이거나_식사중이면_빈테이블로_변경시_예외() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();

        // mocking
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(첫번째_주문테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(첫번째_주문테이블.getId(), new ChangeEmptyRequest(true));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 해당_테이블에_방문한_손님수를_등록() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();

        // mocking
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(첫번째_주문테이블));
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(첫번째_주문테이블);

        // when
        tableService.changeNumberOfGuests(첫번째_주문테이블.getId(), new ChangeNumberOfGuestRequest(3));

        verify(orderTableDao, atMostOnce()).save(any());
    }
}
