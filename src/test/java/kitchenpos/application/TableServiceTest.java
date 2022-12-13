package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    TableService tableService;

    private OrderTable 주문테이블_Empty;
    private OrderTable 주문테이블_NotEmpty;
    private OrderTable 주문테이블_1명_Group;
    private OrderTable 주문테이블_Empty_Group;

    @BeforeEach
    void setUp() {
        주문테이블_Empty = new OrderTable(1L, null, 0, true);
        주문테이블_NotEmpty = new OrderTable(2L, null, 2, false);
        주문테이블_1명_Group = new OrderTable(3L, 1L, 1, false);
        주문테이블_Empty_Group = new OrderTable(4L, 1L, 2, true);
    }

    @Test
    @DisplayName("주문 테이블을 등록한다.")
    void 주문_테이블_등록() {
        // given
        given(orderTableDao.save(주문테이블_Empty)).willReturn(주문테이블_Empty);

        // when
        OrderTable saveOrderTable = tableService.create(주문테이블_Empty);

        // then
        assertThat(saveOrderTable.getId()).isEqualTo(주문테이블_Empty.getId());
        assertThat(saveOrderTable.getTableGroupId()).isEqualTo(주문테이블_Empty.getTableGroupId());
        assertThat(saveOrderTable.getNumberOfGuests()).isEqualTo(주문테이블_Empty.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void 주문_테이블_목록_조회() {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문테이블_Empty));

        // when
        List<OrderTable> orderTableList = tableService.list();

        // then
        assertThat(orderTableList).containsExactly(주문테이블_Empty);
    }

    @Test
    @DisplayName("주문 테이블의 비어있음을 수정한다.")
    void 주문_테이블_EMPTY_UPDATE() {
        // given
        OrderTable orderTable = new OrderTable(주문테이블_Empty.getId(), 주문테이블_Empty.getTableGroupId(), 주문테이블_Empty.getNumberOfGuests(), false);
        given(orderTableDao.findById(주문테이블_Empty.getId())).willReturn(Optional.of(주문테이블_Empty));
        given(tableService.changeEmpty(주문테이블_Empty.getId(), orderTable)).willReturn(orderTable);

        // when
        OrderTable updateOrderTable = tableService.changeEmpty(주문테이블_Empty.getId(), orderTable);

        // then
        assertThat(updateOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    @DisplayName("단체 지정에 등록된 주문 테이블의 비어있음을 수정하면 오류가 발생한다.")
    void error_주문_테이블_EMPTY_UPDATE_단체_지정_등록() {
        // given
        OrderTable orderTable = new OrderTable(주문테이블_Empty_Group.getId(), 주문테이블_Empty_Group.getTableGroupId(), 주문테이블_Empty_Group.getNumberOfGuests(), false);
        given(orderTableDao.findById(주문테이블_Empty_Group.getId())).willReturn(Optional.of(주문테이블_Empty_Group));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(주문테이블_Empty_Group.getId(), orderTable));
    }

    @Test
    @DisplayName("주문 상태가 조리 중/식사 중인 주문 테이블의 비어있음을 수정하면 오류가 발생한다.")
    void error_주문_테이블_EMPTY_UPDATE_주문_상태() {
        // given
        given(orderTableDao.findById(주문테이블_Empty.getId())).willReturn(Optional.of(주문테이블_Empty));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블_Empty.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(주문테이블_Empty.getId(), 주문테이블_NotEmpty));
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 수정한다.")
    void 주문_테이블_GUEST_NUM_UPDATE() {
        // given
        주문테이블_1명_Group = new OrderTable(주문테이블_NotEmpty.getId(), 1L, 주문테이블_NotEmpty.getNumberOfGuests(), false);
        given(orderTableDao.findById(주문테이블_NotEmpty.getId())).willReturn(Optional.of(주문테이블_NotEmpty));
        given(tableService.changeNumberOfGuests(주문테이블_NotEmpty.getId(), 주문테이블_1명_Group)).willReturn(주문테이블_1명_Group);

        // when
        OrderTable updateOrderTable = tableService.changeNumberOfGuests(주문테이블_NotEmpty.getId(), 주문테이블_1명_Group);

        // then
        assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(주문테이블_1명_Group.getNumberOfGuests());
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 0 미만으로 수정하면 오류가 발생한다.")
    void error_주문_테이블_GUEST_NUM_UPDATE_zero_미만() {
        // given
        OrderTable 인원_Zero_미만 = new OrderTable(주문테이블_1명_Group.getId(), 주문테이블_1명_Group.getTableGroupId(), -1, 주문테이블_1명_Group.isEmpty());

        // then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(주문테이블_1명_Group.getId(), 인원_Zero_미만));
    }

    @Test
    @DisplayName("등록되지 않은 주문 테이블의 손님 수를 수정하면 오류가 발생한다.")
    void error_NOT_REGISTER_주문_테이블_GUEST_NUM_UPDATE() {
        // given
        OrderTable 등록_안된_주문_테이블 = new OrderTable(주문테이블_1명_Group.getId(), 주문테이블_1명_Group.getTableGroupId(), 7, 주문테이블_1명_Group.isEmpty());

        // then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(주문테이블_1명_Group.getId(), 등록_안된_주문_테이블));
    }

    @Test
    @DisplayName("비어있는 주문 테이블의 손님 수를 수정하면 오류가 발생한다.")
    void error_EMPTY_주문_테이블_GUEST_NUM_UPDATE() {

        // then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(주문테이블_Empty_Group.getId(), 주문테이블_1명_Group));
    }
}
