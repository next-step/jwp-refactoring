package kitchenpos.table.application;

import static kitchenpos.table.OrderTableTestFixture.주문_테이블_생성;
import static kitchenpos.table.OrderTableTestFixture.테이블_그룹_있는_주문_테이블_생성;
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
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableGuestRequest;
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
public class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문_테이블;
    private OrderTable 비어있는_않은_주문_테이블;

    @BeforeEach
    void init() {
        주문_테이블 = 주문_테이블_생성(1L, 4, true);
        비어있는_않은_주문_테이블 = 주문_테이블_생성(2L, 7, false);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void createOrder() {
        // given
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문_테이블);

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
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(주문_테이블));

        // when
        List<OrderTableResponse> orderTables = tableService.list();

        // then
        assertThat(orderTables.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 테이블의 빈 테이블 상태를 변경한다.")
    void changeEmpty() {
        // given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);

        // when
        OrderTableResponse changedOrderTable = tableService.changeEmpty(주문_테이블.getId(), new OrderTableStatusRequest(비어있는_않은_주문_테이블.isEmpty()));

        // then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("유효하지 않은 주문 테이블의 빈 테이블 상태를 변경할 경우 - 오류")
    void changeEmptyByNonExistentOrderTable() {
        // given
        OrderTable 테이블_그룹_있는_주문_테이블 = 테이블_그룹_있는_주문_테이블_생성(1L, 비어있는_않은_주문_테이블.getTableGroup(), 0, false);

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(테이블_그룹_있는_주문_테이블.getId(), new OrderTableStatusRequest(테이블_그룹_있는_주문_테이블.isEmpty())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 정보가 없는 주문 테이블의 빈 테이블 상태를 변경할 경우 - 오류")
    void changeEmptyByNonExistentTableGroup() {
        // given
        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), new OrderTableStatusRequest(비어있는_않은_주문_테이블.isEmpty())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태가 `요리중`이거나 `식사`일 경우 - 오류")
    void changeEmptyByInvalidOrderStatus() {
        // given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        // when then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), new OrderTableStatusRequest(비어있는_않은_주문_테이블.isEmpty())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 방문 손님 수를 변경한다.")
    void changeGuest() {
        // given
        given(orderTableRepository.findById(비어있는_않은_주문_테이블.getId())).willReturn(Optional.of(비어있는_않은_주문_테이블));

        // when
        OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(비어있는_않은_주문_테이블.getId(), new OrderTableGuestRequest(5));

        // that
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(비어있는_않은_주문_테이블.getNumberOfGuests());
    }

    @Test
    @DisplayName("유효하지 않은 주문 테이블의 손님 수를 변경할 경우 - 오류")
    void changeGuestByNonExistentOrderTable() {
        // given
        given(orderTableRepository.findById(비어있는_않은_주문_테이블.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(비어있는_않은_주문_테이블.getId(), new OrderTableGuestRequest(10)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
