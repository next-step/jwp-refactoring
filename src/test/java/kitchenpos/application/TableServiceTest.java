package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.*;
import static kitchenpos.application.fixture.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        OrderTableFixture.init();

        when(orderTableDao.findById(주문_개인테이블.getId())).thenReturn(Optional.ofNullable(주문_개인테이블));
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(주문_개인테이블);
    }

    @DisplayName("OrderTable 을 등록한다.")
    @Test
    void create1() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable).isEqualTo(주문_개인테이블);
    }

    @DisplayName("OrderTable 목록을 조회한다.")
    @Test
    void findList() {
        // given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(주문_개인테이블));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).containsExactly(주문_개인테이블);
    }

    @DisplayName("OrderTable 을 빈 테이블 상태로 변경한다.")
    @Test
    void changeEmpty1() {
        // given
        주문_개인테이블.setEmpty(false);
        when(orderTableDao.findById(주문_개인테이블.getId())).thenReturn(Optional.ofNullable(주문_개인테이블));

        // when
        OrderTable changedEmptyTable = tableService.changeEmpty(주문_개인테이블.getId(), 빈_개인테이블);

        // then
        assertThat(changedEmptyTable).isEqualTo(주문_개인테이블);
        assertThat(changedEmptyTable.isEmpty()).isTrue();
    }

    @DisplayName("OrderTable 을 빈 테이블 상태로 변경 시, 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeEmpty2() {
        // given
        when(orderTableDao.findById(주문_개인테이블.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_개인테이블.getId(), 빈_개인테이블));

    }

    @DisplayName("OrderTable 을 빈 테이블 상태로 변경 시, 테이블 그룹이 존재하면 예외가 발생한다.")
    @Test
    void changeEmpty3() {
        // given
        주문1_단체테이블.setTableGroupId(단체_테이블그룹.getId());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문1_단체테이블.getId(), 빈_개인테이블));
    }

    @DisplayName("OrderTable 을 빈 테이블 상태로 변경 시, 주문상태가 요리중(COOKING)이거나 식사중(MEAL) 이면 예외가 발생한다.")
    @Test
    void changeEmpty4() {
        // given
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_개인테이블.getId(), 빈_개인테이블));
    }

    @DisplayName("OrderTable 의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests1() {
        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(주문_개인테이블.getId(), 손님_10명_개인테이블);

        // then
        assertThat(changedOrderTable).isEqualTo(주문_개인테이블);
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(손님_10명_개인테이블.getNumberOfGuests());
    }

    @DisplayName("OrderTable 의 손님 수를 변경 시, 손님의 수가 음수이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10})
    void changeNumberOfGuests2(int wrongNumberOfGuests) {
        // given
        OrderTable 손님_음수_개인테이블 = new OrderTable();
        손님_음수_개인테이블.setNumberOfGuests(wrongNumberOfGuests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_개인테이블.getId(),
                                                                                                손님_음수_개인테이블));
    }

    @DisplayName("OrderTable 의 손님 수를 변경 시, 테이블이 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests3() {
        // given
        when(orderTableDao.findById(주문_개인테이블.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_개인테이블.getId(),
                                                                                                손님_10명_개인테이블));
    }

    @DisplayName("OrderTable 의 손님 수를 변경 시, 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests4() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(빈_개인테이블.getId(),
                                                                                                손님_10명_개인테이블));
    }
}