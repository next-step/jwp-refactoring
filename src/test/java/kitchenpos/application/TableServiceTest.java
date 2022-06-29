package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
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

import static kitchenpos.application.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    private OrderTable 주문_테이블;
    private OrderTable 빈_테이블;
    private OrderTable 단체지정_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = createOrderTable(1L, null, 4, false);
        빈_테이블 = createOrderTable(2L, null, 0, true);
        단체지정_테이블 = createOrderTable(3L, 1L, 0, true);
    }

    @DisplayName("테이블을 등록한다.")
    @Test
    void create_success() {
        // given
        given(orderTableDao.save(주문_테이블)).willReturn(주문_테이블);

        // when
        OrderTable saved = tableService.create(주문_테이블);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved).isEqualTo(주문_테이블)
        );
    }

    @DisplayName("테이블의 목록을 조회한다.")
    @Test
    void list() {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문_테이블, 빈_테이블, 단체지정_테이블));

        // when
        List<OrderTable> list = tableService.list();

        // then
        assertThat(list).containsExactly(주문_테이블, 빈_테이블, 단체지정_테이블);
    }

    @DisplayName("빈 테이블로 변경한다")
    @Test
    void changeEmpty_success() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));

        // when
        tableService.changeEmpty(주문_테이블.getId(), 빈_테이블);

        // then
        assertThat(주문_테이블.isEmpty()).isTrue();
    }

    @DisplayName("빈 테이블로 변경에 실패한다. (테이블이 존재하지 않는 경우)")
    @Test
    void changeEmpty_fail_emptyTable() {
        // given
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(null));

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문_테이블.getId(), 빈_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경에 실패한다. (단체 지정된 테이블인 경우)")
    @Test
    void changeEmpty_fail_tableGroup() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(단체지정_테이블));

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(단체지정_테이블.getId(), 빈_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경에 실패한다. (테이블의 주문상태가 '조리' 또는 '식사' 인 경우)")
    @Test
    void changeEmpty_fail_orderStatus() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문_테이블.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문_테이블.getId(), 빈_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문객 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));

        //when
        tableService.changeNumberOfGuests(주문_테이블.getId(), 빈_테이블);

        //then
        assertThat(주문_테이블.getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("테이블의 방문객 수를 변경에 실패한다. (변경할 방문객 수가 0명 미만인 경우)")
    @Test
    void changeNumberOfGuests_fail_guestZero() {
        // when
        OrderTable 비정상_테이블 = createOrderTable(4L, null, -1, false);

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문_테이블.getId(), 비정상_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문객 수를 변경에 실패한다. (테이블이 존재하지 않는 경우)")
    @Test
    void changeNumberOfGuests_fail_empty() {
        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(빈_테이블.getId(), 주문_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문객 수를 변경에 실패한다. (빈 테이블의 방문객 수를 변경하려는 경우)")
    @Test
    void changeNumberOfGuests_fail_emptyTable() {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(빈_테이블));

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(빈_테이블.getId(), 주문_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
