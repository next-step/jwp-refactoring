package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.exception.OrderTableNotFoundException;
import kitchenpos.common.exception.TableGroupAlreadyExistsException;
import kitchenpos.order.application.OrderOrderTableService;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;

@DisplayName("테이블 서비스")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderOrderTableService orderOrderTableService;
    @Mock
    private TableValidator tableValidator;
    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블 생성")
    void create_orderTable() {
        // given
        OrderTable orderTable = new OrderTable(3, false);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(orderTable);

        // when
        OrderTableResponse orderTableResponse = tableService.create(new OrderTableRequest(3, false));

        // then
        assertAll(
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests().toInt()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTable.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블 전체 조회")
    void find_all_orderTable() {
        // mocking
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(3, false), new OrderTable(2, false));
        given(orderTableRepository.findAll()).willReturn(orderTables);

        // when
        List<OrderTableResponse> orderTableResponses = tableService.list();

        // then
        assertThat(orderTableResponses.size()).isEqualTo(orderTables.size());
    }

    @Test
    @DisplayName("테이블 비움처리 요청")
    void changeEmpty_table() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);
        OrderTable orderTable = new OrderTable(3, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(orderOrderTableService.findOrderByOrderTableId(1L)).willReturn(Optional.of(new Order(LocalDateTime.now(), 1L)));

        // when
        OrderTableResponse resultOrderTableResponse = tableService.changeEmpty(1L, orderTableRequest);

        // then
        verify(tableValidator).validateExistsOrderStatusIsCookingANdMeal(any(Optional.class));
        assertThat(resultOrderTableResponse.isEmpty()).isTrue();
    }

    @TestFactory
    @DisplayName("테이블 비움처리시 오류 발생")
    List<DynamicTest> changeEmpty_exception() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);

        return Arrays.asList(
                dynamicTest("주문 테이블 조회 불가시 오류 발생.", () -> {
                    // And
                    given(orderTableRepository.findById(any())).willReturn(Optional.empty());

                    // when
                    assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                            .isInstanceOf(OrderTableNotFoundException.class)
                            .hasMessage("대상 주문테이블이 존재하지 않습니다. 입력 ID : 1");
                }),
                dynamicTest("단체지정이 된 상태일 경우 오류 발생.", () -> {
                    // And
                    OrderTable orderTable = new OrderTable(3, false);
                    orderTable.groupBy(1L);
                    given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
                    given(orderOrderTableService.findOrderByOrderTableId(1L)).willReturn(Optional.of(new Order(LocalDateTime.now(), 1L)));

                    // when
                    assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                            .isInstanceOf(TableGroupAlreadyExistsException.class)
                            .hasMessage("테이블 그룹이 이미 존재합니다.");
                    verify(tableValidator).validateExistsOrderStatusIsCookingANdMeal(any(Optional.class));
                }),
                dynamicTest("주문 상태가 COOKING이거나 MEAL상태이면 오류 발생.", () -> {
                    // And
                    OrderTable orderTable = new OrderTable(3, false);
                    orderTable.ungroup();
                    given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
                    doThrow(RuntimeException.class).when(tableValidator).validateExistsOrderStatusIsCookingANdMeal(any(Optional.class));

                    // when
                    assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                            .isInstanceOf(RuntimeException.class);
                })
        );
    }

    @Test
    @DisplayName("고객 수 변경 요청")
    void change_numberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(3, false);
        orderTable.changeEmpty(false);
        OrderTableResponse orderTableResponse = OrderTableResponse.of(1L, 1L, 5, false);
        orderTable.changeNumberOfGuests(4);
        given(orderTableRepository.findByIdAndEmptyIsFalse(anyLong())).willReturn(Optional.of(orderTable));

        // when
        OrderTableResponse resultOrderTableResponse = tableService.changeNumberOfGuests(1L,
                new OrderTableRequest(5, false));

        // then
        assertThat(resultOrderTableResponse.getNumberOfGuests()).isEqualTo(orderTableResponse.getNumberOfGuests());
    }

    @TestFactory
    @DisplayName("고객 수 변경 요청 오류 발생")
    List<DynamicTest> changeNumberOfGuests_exception() {
        return Arrays.asList(
                dynamicTest("변경하려는 고객의 수가 음수로 입력되었을 경우 오류 발생.", () -> {
                    // given
                    OrderTable orderTable = new OrderTable(3, false);
                    OrderTableRequest orderTableRequest = new OrderTableRequest(-1, false);
                    given(orderTableRepository.findByIdAndEmptyIsFalse(anyLong())).willReturn(Optional.of(orderTable));

                    // then
                    assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableRequest))
                            .isInstanceOf(IllegalArgumentException.class);
                })
        );
    }

    @Test
    @DisplayName("등록되지 않은 테이블 정보 조회 시 오류 발생")
    void tableNotFoundException() {
        assertThatThrownBy(() -> tableService.findById(10L))
                .isInstanceOf(OrderTableNotFoundException.class);
    }
}
