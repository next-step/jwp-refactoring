package kitchenpos.tablegroup.application;

import tablegroup.application.TableGroupService;
import kitchenpos.ordertable.domain.*;
import kitchenpos.ordertable.dto.OrderTableRequest;
import tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tablegroup.domain.OrderTables;
import tablegroup.domain.TableGroup;
import tablegroup.domain.TableGroupRepository;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.common.Message.ERROR_TABLES_SHOULD_ALL_BE_REGISTERED_TO_BE_GROUPED;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private final Long 단체지정_안됨 = null;
    private final boolean 비어있음 = true;
    private final OrderTable 비어있는_첫번째_테이블 = new OrderTable(1L, 단체지정_안됨, 3, 비어있음);
    private final OrderTable 비어있는_두번째_테이블 = new OrderTable(2L, 단체지정_안됨, 3, 비어있음);
    private TableGroup 단체지정 = new TableGroup(new OrderTables(Arrays.asList(비어있는_첫번째_테이블, 비어있는_두번째_테이블)));

    @DisplayName("단체 지정을 등록한다")
    @Test
    void 단체지정_등록() {
        //Given
        OrderTable 첫번째테이블_요청 = new OrderTable(3, 비어있음);
        OrderTable 두번째테이블_요청 = new OrderTable(3, 비어있음);
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(1L, LocalDateTime.now(),
                Arrays.asList(OrderTableRequest.of(첫번째테이블_요청), OrderTableRequest.of(두번째테이블_요청)));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(첫번째테이블_요청, 두번째테이블_요청));
        when(tableGroupRepository.save(any())).thenReturn(단체지정);

        //When
        tableGroupService.create(단체지정_요청);

        //Then
        verify(tableGroupRepository, times(1)).save(any());
    }

    @DisplayName("등록되지 않은 주문 테이블이 입력된 경우, 예외가 발생한다")
    @Test
    void 주문테이블이_등록되지_않은_경우_예외발생() {
        //Given
        OrderTable 첫번째테이블_요청 = new OrderTable(3, 비어있음);
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(1L, LocalDateTime.now(),
                Arrays.asList(OrderTableRequest.of(첫번째테이블_요청), OrderTableRequest.of(첫번째테이블_요청)));
        when(orderTableRepository.findAllByIdIn(any())).thenReturn(Arrays.asList(첫번째테이블_요청));

        //When + Then
        Throwable 테이블_등록안됨_예외 = catchThrowable(() -> tableGroupService.create(단체지정_요청));
        assertThat(테이블_등록안됨_예외).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_TABLES_SHOULD_ALL_BE_REGISTERED_TO_BE_GROUPED.showText());

    }

    @DisplayName("단체 지정을 취소할 수 있다")
    @Test
    void 단체지정_취소() {
        //Given
        final OrderTable 비어있는_첫번째_테이블 = new OrderTable(1L, 단체지정_안됨, 3, 비어있음);
        final OrderTable 비어있는_두번째_테이블 = new OrderTable(2L, 단체지정_안됨, 3, 비어있음);
        TableGroup 단체지정 = new TableGroup(new OrderTables(Arrays.asList(비어있는_첫번째_테이블, 비어있는_두번째_테이블)));

        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(비어있는_첫번째_테이블, 비어있는_두번째_테이블));

        //When
        //tableGroupService.ungroup(단체지정.getId());

        //Then
        verify(orderTableRepository, times(1)).findAllByTableGroupId(any());
    }
}
