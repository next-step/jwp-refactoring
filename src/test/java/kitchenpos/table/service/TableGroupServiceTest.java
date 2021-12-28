package kitchenpos.table.service;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("테이블 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    TableGroupValidator tableGroupValidator;

    @Mock
    TableGroupRepository tableGroupRepository;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹_생성() {
        OrderTable firstOrderTable = OrderTable.of(1, true);
        OrderTable secondOrderTable = OrderTable.of(2, true);


        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));

        TableGroup tableGroup = TableGroup.create();
        TableGroup savedTableGroup = TableGroup.create();
        savedTableGroup.group(tableGroupValidator, tableGroupRequest.getOrderTableIds());

        given(tableGroupRepository.save(tableGroup)).willReturn(savedTableGroup);
        given(tableGroupRepository.save(tableGroup)).willReturn(savedTableGroup);

        TableGroupResponse response = tableGroupService.create(tableGroupRequest);

        assertThat(response).isNotNull();
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        OrderTable firstOrderTable = OrderTable.of(1, true);
        OrderTable secondOrderTable = OrderTable.of(2, true);
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(firstOrderTable, secondOrderTable));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(firstOrderTable.getId(), secondOrderTable.getId()));

        TableGroup tableGroup = TableGroup.create();
        tableGroup.group(tableGroupValidator, tableGroupRequest.getOrderTableIds());

        given(tableGroupRepository.findById(1L)).willReturn(Optional.of(tableGroup));

        tableGroupService.ungroup(1L);

        verify(tableGroupRepository).delete(tableGroup);
    }
}
