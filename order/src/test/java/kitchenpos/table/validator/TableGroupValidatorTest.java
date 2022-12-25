package kitchenpos.table.validator;

import static kitchenpos.order.OrderFixture.식사중;
import static kitchenpos.order.OrderFixture.조리중주문;
import static kitchenpos.order.OrderFixture.주문항목;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    OrderRepository orderRepository;
    @InjectMocks
    TableGroupValidator tableGroupValidator;

    OrderTable 일번조리중테이블;
    OrderTable 이번조리중테이블;
    OrderTable 일번식사중테이블;
    OrderTable 이번식사중테이블;
    OrderTable 일번빈테이블;
    OrderTable 이번빈테이블;
    OrderTable 주문테이블;

    @BeforeEach
    void setup() {
        OrderTable 일번테이블 = new OrderTable(1L, 0, false);
        Order 조리중 = new Order(1L, 일번테이블, COOKING, null, Collections.singletonList(주문항목));
        Order 식사중 = new Order(1L, 일번테이블, MEAL, null, Collections.singletonList(주문항목));
        일번조리중테이블 = new OrderTable(1L, 0, true);
        이번조리중테이블 = new OrderTable(2L, 0, true);
        일번식사중테이블 = new OrderTable(1L, 0, true);
        이번식사중테이블 = new OrderTable(2L, 0, true);
        일번빈테이블 = new OrderTable(1L, 0, true);
        이번빈테이블 = new OrderTable(2L, 0, true);
        주문테이블 = new OrderTable(1L, 0, false);
    }

    @Test
    @DisplayName("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만시 에러 발생")
    void validateOrderTable() {
        //given
        when(orderTableRepository.findAllById(any())).thenReturn(Arrays.asList(일번빈테이블));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(1L, null,
            Collections.singletonList(new OrderTableRequest(1L, 0, false)));

        assertThatThrownBy(() -> tableGroupValidator
            .validateTableGroup(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");

        //when & then
        when(orderTableRepository.findAllById(any())).thenReturn(Arrays.asList(일번빈테이블, 주문테이블));
        TableGroupRequest tableGroupRequest2 = new TableGroupRequest(1L, null,
            Arrays
                .asList(new OrderTableRequest(1L, 0, false), new OrderTableRequest(1L, 0, false)));

        assertThatThrownBy(() -> tableGroupValidator.validateTableGroup(tableGroupRequest2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");

    }

    @Test
    @DisplayName("테이블이 없어서 그룹 생성 실패")
    void noTableException() {
        //given
        when(orderTableRepository.findAllById(any())).thenReturn(Arrays.asList(일번빈테이블, 주문테이블));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(1L, null,
            Collections.emptyList());

        //when & then
        assertThatThrownBy(() -> tableGroupValidator.validateTableGroup(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정할 테이블 중 존재하지 않는 테이블이 존재 합니다.");
    }

    @Test
    @DisplayName("단체 지정 해제할 대상 테이블의 주문 상태가 조리중 이거나 식사중 이라면 해제 불가능")
    void cannotUngroupWhenOrderOnMealOrCooking() {
        //given
        TableGroup 조리중테이블그룹 = new TableGroup(1L);
        when(orderRepository.findAllByOrderTableIdIn(any()))
            .thenReturn(Arrays.asList(조리중주문, 조리중주문));

        //when & then
        assertThatThrownBy(() -> tableGroupValidator.validateUnGroup(조리중테이블그룹))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");

        //given
        TableGroup 조리중테이블그룹2 = new TableGroup(1L);
        when(orderRepository.findAllByOrderTableIdIn(any()))
            .thenReturn(Arrays.asList(식사중, 식사중));

        //when & then
        assertThatThrownBy(() -> tableGroupValidator.validateUnGroup(조리중테이블그룹2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
    }

    @Test
    @DisplayName("단체 지정 해제 실패")
    void ungroupFail() {
        //given
        when(orderTableRepository.findAllByTableGroupId(any()))
            .thenReturn(Collections.singletonList(new OrderTable(1L, 0, false)));
        when(orderRepository.findAllByOrderTableIdIn(any()))
            .thenReturn(Collections.singletonList(식사중));

        //when & then
        assertThatThrownBy(() -> tableGroupValidator.validateUnGroup(new TableGroup(1L)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
    }
}