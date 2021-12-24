package kitchenpos.table.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.OrderTableDto;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private TableValidator tableValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문테이블이 생성된다.")
    @Test
    void create_orderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);

        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(치킨_주문_단체테이블);

        OrderTableDto 주문테이블_생성전문 = OrderTableDto.of(0);

        // when
        OrderTableDto createdOrderTable = tableService.create(주문테이블_생성전문);

        // then
        assertAll(
            () -> Assertions.assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(0),
            () -> Assertions.assertThat(createdOrderTable.getEmpty()).isTrue()  
        );
    }

    @DisplayName("주문테이블이 조회된다.")
    @Test
    void search_orderTable() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);

        when(orderTableRepository.findAll()).thenReturn(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));

        // when
        List<OrderTableDto> searchedOrderTable = tableService.list();

        // then
        Assertions.assertThat(searchedOrderTable).isEqualTo(List.of(OrderTableDto.of(치킨_주문_단체테이블), OrderTableDto.of(치킨2_주문_단체테이블)));
    }

    //Todo Exception
    @DisplayName("주문테이블이 빈테이블 전환 여부가 변경된다.")
    @Test
    void update_orderTable_emptyStatus() {
        // given
        OrderTable 치킨_주문_개인테이블 =  OrderTable.of(10, false);

        when(tableValidator.getValidatedOrderTableForChangeEmpty(nullable(Long.class))).thenReturn(치킨_주문_개인테이블);

        OrderTableDto 주문테이블_빈테이블전환_요청전문 = OrderTableDto.of(0);

        // when
        OrderTableDto changedOrderTable = tableService.changeEmpty(치킨_주문_개인테이블.getId(), 주문테이블_빈테이블전환_요청전문);

        // then
        Assertions.assertThat(changedOrderTable.getEmpty()).isTrue();
    }

    @DisplayName("주문테이블의 방문한 손님수가 변경된다.")
    @Test
    void update_orderTable_numberOfGuests() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(10, false);

        when(tableValidator.getValidatedOrderTableForChangeNumberOfGuests(nullable(Long.class), anyInt())).thenReturn(치킨_주문테이블);

        OrderTableDto 손님수변경_요청전문 = OrderTableDto.of(3);

        // when
        OrderTableDto changedOrderTableDto = tableService.changeNumberOfGuests(치킨_주문테이블.getId(), 손님수변경_요청전문);

        // then
        assertAll(
            () -> Assertions.assertThat(changedOrderTableDto.getNumberOfGuests()).isEqualTo(3),
            () -> Assertions.assertThat(changedOrderTableDto.isEmpty()).isFalse()
        );
    }
}
