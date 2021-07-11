package kitchenpos.table.application;

import static kitchenpos.table.application.TableServiceTest.두명;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 서비스")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    private static final OrderTableRequest 첫번째_주문테이블 = new OrderTableRequest(두명);
    private static final OrderTableRequest 두번째_주문테이블 = new OrderTableRequest(두명);
    private static final List<OrderTableRequest> 주문테이블_목록 = new ArrayList<>(Arrays.asList(첫번째_주문테이블, 두번째_주문테이블));

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() {
        // Given
        List<OrderTableRequest> 주문테이블_목록 = new ArrayList<>();
        주문테이블_목록.add(new OrderTableRequest(1));
        주문테이블_목록.add(new OrderTableRequest(2));
        TableGroupRequest 단체지정 = new TableGroupRequest(주문테이블_목록);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 두명));
        orderTables.add(new OrderTable(2L, 두명));
        given(orderTableRepository.findAllById(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(단체지정.toTableGroup());

        // When
        tableGroupService.create(단체지정);

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
