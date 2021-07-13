package kitchenpos.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.entity.OrderRepository;
import kitchenpos.order.domain.entity.OrderTable;
import kitchenpos.order.domain.entity.OrderTableRepository;
import kitchenpos.order.domain.entity.TableGroup;
import kitchenpos.order.domain.value.NumberOfGuests;
import kitchenpos.order.domain.value.OrderStatus;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
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

    OrderTable 주문테이블_테이블1;
    OrderTable 주문테이블_테이블2;
    OrderTable 주문테이블_상태변경테이블;
    OrderTable 주문테이블_인원변경테이블;

    long 존재하지않는아이디;
    OrderTableRequest 주문테이블_테이블1_리퀘스트;
    OrderTableRequest 주문테이블_상태변경테이블_리퀘스트;
    OrderTableRequest 주문테이블_인원변경테이블_리퀘스트;

    @BeforeEach
    void setUp() {
        존재하지않는아이디 = 99L;

        TableGroup 테이블그룹 = new TableGroup();

        주문테이블_테이블1 = new OrderTable(1L, NumberOfGuests.of(3), false);

        주문테이블_테이블1_리퀘스트 = new OrderTableRequest();

        주문테이블_테이블2 = new OrderTable(1L, NumberOfGuests.of(3), true);

        주문테이블_상태변경테이블 = new OrderTable();

        주문테이블_인원변경테이블 = new OrderTable(1L, NumberOfGuests.of(4), false);

        주문테이블_인원변경테이블_리퀘스트 = new OrderTableRequest(1L, 4, false);

        주문테이블_상태변경테이블_리퀘스트 = new OrderTableRequest(1L, 3, false);
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create_re() {
        //given
        when(orderTableRepository.save(any())).thenReturn(주문테이블_테이블1);

        //when
        OrderTableResponse createdOrderTable = tableService.create(주문테이블_테이블1_리퀘스트);

        //then
        assertThat(createdOrderTable.getId()).isEqualTo(주문테이블_테이블1.getId());
    }

    @Test
    @DisplayName("전체 테이블을 조회한다.")
    void list_re() {
        //given
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(주문테이블_테이블1, 주문테이블_테이블2));

        //when
        List<OrderTableResponse> orderTables = tableService.list();

        //then
        assertThat(orderTables.stream().map(OrderTableResponse::getId))
            .containsExactly(주문테이블_테이블1.getId(), 주문테이블_테이블2.getId());
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty_re() {
        //given

        //주문테이블_상태변경테이블.setEmpty(false);

        when(orderTableRepository.findById(주문테이블_테이블1.getId())).thenReturn(Optional.of(주문테이블_테이블1));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            주문테이블_테이블1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .thenReturn(false);
        when(orderTableRepository.save(주문테이블_테이블1)).thenReturn(주문테이블_테이블1);

        //when
        OrderTableResponse changedOrderTable = tableService
            .changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블_리퀘스트);

        //then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("존재하지않는 테이블의 상태변경은 실패한다.")
    void changeEmpty_with_exception_when_not_exist_orderTableId_re() {
        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(존재하지않는아이디, 주문테이블_상태변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹테이블로 지정되어있을경우 상태변경은 실패한다.")
    void changeEmpty_with_exception_re() {
        OrderTableRequest 주문테이블_상태변경테이블 = new OrderTableRequest(1L, 3, false);
        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리 또는 식사중일 경우 상태변경은 실패한다.")
    void changeEmpty_when_orderStatus_in_cooking_or_meal_re() {
        //given
        when(orderTableRepository.findById(주문테이블_테이블1.getId())).thenReturn(Optional.of(주문테이블_테이블1));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(
            주문테이블_테이블1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .thenReturn(true);

        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 손님 인원을 변경한다.")
    void changeNumberOfGuests_re() {
        //given
        when(orderTableRepository.findById(주문테이블_테이블1.getId())).thenReturn(Optional.of(주문테이블_테이블1));
        when(orderTableRepository.save(주문테이블_테이블1)).thenReturn(주문테이블_테이블1);

        //when
        OrderTableResponse changedOrderTable = tableService
            .changeNumberOfGuests(주문테이블_테이블1.getId(), 주문테이블_인원변경테이블_리퀘스트);

        //then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    @DisplayName("변경인원이 0명 미만일 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_person_smaller_than_zero_re() {
        //given
        //주문테이블_인원변경테이블.setNumberOfGuests(-1);

        //when && then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(주문테이블_테이블1.getId(), 주문테이블_인원변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("없는테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_not_exist_orderTableId_re() {
        //when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(존재하지않는아이디, 주문테이블_인원변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_orderTable_isEmpty_re() {
        //given
        //주문테이블_테이블1.setEmpty(true);

        when(orderTableRepository.findById(주문테이블_테이블2.getId())).thenReturn(Optional.of(주문테이블_테이블2));

        //when && then
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(주문테이블_테이블2.getId(), 주문테이블_인원변경테이블_리퀘스트))
            .isInstanceOf(IllegalArgumentException.class);
    }
}