package kitchenpos.table.application;

import static kitchenpos.table.application.TableServiceTest.두명;
import static kitchenpos.table.application.TableServiceTest.진행중이_아님;
import static kitchenpos.table.application.TableServiceTest.진행중임;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
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

    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @Mock
    private OrderDao orderDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    private List<OrderTable> 주문테이블_목록;
    private TableGroup 단체지정;
    private OrderTable 첫번째_주문테이블;
    private OrderTable 두번째_주문테이블;

    @BeforeEach
    void setup() {
        첫번째_주문테이블 = new OrderTable(1L, 두명);
        두번째_주문테이블 = new OrderTable(2L, 두명);
        주문테이블_목록 = new ArrayList<>(Arrays.asList(첫번째_주문테이블, 두번째_주문테이블));
        단체지정 = new TableGroup(1L, LocalDateTime.now(), 주문테이블_목록);
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() {
        // Given
        given(orderTableDao.findAllByIdIn(any())).willReturn(주문테이블_목록);
        given(tableGroupDao.save(any())).willReturn(단체지정);
        given(orderTableDao.save(any())).willReturn(any());

        // When
        tableGroupService.create(단체지정);

        // Then
        verify(orderTableDao, times(1)).findAllByIdIn(any());
        verify(tableGroupDao, times(1)).save(any());
        verify(orderTableDao, times(2)).save(any());
    }

    @DisplayName("단체 지정은 지정될 주문테이블과 함께 등록한다.")
    @Test
    void create_Fail_01() {
        // Given
        단체지정.setOrderTables(null);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정될 주문테이블은 2개 이상이어야 한다.")
    @Test
    void create_Fail_02() {
        // Given
        List<OrderTable> 주문테이블_1개만_있음 = new ArrayList<>(Collections.singletonList(첫번째_주문테이블));
        단체지정.setOrderTables(주문테이블_1개만_있음);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정될 주문테이블들은 모두 존재해야한다.")
    @Test
    void create_Fail_03() {
        // Given
        List<OrderTable> 단체_지정하려는_주문테이블이_존재하지_않음 = new ArrayList<>();
        given(orderTableDao.findAllByIdIn(any())).willReturn(단체_지정하려는_주문테이블이_존재하지_않음);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정될 주문테이블들은 모두 빈 테이블이어야 한다.")
    @Test
    void create_Fail_04() {
        // Given
        List<OrderTable> 비어있지않은_주문테이블_목록 = 주문테이블_목록.stream()
            .peek(orderTable -> orderTable.setEmpty(false))
            .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(any())).willReturn(비어있지않은_주문테이블_목록);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정될 주문테이블들은 다른 단체로 지정되어있지 않아야 한다.")
    @Test
    void create_Fail_05() {
        // Given
        Long 다른_주문_테이블_ID = 123L;
        List<OrderTable> 다른_단체_지정되어있는_주문테이블_목록 = 주문테이블_목록.stream()
            .peek(orderTable -> orderTable.setTableGroupId(다른_주문_테이블_ID))
            .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(any())).willReturn(다른_단체_지정되어있는_주문테이블_목록);

        // When & Then
        단체_지정_등록시_예외가_발생한다(단체지정);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // Given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(주문테이블_목록);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(진행중이_아님);
        given(orderTableDao.save(any())).willReturn(any());

        // When
        tableGroupService.ungroup(단체지정.getId());

        // Then
        verify(orderTableDao, times(1)).findAllByTableGroupId(any());
        verify(orderDao, times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
        verify(orderTableDao, times(2)).save(any());
    }

    @DisplayName("진행중(조리 or 식사)인 단계의 주문 이력이 존재할 경우 해제가 불가능하다.")
    @Test
    void ungroup_Fail() {
        // Given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(주문테이블_목록);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(진행중임);

        // When & Then
        Long 단체_지정_ID = 단체지정.getId();
        assertThatThrownBy(() -> tableGroupService.ungroup(단체_지정_ID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private void 단체_지정_등록시_예외가_발생한다(TableGroup tableGroup) {
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
