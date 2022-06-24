package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable 테이블_1;

    @BeforeEach
    void setUp() {
        테이블_1 = new OrderTable(1L, null, 0, true);
    }

    @DisplayName("주문테이블을 등록할 수 있다")
    @Test
    void 주문테이블_등록(){
        //given
        given(orderTableDao.save(테이블_1)).willReturn(테이블_1);

        //when
        OrderTable savedTable = tableService.create(테이블_1);

        //then
        assertThat(savedTable).isEqualTo(테이블_1);
    }

    @DisplayName("주문테이블의 목록을 조회할 수 있다")
    @Test
    void 주문테이블_목록_조회(){
        //given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(테이블_1));

        //when
        List<OrderTable> list = tableService.list();

        //then
        assertThat(list).containsExactly(테이블_1);
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트할 수 있다")
    @Test
    void 주문테이블_Empty_업데이트(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_1));

        //when
        OrderTable orderTable_empty = new OrderTable(0, true);
        tableService.changeEmpty(테이블_1.getId(), orderTable_empty);

        //then
        assertThat(테이블_1.isEmpty()).isTrue();
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_검증(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(null));
        OrderTable orderTable_empty = new OrderTable(0, true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(테이블_1.getId(), orderTable_empty));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 테이블그룹에 등록되어있으면 안된다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_테이블그룹_등록_검증(){
        //given
        OrderTable invalidTable = new OrderTable(2L, 1L, 4, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(null));
        OrderTable orderTable_empty = new OrderTable(0, true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(invalidTable.getId(), orderTable_empty));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 안된다")
    @Test
    void 주문테이블_Empty_업데이트_주문_상태_검증(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);
        OrderTable orderTable_empty = new OrderTable(0, true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(테이블_1.getId(), orderTable_empty));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트할 수 있다")
    @Test
    void 주문테이블_손님수_업데이트(){
        //given
        OrderTable 테이블_full = new OrderTable(2L, null, 4, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_full));

        //when
        OrderTable orderTable_oneGuest = new OrderTable(1, false);
        tableService.changeNumberOfGuests(테이블_full.getId(), orderTable_oneGuest);

        //then
        assertThat(테이블_full.getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 손님 수는 0 이상이다")
    @Test
    void 주문테이블_손님수_업데이트_손님수_검증(){
        //given
        OrderTable 테이블_full = new OrderTable(2L, null, 4, false);

        //when
        OrderTable invalidOrderTable = new OrderTable(-1, false);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_full.getId(), invalidOrderTable));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 업데이트할 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_검증(){
        //given
        OrderTable 테이블_full = new OrderTable(2L, null, 4, false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when
        OrderTable orderTable_oneGuest = new OrderTable(1, false);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_full.getId(), orderTable_oneGuest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 주문테이블이 비어있으면 안된다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_Empty_검증(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_1));

        //when
        OrderTable orderTable_oneGuest = new OrderTable(1, false);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_1.getId(), orderTable_oneGuest));
    }
}
