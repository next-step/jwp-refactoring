package kitchenpos.table.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 병합")
    void create() {
        // given
        OrderTable 일번_테이블 = OrderTable.of(0, true);
        OrderTable 이번_테이블 = OrderTable.of(0, true);
        given(orderTableRepository.findAllById(any()))
                .willReturn(Arrays.asList(일번_테이블, 이번_테이블));
        given(tableGroupRepository.save(any()))
                .willReturn(TableGroup.of(Arrays.asList(일번_테이블, 이번_테이블), 2));
        // when
        final TableGroupResponse tableGroup = tableGroupService.create(new TableGroupRequest(Arrays.asList(일번_테이블, 이번_테이블)));
        // then
        assertThat(tableGroup)
                .isInstanceOf(TableGroupResponse.class);
    }

    @Test
    @DisplayName("테이블 분리")
    void ungroup() {
        // given
        final OrderTable 일번_테이블 = OrderTable.of(0, true);
        final OrderTable 이번_테이블 = OrderTable.of(0, true);
        final TableGroup tableGroup = TableGroup.of(Arrays.asList(일번_테이블, 이번_테이블), 2);
        given(orderTableRepository.findAllById(any()))
                .willReturn(Arrays.asList(일번_테이블, 이번_테이블));
        given(tableGroupRepository.findById(any()))
                .willReturn(Optional.of(TableGroup.of(Arrays.asList(일번_테이블, 이번_테이블), 2)));
        // when
        tableGroupService.ungroup(1L);
        // then
        assertThat(일번_테이블.getTableGroupId()).isNull();
    }
}
