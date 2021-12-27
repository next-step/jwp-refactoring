package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    TableService tableService;

    @InjectMocks
    TableGroupService tableGroupService;

    TableGroupRequest 단체_지정_요청;
    List<OrderTableRequest> orderTables;
    OrderTableRequest firstOrderTableRequest;
    OrderTableRequest secondOrderTableRequest;

    @BeforeEach
    void setUp() {
        firstOrderTableRequest = OrderTableRequest.from(1L);
        secondOrderTableRequest = OrderTableRequest.from(1L);
        orderTables = Arrays.asList(firstOrderTableRequest, secondOrderTableRequest);
        단체_지정_요청 = TableGroupRequest.from(orderTables);
    }

    @Test
    void 단체_지정_시_등록하려는_주문_테이블은_주문테이블에_등록되어있어야_한다() {
        // given
        given(tableService.findOrderTables(any())).willReturn(new ArrayList<>());

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정_시_주문_테이블은_빈_테이블이어야_한다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(2, false),
                OrderTable.of(3, false));

        given(tableService.findOrderTables(any())).willReturn(orderTables);

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        OrderTable firstOrderTable = OrderTable.of(2, true);
        OrderTable secondOrderTable = OrderTable.of(3, true);
        List<OrderTable> orderTables = Arrays.asList(
                firstOrderTable,
                secondOrderTable);

        given(tableService.findAllByTableGroupId(any())).willReturn(orderTables);

        // when
        tableGroupService.ungroup(any());

        Assertions.assertThat(orderTables)
                .extracting("tableGroup")
                .containsExactly(null, null);
    }
}
