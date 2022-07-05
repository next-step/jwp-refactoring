package kitchenpos.table.application;

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
import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public
class TableServiceTest {
    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문_테이블;
    private OrderTable 비어있는_주문_테이블;

    @BeforeEach
    void init() {
        주문_테이블 = 주문_테이블_생성(1L, 4, true);
        비어있는_주문_테이블 = 주문_테이블_생성(1L, 7, false);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void createOrder() {
        // given
        given(orderTableDao.save(any(OrderTable.class))).willReturn(주문_테이블);

        // when
        OrderTableResponse savedOrderTable = tableService.create(new OrderTableRequest(주문_테이블.getNumberOfGuests(), 주문_테이블.isEmpty()));

        // then
        assertAll(
            () -> assertThat(savedOrderTable).isNotNull(),
            () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(
                주문_테이블.getNumberOfGuests()),
            () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(주문_테이블.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void findAll() {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문_테이블));

        // when
        List<OrderTableResponse> orderTables = tableService.list();

        // then
        assertThat(orderTables.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 테이블의 빈 테이블 상태를 변경한다.")
    void changeEmpty() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));
        given(orderDao.existsByOrderTableAndOrderStatusIn(any(OrderTable.class), anyList())).willReturn(false);

        // when
        OrderTableResponse changedOrderTable = tableService.changeEmpty(주문_테이블.getId(), new OrderTableStatusRequest(비어있는_주문_테이블.isEmpty()));

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 주문 테이블의 빈 테이블 상태를 변경할 경우 - 오류")
    void changeEmptyByNonExistentOrderTable() {
        // given
        OrderTable 테이블_그룹_있는_주문_테이블 = 테이블_그룹_있는_주문_테이블_생성(1L, 비어있는_주문_테이블.getTableGroup(), 0, false);

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(테이블_그룹_있는_주문_테이블.getId(), new OrderTableStatusRequest(테이블_그룹_있는_주문_테이블.isEmpty())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 정보가 없는 주문 테이블의 빈 테이블 상태를 변경할 경우 - 오류")
    void changeEmptyByNonExistentTableGroup() {
        // given
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), new OrderTableStatusRequest(비어있는_주문_테이블.isEmpty())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태가 `요리중`이거나 `식사`일 경우 - 오류")
    void changeEmptyByInvalidOrderStatus() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));
        given(orderDao.existsByOrderTableAndOrderStatusIn(any(OrderTable.class), anyList())).willReturn(true);

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), new OrderTableStatusRequest(비어있는_주문_테이블.isEmpty())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문 손님 수를 변경한다.")
    void changeGuest() {
        // given
        OrderTable 손님_수_변경_테이블 = 주문_테이블_생성(비어있는_주문_테이블.getId(), 4, 비어있는_주문_테이블.isEmpty());
        given(orderTableDao.save(any(OrderTable.class))).willReturn(비어있는_주문_테이블);
        given(orderTableDao.findById(비어있는_주문_테이블.getId())).willReturn(Optional.of(비어있는_주문_테이블));

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(비어있는_주문_테이블.getId(), 손님_수_변경_테이블);

        // that
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(비어있는_주문_테이블.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블의 방문 손님 수를 0명 이하로 변경할 경우 - 오류")
    void changeNegativeQuantityGuest() {
        // given
        OrderTable 손님_수_변경_테이블 = 주문_테이블_생성(비어있는_주문_테이블.getId(), -4, 비어있는_주문_테이블.isEmpty());

        // when then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(비어있는_주문_테이블.getId(), 손님_수_변경_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효하지 않은 주문 테이블의 손님 수를 변경할 경우 - 오류")
    void changeGuestByNonExistentOrderTable() {
        // given
        OrderTable 손님_수_변경_테이블 = 주문_테이블_생성(비어있는_주문_테이블.getId(), 4, 비어있는_주문_테이블.isEmpty());
        given(orderTableDao.findById(비어있는_주문_테이블.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(비어있는_주문_테이블.getId(), 손님_수_변경_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어 있지 않은 주문 테이블의 손님 수를 변경할 경우 - 오류")
    void changeGuestOfOrderTableIfEmptyIsTrue() {
        // given
        OrderTable 손님_수_변경_테이블 = 주문_테이블_생성(비어있는_주문_테이블.getId(), 4, true);
        given(orderTableDao.findById(비어있는_주문_테이블.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(비어있는_주문_테이블.getId(), 손님_수_변경_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable 주문_테이블_생성(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static OrderTable 테이블_그룹_있는_주문_테이블_생성(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }
}
