package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.OrderAlreadyExistsException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.TableGroupAlreadyExistsException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.utils.domain.OrderTableObjects;

@DisplayName("테이블 서비스")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderService orderService;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable changeEmptyOrderTable;
    private OrderTable beforeOrderTable;
    private OrderTable createOrderTable;
    private OrderTable changeNumberOrderTable;
    private List<OrderTable> orderTables;
    private OrderTableRequest orderTableRequest;

    @BeforeEach
    void setUp() {
        OrderTableObjects orderTableObjects = new OrderTableObjects();
        changeEmptyOrderTable = orderTableObjects.getOrderTable1();
        beforeOrderTable = orderTableObjects.getOrderTable2();
        changeNumberOrderTable = orderTableObjects.getOrderTable3();
        createOrderTable = orderTableObjects.getOrderTable4();
        orderTableRequest = orderTableObjects.getOrderTableRequest4();
        orderTables = orderTableObjects.getOrderTables();
    }

    @Test
    @DisplayName("주문 테이블 생성")
    void create_orderTable() {
        // given
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(createOrderTable);

        // when
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        // then
        assertAll(
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(createOrderTable.getNumberOfGuests().toInt()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(createOrderTable.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블 전체 조회")
    void find_all_orderTable() {
        // mocking
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
        beforeOrderTable.changeEmpty(false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(beforeOrderTable));

        // when
        OrderTableResponse resultOrderTableResponse = tableService.changeEmpty(1L, orderTableRequest);

        // then
        verify(orderService).validateExistsOrderStatusIsCookingANdMeal(any());
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
                    changeEmptyOrderTable.setTableGroup(new TableGroup());
                    given(orderTableRepository.findById(any())).willReturn(Optional.of(changeEmptyOrderTable));

                    // when
                    assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableRequest))
                            .isInstanceOf(TableGroupAlreadyExistsException.class)
                            .hasMessage("테이블 그룹이 이미 존재합니다.");
                }),
                dynamicTest("주문 상태가 COOKING이거나 MEAL상태이면 오류 발생.", () -> {
                    // And
                    changeEmptyOrderTable.setTableGroup(null);
                    given(orderTableRepository.findById(any())).willReturn(Optional.of(changeEmptyOrderTable));
                    doThrow(OrderAlreadyExistsException.class).when(orderService).validateExistsOrderStatusIsCookingANdMeal(any());

                    // when
                    assertThatThrownBy(() -> tableService.changeEmpty(changeEmptyOrderTable.getId(), orderTableRequest))
                            .isInstanceOf(OrderAlreadyExistsException.class);
                })
        );
    }

    @Test
    @DisplayName("고객 수 변경 요청")
    void change_numberOfGuests() {
        // given
        beforeOrderTable.changeNumberOfGuests(3);
        beforeOrderTable.changeEmpty(false);
        OrderTableResponse orderTableResponse = OrderTableResponse.of(1L, 1L, 5, false);
        changeNumberOrderTable.changeNumberOfGuests(4);
        given(orderTableRepository.findByIdAndEmptyIsFalse(anyLong())).willReturn(Optional.of(beforeOrderTable));

        // when
        OrderTableResponse resultOrderTableResponse = tableService.changeNumberOfGuests(1L, new OrderTableRequest(5, false));

        // then
        assertThat(resultOrderTableResponse.getNumberOfGuests()).isEqualTo(orderTableResponse.getNumberOfGuests());
    }

    @TestFactory
    @DisplayName("고객 수 변경 요청 오류 발생")
    List<DynamicTest> changeNumberOfGuests_exception() {
        return Arrays.asList(
                dynamicTest("변경하려는 테이블 조회가 실패 하거나 비어있는 테이블 일 경우 오류 발생.", () -> {
                    // given
                    OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);
                    given(orderTableRepository.findByIdAndEmptyIsFalse(anyLong())).willReturn(Optional.empty());

                    // then
                    assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, orderTableRequest))
                            .isInstanceOf(NonEmptyOrderTableNotFoundException.class)
                            .hasMessage("비어있지 않은 테이블 대상이 존재하지 않습니다. 입력 ID : 100");
                }),
                dynamicTest("변경하려는 고객의 수가 음수로 입력되었을 경우 오류 발생.", () -> {
                    // given
                    OrderTableRequest orderTableRequest = new OrderTableRequest(-1, false);
                    given(orderTableRepository.findByIdAndEmptyIsFalse(anyLong())).willReturn(Optional.of(changeNumberOrderTable));

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
