package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableEmptyRequest;
import kitchenpos.dto.OrderTableNumOfGuestRequest;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.factory.OrderTableFixtureFactory;
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

import static kitchenpos.factory.OrderTableFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable 테이블_EMPTY;
    private OrderTable 테이블_NOT_EMPTY;
    private OrderTable 테이블_GROUPED;

    @BeforeEach
    void setUp() {
        테이블_EMPTY = createOrderTable(1L, null, 0, true);
        테이블_NOT_EMPTY = createOrderTable(2L, null, 4, false);
        테이블_GROUPED = createOrderTable(3L, 1L, 0, true);
    }

    @DisplayName("주문테이블을 등록할 수 있다")
    @Test
    void 주문테이블_등록(){
        //given
        given(orderTableDao.save(any(OrderTable.class))).willReturn(테이블_EMPTY);

        //when
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_EMPTY.getNumberOfGuests(), 테이블_EMPTY.isEmpty())
        );

        //then
        assertThat(savedTable).isEqualTo(OrderTableResponse.from(테이블_EMPTY));
    }

    @DisplayName("주문테이블의 목록을 조회할 수 있다")
    @Test
    void 주문테이블_목록_조회(){
        //given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(테이블_EMPTY));

        //when
        List<OrderTableResponse> list = tableService.list();

        //then
        assertThat(list).containsExactly(OrderTableResponse.from(테이블_EMPTY));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트할 수 있다")
    @Test
    void 주문테이블_Empty_업데이트(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_EMPTY));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(createOrderTable(테이블_EMPTY.getId(),
                테이블_EMPTY.getTableGroupId(), 테이블_EMPTY.getNumberOfGuests(), true));

        //when
        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(true);
        tableService.changeEmpty(테이블_EMPTY.getId(), emptyRequest);

        //then
        assertThat(테이블_EMPTY.isEmpty()).isTrue();
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_검증(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when
        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(테이블_EMPTY.getId(), emptyRequest));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 테이블그룹에 등록되어있으면 안된다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_테이블그룹_등록_검증(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_GROUPED));
        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(테이블_GROUPED.getId(), emptyRequest));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 안된다")
    @Test
    void 주문테이블_Empty_업데이트_주문_상태_검증(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_EMPTY));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        //when
        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(테이블_EMPTY.getId(), emptyRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트할 수 있다")
    @Test
    void 주문테이블_손님수_업데이트(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_NOT_EMPTY));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(createOrderTable(테이블_NOT_EMPTY.getId(),
                테이블_NOT_EMPTY.getTableGroupId(), 1, false));

        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);
        tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), oneGuestRequest);

        //then
        assertThat(테이블_NOT_EMPTY.getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 손님 수는 0 이상이다")
    @Test
    void 주문테이블_손님수_업데이트_손님수_검증(){
        //when
        OrderTableNumOfGuestRequest invalidRequest = OrderTableNumOfGuestRequest.from(-1);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), invalidRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 업데이트할 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_검증(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), oneGuestRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 주문테이블이 비어있으면 안된다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_Empty_검증(){
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(테이블_EMPTY));

        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_EMPTY.getId(), oneGuestRequest));
    }
}
