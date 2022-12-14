package kitchenpos.application;

import static kitchenpos.exception.ErrorCode.NOT_BEEN_UNGROUP;
import static kitchenpos.exception.ErrorCode.NOT_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.NOT_EXISTS_TABLE;
import static kitchenpos.exception.ErrorCode.PEOPLE_LESS_THAN_ZERO;
import static kitchenpos.exception.ErrorCode.TABLE_IS_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private TableService tableService;
    private OrderTable 주문_좌석;
    private OrderTable 공석_주문_좌석;
    private OrderTable 공석_변경_요청;
    private OrderTable 인원_변경_요청;

    @BeforeEach
    void setUp() {
        주문_좌석 = new OrderTable(1L, null, 0, false);
        공석_주문_좌석 = new OrderTable(1L, null, 0, true);
        공석_변경_요청 = new OrderTable(1L, 1L, 0, true);
        인원_변경_요청 = new OrderTable(1L, null, 4, false);
    }

    @Test
    void 생성() {
        given(orderTableDao.save(any())).willReturn(주문_좌석);

        OrderTableResponse response = tableService.create(주문_좌석);

        assertAll(
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(response.getTableGroupId()).isNull(),
                () -> assertThat(response.isEmpty()).isFalse()
        );
    }

    @Test
    void 조회() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문_좌석));

        List<OrderTableResponse> responses = tableService.list();

        assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    void 공석으로_변경() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_좌석));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(공석_변경_요청);

        OrderTableResponse response = tableService.changeEmpty(주문_좌석.getId(), 공석_변경_요청);

        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    void 등록된_주문_좌석이_아닌_경우() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_좌석.getId(), 공석_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_EXISTS_TABLE.getDetail());
    }

    @Test
    void 좌석_그룹으로_등록된_경우() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(공석_변경_요청));

        assertThatThrownBy(
                () -> tableService.changeEmpty(공석_변경_요청.getId(), 공석_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_BEEN_UNGROUP.getDetail());
    }

    @Test
    void 좌석_상태가_준비중이거나_식사중인_경우() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_좌석));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_좌석.getId(), 공석_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_COMPLETION_STATUS.getDetail());
    }

    @Test
    void 좌석_인원_변경() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_좌석));
        given(orderTableDao.save(any())).willReturn(인원_변경_요청);

        OrderTableResponse response = tableService.changeNumberOfGuests(주문_좌석.getId(), 인원_변경_요청);

        assertThat(response.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void 음수를_인원_변경을_요청한_경우() {
        인원_변경_요청 = new OrderTable(1L, null, -4, false);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(주문_좌석.getId(), 인원_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PEOPLE_LESS_THAN_ZERO.getDetail());
    }

    @Test
    void 주문한_좌석이_아닌_경우() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(주문_좌석.getId(), 인원_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_EXISTS_TABLE.getDetail());
    }

    @Test
    void 공석인_경우() {
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(공석_주문_좌석));

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(주문_좌석.getId(), 인원_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(TABLE_IS_EMPTY.getDetail());
    }
}
