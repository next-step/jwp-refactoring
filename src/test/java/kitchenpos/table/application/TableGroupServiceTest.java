package kitchenpos.table.application;

import static kitchenpos.table.application.TableServiceTest.두명;
import static kitchenpos.table.application.TableServiceTest.진행중이_아님;
import static kitchenpos.table.application.TableServiceTest.진행중임;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
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
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() {
        // Given
        TableGroupRequest 단체지정 = new TableGroupRequest(주문테이블_목록);
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 두명));
        orderTables.add(new OrderTable(2L, 두명));
        given(orderTableDao.findAllById(any())).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(단체지정.toTableGroup());

        // When
        tableGroupService.create(단체지정);

        // Then
        verify(orderTableDao, times(1)).findAllById(any());
        verify(tableGroupDao, times(1)).save(any());
    }

    @DisplayName("단체 지정은 지정될 주문테이블과 함께 등록한다.")
    @Test
    void create_Fail_01() {
        // Given
        TableGroupRequest 단체지정 = new TableGroupRequest();

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정될 주문테이블은 2개 이상이어야 한다.")
    @Test
    void create_Fail_02() {
        // Given
        List<OrderTableRequest> 주문테이블_1개만_있음 = new ArrayList<>(Collections.singletonList(첫번째_주문테이블));
        TableGroupRequest 단체지정 = new TableGroupRequest(주문테이블_1개만_있음);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정될 주문테이블들은 모두 존재해야한다.")
    @Test
    void create_Fail_03() {
        // Given
        TableGroupRequest 단체지정 = new TableGroupRequest(주문테이블_목록);
        List<OrderTable> 단체_지정하려는_주문테이블이_존재하지_않음 = new ArrayList<>();
        given(orderTableDao.findAllById(any())).willReturn(단체_지정하려는_주문테이블이_존재하지_않음);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정될 주문테이블들은 모두 빈 테이블이어야 한다.")
    @Test
    void create_Fail_04() {
        // Given
        TableGroupRequest 단체지정 = new TableGroupRequest(주문테이블_목록);
        List<OrderTable> 비어있지않은_주문테이블_목록 = 주문테이블_목록.stream()
            .peek(orderTableRequest -> orderTableRequest.setEmpty(false))
            .map(orderTableRequest -> orderTableRequest.toOrderTable())
            .collect(Collectors.toList());
        given(orderTableDao.findAllById(any())).willReturn(비어있지않은_주문테이블_목록);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정될 주문테이블들은 다른 단체로 지정되어있지 않아야 한다.")
    @Test
    void create_Fail_05() {
        // Given
        TableGroupRequest 단체지정 = new TableGroupRequest(주문테이블_목록);
        Long 다른_주문_테이블_ID = 123L;
        List<OrderTable> 다른_단체_지정되어있는_주문테이블_목록 = 주문테이블_목록.stream()
            .peek(orderTableRequest -> orderTableRequest.setTableGroupId(다른_주문_테이블_ID))
            .map(OrderTableRequest::toOrderTable)
            .collect(Collectors.toList());
        given(orderTableDao.findAllById(any())).willReturn(다른_단체_지정되어있는_주문테이블_목록);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // Given
        TableGroupRequest 단체지정 = new TableGroupRequest(1L, 주문테이블_목록);
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 두명));
        orderTables.add(new OrderTable(2L, 두명));
        given(tableGroupDao.findById(any())).willReturn(Optional.of(단체지정.toTableGroup()));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(진행중이_아님);

        // When
        tableGroupService.ungroup(단체지정.getId());

        // Then
        verify(tableGroupDao, times(1)).findById(any());
        verify(orderDao, times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
    }

    @DisplayName("진행중(조리 or 식사)인 단계의 주문 이력이 존재할 경우 해제가 불가능하다.")
    @Test
    void ungroup_Fail() {
        // Given
        TableGroupRequest 단체지정 = new TableGroupRequest(1L, 주문테이블_목록);
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(new OrderTable(1L, 두명));
        orderTables.add(new OrderTable(2L, 두명));
        given(tableGroupDao.findById(any())).willReturn(Optional.of(단체지정.toTableGroup()));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(진행중임);

        // When & Then
        Long 단체지정_ID = 단체지정.getId();
        assertThatThrownBy(() -> tableGroupService.ungroup(단체지정_ID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private void 단체_지정_등록시_예외가_발생한다(TableGroupRequest tableGroupRequest) {
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
