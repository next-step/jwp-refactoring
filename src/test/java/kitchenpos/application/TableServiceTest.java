package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.dto.CreateOrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.AfterEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    public static final OrderTable 일번_테이블 = OrderTable.of(0, true);
    public static final OrderTable 이번_테이블 = OrderTable.of(0, true);


    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    TableService tableService;

    @AfterEach
    void afterEach() {
    }

    @Test
    @DisplayName("테이블 추가")
    void create() {
        // given
        orderTableSave(일번_테이블);
        // when
        OrderTableResponse orderTableResponse = tableService.create(일번_테이블);
        // then
        assertThat(orderTableResponse).isInstanceOf(OrderTableResponse.class);
    }

    //
//    @Test
//    @DisplayName("테이블 전체 조회")
//    void list() {
//        // given
//        orderTableFindyAll();
//        // when
//        final List<OrderTable> list = tableService.list();
//        // then
//        assertThat(list).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("테이블 빈 값 상태 변경")
//    void changeEmpty() {
//        // given
//        orderTableFindById(orderTableDao, 일번_테이블);
//        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
//                .willReturn(false);
//        일번_테이블.setEmpty(false);
//        // given
//        orderTableSave(orderTableDao, 일번_테이블);
//
//        // when
//        final OrderTable orderTable = tableService.changeEmpty(일번_테이블.getId(), 일번_테이블);
//        // then
//        assertThat(orderTable.isEmpty()).isFalse();
//    }
//
//    @Test
//    @DisplayName("테이블 사람수 변경")
//    void changeNumberOfGuests() {
//        // given
//        orderTableFindById(orderTableDao, 일번_테이블);
//        일번_테이블.setEmpty(false);
//        일번_테이블.setNumberOfGuests(5);
//        orderTableSave(orderTableDao, 일번_테이블);
//        // when
//        final OrderTable orderTable = tableService.changeNumberOfGuests(일번_테이블.getId(), 일번_테이블);
//        // then
//        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
//    }
//
    private void orderTableSave(final OrderTable 일번테이블) {
        given(orderTableRepository.save(any()))
                .willReturn(일번테이블);
    }

//
//    private void orderTableFindById(final OrderTableDao orderTableDao, final OrderTable 일번테이블) {
//        given(orderTableDao.findById(any()))
//                .willReturn(Optional.of(일번테이블));
//    }
//
//    private void orderTableFindyAll() {
//        given(orderTableDao.findAll())
//                .willReturn(Arrays.asList(일번_테이블));
//    }
}
