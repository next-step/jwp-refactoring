package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.tablegroup.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("테이블 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    private Long orderTable1Id = 1L;
    private Long orderTable1TableGroupId = 1L;
    private int orderTable1NumberOfGuests = 4;
    private boolean orderTable1Empty = false;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable1 = new OrderTable(orderTable1Id, orderTable1TableGroupId, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTableResponse = new OrderTable(orderTable1Id, orderTable1TableGroupId, orderTable1NumberOfGuests, orderTable1Empty);

        when(orderTableDao.save(any())).thenReturn(orderTableResponse);

        OrderTable response = tableService.create(orderTable1);
        assertThat(response.getId()).isEqualTo(orderTableResponse.getId());
        assertThat(response.getTableGroupId()).isEqualTo(orderTableResponse.getTableGroupId());
        assertThat(response.getNumberOfGuests()).isEqualTo(orderTableResponse.getNumberOfGuests());
        assertThat(response.isEmpty()).isEqualTo(orderTableResponse.isEmpty());
    }

    @DisplayName("테이블 전체를 조회할 수 있다.")
    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable(orderTable1Id, orderTable1TableGroupId, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTable2 = new OrderTable(2L, 2L, 0, true);

        when(orderTableDao.findAll()).thenReturn(
                Arrays.asList(
                        orderTable1,
                        orderTable2
                )
        );

        assertThat(tableService.list()).contains(orderTable1, orderTable2);
    }

    @DisplayName("테이블을 빈테이블 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTableRequest = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, true);

        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTableSaved));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(orderTableRequest);

        OrderTable response = tableService.changeEmpty(orderTableSaved.getId(), orderTableRequest);

        assertThat(response.getId()).isEqualTo(orderTableRequest.getId());
        assertThat(response.getTableGroupId()).isEqualTo(orderTableRequest.getTableGroupId());
        assertThat(response.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
        assertThat(response.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
    }

    @DisplayName("주문테이블이 등록되어 있어야 한다.")
    @Test
    void 주문테이블이_올바르지_않으면_테이블상태를_변경할_수_없다_1() {
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTableRequest = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, true);

        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어있던 주문테이블이 단체지정 되어있지 않아야 한다.")
    @Test
    void 주문테이블이_올바르지_않으면_테이블상태를_변경할_수_없다_2() {
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, 2L, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTableRequest = new OrderTable(orderTable1Id, 2L, orderTable1NumberOfGuests, true);

        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTableSaved));

        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 주문 상태가 계산완료 상태여야 한다.")
    @Test
    void 주문테이블이_올바르지_않으면_빈테이블로_변경할_수_없다_3() {
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTableRequest = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, true);

        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTableSaved));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        int changeNumberOfGuests = 11;
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTableRequest = new OrderTable(orderTable1Id, null, changeNumberOfGuests, false);

        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTableSaved));
        when(orderTableDao.save(any())).thenReturn(orderTableRequest);

        OrderTable response = tableService.changeNumberOfGuests(orderTableSaved.getId(), orderTableRequest);

        assertThat(response.getNumberOfGuests()).isEqualTo(changeNumberOfGuests);
    }

    @DisplayName("손님 숫자는 0 이상이어야 한다.")
    @Test
    void 주문테이블의_손님_수가_올바르지_않으면_손님_수를_변경할_수_없다() {
        int changeNumberOfGuests = 11;
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, 0, true);
        OrderTable orderTableRequest = new OrderTable(orderTable1Id, null, changeNumberOfGuests, false);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 등록되어 있어야 한다..")
    @Test
    void 주문테이블이_올바르지_않으면_인원을_변경할_수_없다_1() {
        int changeNumberOfGuests = 11;
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, orderTable1Empty);
        OrderTable orderTableRequest = new OrderTable(orderTable1Id, null, changeNumberOfGuests, false);

        when(orderTableDao.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블은 비어있지 않아야 한다.")
    @Test
    void 주문테이블이_올바르지_않으면_인원을_변경할_수_없다_2() {
        int changeNumberOfGuests = 11;
        OrderTable orderTableSaved = new OrderTable(orderTable1Id, null, orderTable1NumberOfGuests, true);
        OrderTable orderTableRequest = new OrderTable(orderTable1Id, null, changeNumberOfGuests, false);

        when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTableSaved));

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTableSaved.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
