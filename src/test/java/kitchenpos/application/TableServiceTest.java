package kitchenpos.application;

import static common.OrderTableFixture.*;
import static common.OrderTableFixture.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
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

    @Test
    void 주문가능_테이블생성() {
        // given
        OrderTable 첫번째_테이블 = 첫번째_주문테이블();

        // mocking
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(첫번째_테이블);
        
        // when
        OrderTable orderTable = tableService.create(첫번째_테이블);
        
        // then 
        assertThat(orderTable).isEqualTo(첫번째_테이블);
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
        OrderTable orderTable = new OrderTable(첫번째_주문테이블.getId(),
            첫번째_주문테이블.getTableGroupId(),
            첫번째_주문테이블.getNumberOfGuests(), true);

        OrderTable 저장된_주문_테이블 = tableService.changeEmpty(첫번째_주문테이블.getId(), orderTable);

        Assertions.assertThat(저장된_주문_테이블).isEqualTo(첫번째_주문테이블);

    }

    @Test
    void 요리중이거나_식사중이면_빈테이블로_변경시_예외() {
        // given
        OrderTable 주문_첫번째_1번_테이블 = 첫번째_주문테이블();

        // mocking
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(주문_첫번째_1번_테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        // when
        OrderTable orderTable = new OrderTable(주문_첫번째_1번_테이블.getId(),
            주문_첫번째_1번_테이블.getTableGroupId(),
            주문_첫번째_1번_테이블.getNumberOfGuests(), true);

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문_첫번째_1번_테이블.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 해당_테이블에_방문한_손님수를_등록() {
        // given
        OrderTable 첫번째_주문테이블 = 첫번째_주문테이블();
        OrderTable 방문자수_3명으로변경 = new OrderTable(3L, null, 3, false);
        // mocking
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(첫번째_주문테이블));
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(첫번째_주문테이블);

        // when
        OrderTable orderTable = tableService.changeNumberOfGuests(첫번째_주문테이블.getId(),
            방문자수_3명으로변경);

        Assertions.assertThat(orderTable).isEqualTo(첫번째_주문테이블);
    }
}
