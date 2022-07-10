package kichenpos.application;

import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.validator.OrderStatusValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    OrderStatusValidator orderStatusValidator;
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
        given(orderTableRepository.findAllById(any()))
                .willReturn(Arrays.asList(일번_테이블, 이번_테이블));
        given(tableGroupRepository.findById(any()))
                .willReturn(Optional.of(TableGroup.of(Arrays.asList(일번_테이블, 이번_테이블), 2)));
        doNothing().when(orderStatusValidator).validateTableSeparate(any());
        // when
        tableGroupService.ungroup(1L);
        // then
        assertThat(일번_테이블.getTableGroupId()).isNull();
    }
}
