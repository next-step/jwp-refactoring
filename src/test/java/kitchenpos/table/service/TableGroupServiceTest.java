package kitchenpos.table.service;

import kitchenpos.common.exception.BadRequestException;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.order.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹_생성() {
        OrderTable firstOrderTable = new OrderTable(1L, null, 10, true);
        OrderTable secondOrderTable = new OrderTable(2L, null, 5, true);
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(firstOrderTable, secondOrderTable));

        OrderTable savedFirstOrderTable = new OrderTable(1L, null, 10, true);
        OrderTable savedSecondOrderTable = new OrderTable(2L, null, 5, true);
        List<OrderTable> savedOrderTables = new ArrayList<>(Arrays.asList(savedFirstOrderTable, savedSecondOrderTable));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));

        TableGroup tableGroup = TableGroup.create();
        TableGroup savedTableGroup = TableGroup.create();
        savedTableGroup.group(savedOrderTables);

        given(orderTableRepository.findAllById(tableGroupRequest.getOrderTableIds())).willReturn(orderTables);
        given(tableGroupRepository.save(tableGroup)).willReturn(savedTableGroup);

        TableGroupResponse response = tableGroupService.create(tableGroupRequest);

        System.out.println("response = " + response.getOrderTables());
        assertThat(response.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("테이블 그룹을 생성 요청 받은 주문 테이블이 최소 2개 이상이어야 한다.")
    @Test
    void 테이블_그룹_생성_주문_테이블_2개_미만_예외() {
        OrderTable firstOrderTable = new OrderTable(1L, null, 10, true);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.singletonList(firstOrderTable.getId()));

        List<OrderTable> orderTables = new ArrayList<>(Collections.singletonList(firstOrderTable));

        given(orderTableRepository.findAllById(tableGroupRequest.getOrderTableIds())).willReturn(orderTables);


        Throwable thrown = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("주문 테이블 2개 이상 그룹화할 수 있습니다.");
    }

    @DisplayName("테이블 그룹을 생성 요청 받은 주문 테이블들이 디비에 존재해야 한다.")
    @Test
    void 테이블_그룹_생성_주문_테이블_미존재_예외() {
        OrderTable firstOrderTable = new OrderTable(1L, null, 10, true);
        OrderTable secondOrderTable = new OrderTable(2L, null, 5, true);
        List<OrderTable> orderTables = new ArrayList<>(Collections.singletonList(firstOrderTable));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));


        given(orderTableRepository.findAllById(tableGroupRequest.getOrderTableIds())).willReturn(orderTables);

        Throwable thrown = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청한 그룹화 주문 테이블 갯수와 저장된 주문 테이블 갯수가 일치하지 않습니다.");
    }

    @DisplayName("테이블 그룹을 생성 요청 받은 주문 테이블들 빈 테이블 아님 예외")
    @Test
    void 테이블_그룹_생성_주문_테이블들_빈테이블_아님_예외() {
        OrderTable firstOrderTable = new OrderTable(1L, null, 10, false);
        OrderTable secondOrderTable = new OrderTable(2L, null, 5, true);
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(firstOrderTable, secondOrderTable));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));


        given(orderTableRepository.findAllById(tableGroupRequest.getOrderTableIds())).willReturn(orderTables);

        Throwable thrown = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("빈 테이블이 아닌 주문 테이블은 그룹화할 수 없습니다.");
    }

    @DisplayName("테이블 그룹을 생성 요청 받은 주문 테이블들 이미 그룹이 존재함 예외")
    @Test
    void 테이블_그룹_생성_주문_테이블들_이미_그룹_존재_예외() {
        OrderTable firstOrderTable = new OrderTable(1L, null, 10, true);
        OrderTable secondOrderTable = new OrderTable(2L, null, 5, true);
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(firstOrderTable, secondOrderTable));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));

        TableGroup tableGroup = TableGroup.create();
        tableGroup.group(orderTables);

        given(orderTableRepository.findAllById(tableGroupRequest.getOrderTableIds())).willReturn(orderTables);

        Throwable thrown = catchThrowable(() -> tableGroupService.create(tableGroupRequest));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("이미 그룹화된 주문 테이블 입니다.");
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        OrderTable firstOrderTable = new OrderTable(1L, null, 10, true);
        OrderTable secondOrderTable = new OrderTable(2L, null, 5, true);
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(firstOrderTable, secondOrderTable));

        TableGroup tableGroup = TableGroup.create();
        tableGroup.group(orderTables);

        given(tableGroupRepository.findById(tableGroup.getId())).willReturn(Optional.of(tableGroup));

        tableGroupService.ungroup(tableGroup.getId());

        assertAll(
                () -> assertThat(firstOrderTable.getTableGroup()).isNull(),
                () -> assertThat(secondOrderTable.getTableGroup()).isNull()
        );
    }

    @DisplayName("주문들의 상태가 완료가 아닌 경우 그룹을 해제할 수 없다.")
    @Test
    void 주문_상태_완료_아님_예외() {
        OrderTable firstOrderTable = new OrderTable(1L, null, 10, true);
        OrderTable secondOrderTable = new OrderTable(2L, null, 5, true);
        List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);

        TableGroup tableGroup = TableGroup.create();
        tableGroup.group(orderTables);

        Order firstOrder = Order.of(firstOrderTable);
        Order secondOrder = Order.of(secondOrderTable);


        given(tableGroupRepository.findById(tableGroup.getId())).willReturn(Optional.of(tableGroup));

        Throwable thrown = catchThrowable(() -> tableGroupService.ungroup(tableGroup.getId()));

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessage("주문이 완료 되지 않아 그룹을 해제할 수 없습니다.");
    }
}
