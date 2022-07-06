package kitchenpos.table.application;

import kitchenpos.order.exception.OrderStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableIdsRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.factory.TableGroupFixture.테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @InjectMocks
    TableValidator tableValidator;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    TableGroup 단체;

    OrderTable 주문테이블1;
    OrderTable 주문테이블2;

    @Test
    @DisplayName("테이블을 조회한다")
    void findOrderTableById() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId())));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블1));

        //when
        OrderTable orderTable = tableValidator.findOrderTableById(주문테이블1.getId());

        //then
        assertThat(orderTable.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("테이블을 조회할 수 없을 때")
    void findOrderTableByIdNotFound() {
        //given
        주문테이블1 = 테이블_생성(1L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            tableValidator.findOrderTableById(주문테이블1.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 조리중, 식사 중 상태가 아니어야 한다 (Happy Path)")
    void orderStatusValidate() {
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId())));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);

        tableValidator.orderStatusValidate(주문테이블1.getId());
    }

    @Test
    @DisplayName("테이블이 조리중, 식사 중 상태이면 안된다")
    void orderStatusValidateInvalidOrderStatus() {
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId())));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(() -> {
            tableValidator.orderStatusValidate(주문테이블1.getId());
        }).isInstanceOf(OrderStatusException.class)
        .hasMessageContaining(OrderStatusException.ORDER_STATUS_CAN_NOT_UNGROUP_MSG);
    }

    @Test
    @DisplayName("테이블들의 id로 테이블을 조회한다")
    void findTableAllByIdIn() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        //when
        List<OrderTable> orderTables = tableValidator.findTableAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));

        //then
        assertThat(orderTables).containsExactlyInAnyOrderElementsOf(Arrays.asList(주문테이블1, 주문테이블2));
    }

    @Test
    @DisplayName("단체 지정 시 테이블 유효성 검사 (Happy Path)")
    void orderTablesSizeValidation() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId())));

        //then
        tableValidator.orderTablesSizeValidation(Arrays.asList(주문테이블1, 주문테이블2), 단체Request);
    }

    @Test
    @DisplayName("단체 지정 시 지정할 테이블이 없으면 안된다")
    void orderTablesSizeValidationEmptyRequestTables() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList());

        //then
        assertThatThrownBy(() -> {
            tableValidator.orderTablesSizeValidation(Arrays.asList(주문테이블1, 주문테이블2), 단체Request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 시 테이블은 유효해야한다.")
    void orderTablesSizeValidationDiffSize() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        TableGroupRequest 단체Request = new TableGroupRequest(Arrays.asList(new OrderTableIdsRequest(주문테이블1.getId()),
                new OrderTableIdsRequest(주문테이블2.getId())));

        //then
        assertThatThrownBy(() -> {
            tableValidator.orderTablesSizeValidation(Arrays.asList(주문테이블1), 단체Request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체에 테이블 지정 시 유효성 검사 (Happy Path)")
    void addOrderTableValidation() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);

        //then
        tableValidator.addOrderTableValidation(Arrays.asList(주문테이블1, 주문테이블2));
    }

    @Test
    @DisplayName("단체 지정 시 테이블은 두 개 이상이어야 한다")
    void addOrderTableValidationHaveTwo() {
        //given
        주문테이블1 = 테이블_생성(1L);

        //then
        assertThatThrownBy(() -> {
            tableValidator.addOrderTableValidation(Arrays.asList(주문테이블1));
        }).isInstanceOf(OrderTableException.class)
            .hasMessageContaining(OrderTableException.ORDER_TABLE_SIZE_OVER_TWO_MSG);
    }

    @Test
    @DisplayName("단체 지정 시 테이블이 다른 단체에 지정되어 있으면 안된다")
    void addOrderTableValidationAlreadyOtherTableGroup() {
        //given
        주문테이블1 = 테이블_생성(1L, 1L, 2, false);
        주문테이블2 = 테이블_생성(2L, 1L, 3, false);

        //then
        assertThatThrownBy(() -> {
            tableValidator.addOrderTableValidation(Arrays.asList(주문테이블1, 주문테이블2));
        }).isInstanceOf(OrderTableException.class)
            .hasMessageContaining(OrderTableException.ORDER_TALBE_ALREADY_HAS_GROUP_MSG);
    }

    @Test
    @DisplayName("단체의 테이블들은 조리중/식사중 상태가 아니어야한다 (Happy Path)")
    void orderStatusByIdsValidate() {
        //given
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //then
        tableValidator.orderStatusByIdsValidate(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
    }

    @Test
    @DisplayName("단체의 테이블들은 유효해야한다")
    void orderStatusByIdsValidateInvalidTable() {
        //then
        assertThatThrownBy(() -> {
            tableValidator.orderStatusByIdsValidate(Arrays.asList());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체의 테이블들은 조리중/식사중 상태이면 안된다")
    void orderStatusByIdsValidateInvalidTableOrderStatus() {
        주문테이블1 = 테이블_생성(1L);
        주문테이블2 = 테이블_생성(2L);
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> {
            tableValidator.orderStatusByIdsValidate(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
        }).isInstanceOf(OrderStatusException.class)
            .hasMessageContaining(OrderStatusException.ORDER_STATUS_CAN_NOT_UNGROUP_MSG);
    }
}
