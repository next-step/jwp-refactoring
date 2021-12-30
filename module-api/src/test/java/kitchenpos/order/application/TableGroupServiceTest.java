package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableState;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.repository.TableGroupRepository;
import kitchenpos.order.repository.TableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.order.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("테이블 그룹 서비스")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableRepository tableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableGroupValidator tableGroupValidator;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    void setUp() {
        테이블1 = OrderTable.of(3, new TableState(false));
        테이블2 = OrderTable.of(3, new TableState(false));
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(1L, 2L));

        when(tableGroupRepository.save(any())).thenReturn(new TableGroup());
        TableGroupResponse response = tableGroupService.create(request);

        verify(tableGroupRepository, times(1)).save(any(TableGroup.class));
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        테이블1.changeStatus(COMPLETION);
        테이블2.changeStatus(COMPLETION);
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(new TableGroup()));

        tableGroupService.ungroup(1L);

        verify(tableGroupRepository, times(1)).findById(anyLong());
        assertThat(테이블1.getTableGroupId()).isNull();
        assertThat(테이블2.getTableGroupId()).isNull();
    }
}