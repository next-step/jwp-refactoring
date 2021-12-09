package kitchenpos.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
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

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 치킨_주문_단체테이블;
    private OrderTable 치킨2_주문_단체테이블;
    private OrderTable 치킨_주문_개인테이블;

    private TableGroup 단체주문테이블;

    @BeforeEach
    void setUp() {
        치킨_주문_단체테이블 = new OrderTable();
        치킨_주문_단체테이블.setId(1L);
        치킨_주문_단체테이블.setEmpty(true);

        치킨2_주문_단체테이블 = new OrderTable();
        치킨2_주문_단체테이블.setId(2L);
        치킨2_주문_단체테이블.setEmpty(true);

        치킨_주문_개인테이블 = new OrderTable();
        치킨_주문_개인테이블.setId(3L);
        치킨_주문_개인테이블.setEmpty(false);

        단체주문테이블 = new TableGroup();
        단체주문테이블.setId(1L);
        단체주문테이블.setOrderTables(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
        단체주문테이블.setCreatedDate(LocalDateTime.now());
    }

    @DisplayName("주문테이블이 생성된다.")
    @Test
    void create_orderTable() {
        // given
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(this.치킨_주문_단체테이블);

        // when
        OrderTable createdOrderTable = tableService.create(this.치킨_주문_단체테이블);

        // then
        Assertions.assertThat(createdOrderTable).isEqualTo(this.치킨_주문_단체테이블);
    }

    @DisplayName("주문테이블이 조회된다.")
    @Test
    void search_orderTable() {
        // given
        when(orderTableDao.findAll()).thenReturn(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));

        // when
        List<OrderTable> searchedOrderTable = tableService.list();

        // then
        Assertions.assertThat(searchedOrderTable).isEqualTo(List.of(this.치킨_주문_단체테이블, this.치킨2_주문_단체테이블));
    }

    //Todo Exception
    @DisplayName("주문테이블이 빈테이블 전환 여부가 변경된다.")
    @Test
    void update_orderTable_emptyStatus() {
        // given
        when(orderTableDao.findById(this.치킨_주문_개인테이블.getId())).thenReturn(Optional.of(this.치킨_주문_개인테이블));
        when(orderTableDao.save(this.치킨_주문_개인테이블)).thenReturn(this.치킨_주문_개인테이블);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(this.치킨_주문_개인테이블.getId(), this.치킨_주문_개인테이블);

        // then
        Assertions.assertThat(changedOrderTable).isEqualTo(this.치킨_주문_개인테이블);
    }

    @DisplayName("단체지정된 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_existOrderTableInTableGroup() {
        // given
        when(orderTableDao.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(this.치킨_주문_단체테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);
        this.치킨_주문_단체테이블.setTableGroupId(this.단체주문테이블.getId());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableService.changeEmpty(this.치킨_주문_단체테이블.getId(), this.치킨_주문_단체테이블));
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_EmptyStatus() {
        // given
        when(orderTableDao.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(this.치킨_주문_단체테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);
        this.치킨_주문_단체테이블.setTableGroupId(this.단체주문테이블.getId());

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                      .isThrownBy(() -> tableService.changeEmpty(this.치킨_주문_단체테이블.getId(), this.치킨_주문_단체테이블));
    }

    @DisplayName("주문테이블의 방문한 손님수가 변경된다.")
    @Test
    void update_orderTable_numberOfGuests() {
        // given
        when(orderTableDao.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(this.치킨_주문_단체테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(this.치킨_주문_단체테이블);

        this.치킨_주문_단체테이블.setNumberOfGuests(3);
        this.치킨_주문_단체테이블.setEmpty(false);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(this.치킨_주문_단체테이블.getId(), this.치킨_주문_단체테이블);

        // then
        assertAll(
            () -> Assertions.assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(3),
            () -> Assertions.assertThat(changedOrderTable.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문테이블의 방문한 손님수를 0이만으로 변경시 예외가 발생된다.")
    @ValueSource(ints = {-1, -9})
    @ParameterizedTest(name ="[{index}] 방문한 손님수는 [{0}]")
    void exception_updateOrderTable_underZeroCountAboutNumberOfGuest(int numberOfGuests) {
        // given
        when(orderTableDao.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(this.치킨_주문_단체테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        this.치킨_주문_단체테이블.setNumberOfGuests(numberOfGuests);
        this.치킨_주문_단체테이블.setEmpty(false);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> tableService.changeNumberOfGuests(this.치킨_주문_단체테이블.getId(), this.치킨_주문_단체테이블));

    }

    @DisplayName("빈테이블에 방문한 손님수 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_atEmptyTable() {
        // given
        when(orderTableDao.findById(this.치킨_주문_단체테이블.getId())).thenReturn(Optional.of(this.치킨_주문_단체테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        this.치킨_주문_단체테이블.setNumberOfGuests(3);
        this.치킨_주문_단체테이블.setEmpty(true);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                   .isThrownBy(() -> tableService.changeNumberOfGuests(this.치킨_주문_단체테이블.getId(), this.치킨_주문_단체테이블));
    }
}
