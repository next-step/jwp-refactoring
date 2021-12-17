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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable();
        주문테이블.setId(1L);
        주문테이블.setEmpty(false);
        주문테이블.setNumberOfGuests(0);
    }

    @DisplayName("주문 테이블은 방문한 손님 수,빈 테이블 상태로 등록 할 수 있다.")
    @Test
    void create() {
        given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

        OrderTable createOrderTable = tableService.create(주문테이블);

        assertThat(createOrderTable).isNotNull();
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void list() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문테이블));

        List<OrderTable> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables.size()).isEqualTo(1),
                () -> assertThat(orderTables.contains(주문테이블)).isTrue()
        );
    }

    @DisplayName("주문 테이블 상태를 변경한다.")
    @Test
    void changeEmpty() {
        OrderTable 빈주문테이블 = new OrderTable();
        빈주문테이블.setId(1L);
        빈주문테이블.setEmpty(true);
        빈주문테이블.setNumberOfGuests(0);
        OrderTable 주문테이블_상태변경 = new OrderTable();
        주문테이블_상태변경.setEmpty(false);

        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.of(빈주문테이블));
        given(orderTableDao.save(any())).willReturn(빈주문테이블);

        OrderTable changeOrderTable = tableService.changeEmpty(1L, 주문테이블_상태변경);

        assertThat(changeOrderTable.isEmpty()).isFalse();

    }

    @DisplayName("주문 테이블 상태를 요리중이거나 식사중일땐 바꿀 수 없다.")
    @Test
    void changeError() {
        OrderTable 빈주문테이블 = new OrderTable();
        빈주문테이블.setId(2L);
        빈주문테이블.setEmpty(true);
        빈주문테이블.setNumberOfGuests(0);
        OrderTable 주문테이블_상태변경 = new OrderTable();
        주문테이블_상태변경.setEmpty(false);

        given(orderTableDao.findById(2L)).willReturn(java.util.Optional.of(빈주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        assertThatThrownBy(
                () -> tableService.changeEmpty(2L, 주문테이블_상태변경)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 변경 할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable 주문테이블_손님_수_변경 = new OrderTable();
        주문테이블_손님_수_변경.setNumberOfGuests(10);
        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(주문테이블));
        given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

        OrderTable changeNumberOfGuests = tableService.changeNumberOfGuests(1L, 주문테이블_손님_수_변경);

        assertThat(changeNumberOfGuests.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("0명부터 가능하다.(음수 안됨)")
    @Test
    void changeNumberOfGuestsError() {
        OrderTable 주문테이블_손님_수_변경 = new OrderTable();
        주문테이블_손님_수_변경.setNumberOfGuests(-10);

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(1L, 주문테이블_손님_수_변경)
         ).isInstanceOf(IllegalArgumentException.class);
    }
}
