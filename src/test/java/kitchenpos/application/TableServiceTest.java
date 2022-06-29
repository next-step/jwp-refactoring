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
import java.util.Optional;

import static kitchenpos.application.ServiceTestFactory.테이블fixture생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
    }



    @Test
    void 테이블_생성() {
        OrderTable orderTable = 테이블fixture생성(1L, null, 3, true);
        given(orderTableDao.save(any())).willReturn(orderTable);

        OrderTable createdOrderTable = tableService.create(orderTable);

        assertThat(createdOrderTable.getId()).isNotNull();
    }

    @Test
    void 테이블_조회() {
        OrderTable orderTable = 테이블fixture생성(1L, null, 3, true);
        OrderTable orderTable2 = 테이블fixture생성(2L, null, 5, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable, orderTable2);
        given(orderTableDao.findAll()).willReturn(orderTables);

        List<OrderTable> searchOrdertables = tableService.list();

        assertThat(searchOrdertables.size()).isEqualTo(orderTables.size());
    }

    @Test
    void 테이블_비우기() {
        OrderTable orderTable = 테이블fixture생성(1L, null, 3, false);
        OrderTable request = 테이블fixture생성(null, null, 3, true);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        OrderTable changeEmptyTable = tableService.changeEmpty(orderTable.getId(), request);

        assertThat(changeEmptyTable.isEmpty()).isTrue();
    }

    @Test
    void 테이블_비우기_실패_주문테이블이_존재하지않음() {
        OrderTable orderTable = 테이블fixture생성(1L, null, 3, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_비우기_실패_테이블그룹이_이미존재() {
        OrderTable orderTable = 테이블fixture생성(1L, 1L, 3, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_비우기_실패_주문상태가_요리중이거나식사중() {
        OrderTable orderTable = 테이블fixture생성(1L, null, 3, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 인원수_변경() {
        OrderTable orderTable = 테이블fixture생성(1L, null, 3, false);
        OrderTable 인원수변경_5명 = 테이블fixture생성(null, null, 5, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), 인원수변경_5명);

        assertThat(result.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 인원수_변경_실패_음수인원() {
        OrderTable orderTable = 테이블fixture생성(1L, null, 3, false);
        OrderTable 음수인원수로변경 = 테이블fixture생성(null, null, -5, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), 음수인원수로변경))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 인원수_변경_실패_존재하지않는_테이블() {
        OrderTable orderTable = 테이블fixture생성(1L, null, 3, false);
        OrderTable 인원수변경_5명 = 테이블fixture생성(null, null, 5, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), 인원수변경_5명))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
