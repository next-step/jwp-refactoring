package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.KitchenposException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableResponses;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 4, false);
        orderTable2 = new OrderTable(2L, null, 2, false);
    }

    @DisplayName("테이블 생성")
    @Test
    void create() {
        // given
        저장시_예상된_결과_반환(orderTable1);

        OrderTableRequest request = new OrderTableRequest(4, false);

        // when
        OrderTableResponse actual = tableService.create(request);

        // then
        assertThat(actual).isEqualTo(OrderTableResponse.from(orderTable1));
    }

    @DisplayName("테이블 목록 조회")
    @Test
    void list() {
        // given
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        Mockito.when(orderTableRepository.findAll())
            .thenReturn(orderTables);

        // when
        OrderTableResponses actual = tableService.list();

        // then
        assertThat(actual).isEqualTo(OrderTableResponses.from(orderTables));
    }

    @DisplayName("테이블 빈 테이블 여부 변경")
    @Test
    void changeEmpty() {
        // given
        아이디로_조회시_주문테이블을_반환(orderTable1);

        저장시_예상된_결과_반환(new OrderTable(1L, null, 4, true));

        // when
        OrderTableResponse actual = tableService.changeEmpty(1L, new OrderTableRequest(4, true));

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("테이블 그룹이 있을 시 변경 불가능")
    @Test
    void changeEmptyFailWhenTableGroupExists() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 4, false);
        아이디로_조회시_주문테이블을_반환(orderTable);

        OrderTableRequest request = new OrderTableRequest(4, true);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableService.changeEmpty(1L, request))
            .withMessage("주문 테이블이 그룹에 포함되어 있습니다.");
    }

    @DisplayName("고객 수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        아이디로_조회시_주문테이블을_반환(orderTable1);
        저장시_예상된_결과_반환(new OrderTable(1L, null, 4, false));

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(1L, new OrderTableRequest(4, false));

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(4);
    }

    @DisplayName("인원수 0 미만시 변경 불가능")
    @Test
    void modifyNumberOfGuestFailWhenLessThanZero() {
        // given
        OrderTableRequest request = new OrderTableRequest(-1, false);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
            .withMessage("0 이상의 고객수만 입력 가능합니다.");
    }

    @DisplayName("주문 테이블이 비어있을 시 변경 불가능")
    @Test
    void modifyNumberOfGuestFailWhenTableNotExists() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 2, true);
        아이디로_조회시_주문테이블을_반환(orderTable);

        OrderTableRequest request = new OrderTableRequest(4, false);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
            .withMessage("주문 테이블이 비어있습니다.");
    }

    private void 아이디로_조회시_주문테이블을_반환(OrderTable orderTable) {
        Mockito.when(orderTableRepository.findById(Mockito.anyLong()))
            .thenReturn(Optional.of(orderTable));
    }

    private void 저장시_예상된_결과_반환(OrderTable expected) {
        Mockito.when(orderTableRepository.save(Mockito.any()))
            .thenReturn(expected);
    }
}