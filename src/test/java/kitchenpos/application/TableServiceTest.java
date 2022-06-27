package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.TestOrderTableFactory;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;

    @BeforeEach
    void setUp() {
        주문_테이블_1 = TestOrderTableFactory.create(1L, 3);
        주문_테이블_2 = TestOrderTableFactory.create(2L, 5);
    }

    @DisplayName("주문테이블을 등록한다")
    @Test
    void create() throws Exception {
        // given
        given(orderTableDao.save(any())).willReturn(주문_테이블_1);

        // when
        OrderTable orderTable = tableService.create(주문_테이블_1);

        // then
        assertThat(orderTable).isEqualTo(주문_테이블_1);
    }

    @DisplayName("전체 테이블을 조회한다")
    @Test
    void list() throws Exception {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when
        List<OrderTable> orderTable = tableService.list();

        // then
        assertThat(orderTable).containsExactly(주문_테이블_1, 주문_테이블_2);
    }

    @DisplayName("테이블을 비운다")
    @Test
    void changeEmpty() throws Exception {
        // given
        주문_테이블_2.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블_1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(주문_테이블_1);

        // when
        OrderTable orderTable = tableService.changeEmpty(주문_테이블_1.getId(), 주문_테이블_2);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(true);
    }

    @DisplayName("주문 테이블이 없으면 비울 수 없다")
    @Test
    void changeEmptyException1() throws Exception {
        // given
        주문_테이블_2.setEmpty(true);
        주문_테이블_1.setTableGroupId(2L);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블_1.getId(), 주문_테이블_2))
                .isInstanceOf(IllegalArgumentException.class);

        // then
    }

    @DisplayName("단체 테이블이면 비울 수 없다")
    @Test
    void changeEmptyException2() throws Exception {
        // given
        주문_테이블_2.setEmpty(true);
        주문_테이블_1.setTableGroupId(2L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블_1));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블_1.getId(), 주문_테이블_2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 주문상태가 COOKING, MEAL 이면 초기화할 수 없다")
    @Test
    void changeEmptyException3() throws Exception {
        // given
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블_1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블_1.getId(), 주문_테이블_2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 5명에서 10명으로 변경한다")
    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        주문_테이블_1.setNumberOfGuests(5);
        주문_테이블_2.setNumberOfGuests(10);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블_1));
        given(orderTableDao.save(any())).willReturn(주문_테이블_1);

        // when
        OrderTable orderTable = tableService.changeNumberOfGuests(주문_테이블_1.getId(), 주문_테이블_2);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("변경할 손님 수가 0명 미만이면 변경할 수 없다")
    @Test
    void changeNumberOfGuestsException1() throws Exception {
        // given
        주문_테이블_1.setNumberOfGuests(5);
        주문_테이블_2.setNumberOfGuests(-1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_1.getId(), 주문_테이블_2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("변경할 손님 수가 0명 미만이면 변경할 수 없다")
    @Test
    void changeNumberOfGuestsException2() throws Exception {
        // given
        주문_테이블_1.setNumberOfGuests(5);
        주문_테이블_2.setNumberOfGuests(10);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_1.getId(), 주문_테이블_2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님수 변경은 주문 테이블이 비어 있으면 수정할 수 없다")
    @Test
    void changeNumberOfGuestsException3() throws Exception {
        // given
        주문_테이블_1.setEmpty(true);
        주문_테이블_2.setNumberOfGuests(10);

        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블_1));
        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_1.getId(), 주문_테이블_2))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
