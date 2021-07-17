package kitchenpos.order.application;

import static kitchenpos.order.application.TableServiceTest.두명;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 서비스")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    protected static final OrderTableRequest 첫번째_주문테이블 = new OrderTableRequest(두명);
    protected static final OrderTableRequest 두번째_주문테이블 = new OrderTableRequest(두명);
    protected static final List<OrderTableRequest> 주문테이블_목록 = new ArrayList<>(Arrays.asList(첫번째_주문테이블, 두번째_주문테이블));

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableGroupValidator tableGroupValidator;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() {
        // Given
        List<OrderTableRequest> 주문테이블_목록 = new ArrayList<>();
        주문테이블_목록.add(new OrderTableRequest(1));
        주문테이블_목록.add(new OrderTableRequest(2));
        TableGroup 단체지정 = new TableGroup(1L, OrderTableRequest.toOrderTables(주문테이블_목록));

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 두명));
        orderTables.add(new OrderTable(2L, 두명));
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(단체지정);

        // When
        tableGroupService.create(new TableGroupRequest(주문테이블_목록));

        // Then
        verify(orderTableRepository, times(1)).findAllById(any());
        verify(tableGroupRepository, times(1)).save(any());
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // Given
        TableGroupRequest 단체지정 = new TableGroupRequest(1L, 주문테이블_목록);
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 두명));
        orderTables.add(new OrderTable(2L, 두명));
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(단체지정.toTableGroup()));

        // When
        tableGroupService.ungroup(단체지정.getId());

        // Then
        verify(tableGroupRepository, times(1)).findById(any());
    }

}
