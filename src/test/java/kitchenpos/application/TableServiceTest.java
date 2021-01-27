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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(4);
    }

    @Test
    @DisplayName("주문테이블 등록")
    void create() {
        when(orderTableDao.save(any())).thenReturn(orderTable);

        assertThat(tableService.create(orderTable)).isNotNull();
    }

    @Test
    @DisplayName("주문테이블 조회")
    void list() {
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable));

        assertThat(tableService.list()).isNotNull();
    }

    @Test
    @DisplayName("빈테이블 수정")
    void changeEmpty() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(orderTable);

        assertThat(tableService.changeEmpty(orderTable.getId(), orderTable)).isNotNull();
    }

    @Test
    @DisplayName("빈테이블 수정시 단체지정 되어 있으면 수정 안됨")
    void callExceptionWhenChangeEmpty() {
        orderTable.setTableGroupId(1l);
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), orderTable);
            }
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("방문한 손님수 수정")
    void changeNumberOfGuests() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(orderTable);

        assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
    }

    @Test
    @DisplayName("방문한 손님수 수정시 0명 이하이면 등록 안됨")
    void callExceptionChangeNumberOfGuests1() {
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("방문한 손님수 수정시 주문테이블이 없으면 등록 안됨")
    void callExceptionChangeNumberOfGuests2(){
        when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable()));

        try {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("방문한 손님수 수정시 빈테이블이면 등록 안됨")
    void callExceptionChangeNumberOfGuests3() {
        orderTable.setEmpty(true);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
        });
    }
}