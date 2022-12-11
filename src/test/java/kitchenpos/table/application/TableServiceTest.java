package kitchenpos.table.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.table.application.TableService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    public OrderTable 주문테이블_1번;
    public OrderTable 주문테이블_2번;

    @BeforeEach
    void set_up() {
        주문테이블_1번 = OrderTableFixture.create(1L, null, 0, true);
        주문테이블_2번 = OrderTableFixture.create(2L, null, 0, true);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        // given
        when(orderTableDao.save(주문테이블_1번)).thenReturn(주문테이블_1번);

        // when
        OrderTable 테이블_등록_결과 = tableService.create(주문테이블_1번);

        // then
        assertThat(테이블_등록_결과).isEqualTo(주문테이블_1번);
    }

    @DisplayName("테이블 목록을 조회 할 수 있다.")
    @Test
    void list() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(주문테이블_1번, 주문테이블_2번));

        // when
        List<OrderTable> 테이블_목록_조회 = tableService.list();

        // then
        assertAll(
                () -> assertThat(테이블_목록_조회).hasSize(2),
                () -> assertThat(테이블_목록_조회).containsExactly(주문테이블_1번, 주문테이블_2번)
        );
    }

    @DisplayName("테이블 상태를 빈 테이블로 변경할 수 있다.")
    @Test
    void update_table_empty() {
        // given
        OrderTable 주문테이블_변경 = OrderTableFixture.create(2L, null, 10, false);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(주문테이블_변경));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(주문테이블_변경);

        // when
        주문테이블_변경 = tableService.changeEmpty(주문테이블_변경.getId(), 주문테이블_1번);

        // then
        assertThat(주문테이블_변경.isEmpty()).isTrue();

    }

    @DisplayName("테이블이 저장되어 있지 않으면 테이블의 상태를 변경할 수 없다.")
    @Test
    void update_error_not_found_table() {
        // when && then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_1번.getId(), 주문테이블_1번))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 테이블 그룹이 등록되어 있다면 변경할 수 없다.")
    @Test
    void update_error_exist_table_group() {
        // given
        주문테이블_1번.setTableGroupId(1L);
        when(orderTableDao.findById(any())).thenReturn(Optional.ofNullable(주문테이블_1번));

        // when && then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_1번.getId(), 주문테이블_1번))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 주문의 상태가 조리 또는 식사일 경우 테이블의 상태를 변경할 수 없다.")
    @Test
    void update_error_table_status() {
        // given
        when(orderTableDao.findById(주문테이블_1번.getId())).thenReturn(Optional.ofNullable(주문테이블_1번));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                주문테이블_1번.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        // when && then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블_1번.getId(), 주문테이블_1번);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님의 수를 변경할 수 있다.")
    @Test
    void change_number_guest() {
        // given
        주문테이블_1번.setNumberOfGuests(20);
        주문테이블_1번.setEmpty(false);
        when(orderTableDao.findById(주문테이블_1번.getId())).thenReturn(Optional.ofNullable(주문테이블_1번));
        when(orderTableDao.save(주문테이블_1번)).thenReturn(주문테이블_1번);

        // when
        OrderTable 주문테이블_변경 = tableService.changeNumberOfGuests(주문테이블_1번.getId(), 주문테이블_1번);

        // then
        assertThat(주문테이블_변경.getNumberOfGuests()).isEqualTo(20);
    }

    @DisplayName("손님의 수가 0보다 작다면 변경할 수 없다.")
    @Test
    void error_change_number_guest_less_then_0() {
        // given
        주문테이블_1번.setNumberOfGuests(-1);

        // when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_1번.getId(), 주문테이블_1번))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요청한 테이블을 찾을 수 없으면 손님의 수를 변경할 수 없다.")
    @Test
    void error_change_not_found_table() {
        // when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_1번.getId(), 주문테이블_1번))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요청한 테이블이 존재하지 않으면 손님의 수를 변경할 수 없다.")
    @Test
    void error_change_number_table_empty() {
        // given
        when(orderTableDao.findById(주문테이블_1번.getId())).thenReturn(Optional.ofNullable(주문테이블_1번));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_1번.getId(), 주문테이블_1번))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
