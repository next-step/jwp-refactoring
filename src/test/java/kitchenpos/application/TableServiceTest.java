package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@DisplayName("주문 테이블 서비스")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    public static final int 두명 = 2;
    public static final boolean 비어있음 = true;
    public static final boolean 진행중이_아님 = false;
    public static final boolean 진행중임 = true;

    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private TableService tableService;

    private Long 주문테이블번호 = 1L;
    private OrderTable 주문테이블;

    @BeforeEach
    void setup() {
        주문테이블 = new OrderTable(주문테이블번호, 두명);
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        // Given
        given(tableService.create(주문테이블)).willReturn(주문테이블);

        // When
        tableService.create(주문테이블);

        // Then
        verify(orderTableDao, times(1)).save(any());
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // Given
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 두명, 비어있음));
        orderTables.add(new OrderTable(2L, 두명, 비어있음));
        given(orderTableDao.findAll()).willReturn(orderTables);

        // When & Then
        assertThat(tableService.list()).hasSize(2);
        verify(orderTableDao, times(1)).findAll();
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        // Given
        주문테이블.setTableGroupId(null);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(진행중이_아님);
        given(orderTableDao.save(any())).willReturn(주문테이블);

        // When
        tableService.changeEmpty(주문테이블.getId(), 주문테이블);

        // Then
        verify(orderTableDao, times(1)).save(any());
    }

    @DisplayName("단체 지정된 주문테이블인 경우 빈 테이블로 변경이 불가능하다.")
    @Test
    void changeEmpty_Fail_01() {
        // Given
        주문테이블.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문테이블));

        // When
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블번호, 주문테이블))
            .isInstanceOf(IllegalArgumentException.class);

        // Then
        verify(orderTableDao, times(1)).findById(any());
    }

    @DisplayName("진행중(조리 or 식사)인 경우 빈 테이블로 변경이 불가능하다.")
    @Test
    void changeEmpty_Fail_02() {
        // Given
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(진행중임);

        // When
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블번호, 주문테이블))
            .isInstanceOf(IllegalArgumentException.class);

        // Then
        verify(orderTableDao, times(1)).findById(any());
        verify(orderDao, times(1)).existsByOrderTableIdAndOrderStatusIn(any(), any());
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // Given
        주문테이블.setEmpty(false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문테이블));
        given(orderTableDao.save(any())).willReturn(주문테이블);

        // When
        tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블);

        // Then
        verify(orderTableDao, times(1)).save(any());
    }

    @DisplayName("변경하려는 손님 수는 최소 1명 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_Fail_01() {
        // Given
        주문테이블.setNumberOfGuests(-1);

        // When & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블번호, 주문테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 주문 테이블은 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_Fail_02() {
        // Given
        주문테이블.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문테이블));

        // When
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블번호, 주문테이블))
            .isInstanceOf(IllegalArgumentException.class);

        // Then
        verify(orderTableDao, times(1)).findById(any());
    }

}
