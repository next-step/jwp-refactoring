package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    TableService tableService;

    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
    }

    @Test
    @DisplayName("주문 테이블 저장")
    public void save() {
        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        OrderTable savedTable = tableService.create(orderTable);
        assertThat(savedTable.getId()).isEqualTo(orderTable.getId());
    }

    @Test
    @DisplayName("전체 테이블 조회 정상")
    public void list() {
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable);
        orderTables.add(orderTable2);

        given(orderTableRepository.findAll()).willReturn(orderTables);

        assertThat(tableService.list()).contains(orderTable, orderTable2);
    }

    @Test
    @DisplayName("테이블 빈값 여부 세팅할 테이블 미존재")
    public void changeEmptyNotFoundException() {
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체로 지정된 테이블의 자리 착석여부 변경시 예외처리")
    public void changeEmtpyAlreadyInTableGroup() {
        orderTable.setEmpty(true);
        orderTable.setTableGroup(new TableGroup());

        OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setEmpty(false);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경할 테이블의 주문건 중 COOKING 또는 MEAL 상태인 건 존재시 변경 불가")
    public void changeEmtpyInCookingOrMeal() {
        orderTable.setEmpty(true);
        orderTable.setTableGroup(new TableGroup());

        OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setEmpty(false);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("테이블 empty 상태 변경 정상 처리")
    public void changEmptySuccess(){
        orderTable.setEmpty(true);

        OrderTable changeOrderTable = new OrderTable();
        changeOrderTable.setId(1L);
        changeOrderTable.setEmpty(false);

        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableRepository.save(orderTable)).willReturn(changeOrderTable);

        assertThat(tableService.changeEmpty(orderTable.getId(), changeOrderTable).isEmpty()).isEqualTo(changeOrderTable.isEmpty());
    }

    @Test
    @DisplayName("0미만 고객수로 변경 시도 시 에러 반환")
    public void changeNumerOfGuestsUnderZero(){
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("없는 테이블 고객수 변경 시도 시 에러 반환")
    public void changeNumerOfGuestsEmptyTable(){
        orderTable.setNumberOfGuests(2);

        given(orderTableRepository.findById(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("테이블 고객수 변경 정상 처리")
    public void changeNumerOfGuestsSuccess(){
        orderTable.setNumberOfGuests(2);

        given(orderTableRepository.findById(any())).willReturn(Optional.of(new OrderTable(1L,null, 1, false)));
        given(orderTableRepository.save(any())).willReturn(orderTable);

        Assertions.assertThat(tableService.changeNumberOfGuests(1L, orderTable).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }
}