package kitchenpos.application;

import static java.util.Collections.*;
import static kitchenpos.utils.DataInitializer.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.DataInitializer;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 그룹 서비스")
class TableGroupServiceTest {

    TableGroupService tableGroupService;

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;

    List<OrderTable> 테이블_리스트;
    TableGroup 신규_테이블_그룹;

    @BeforeEach
    void setUp() {
        DataInitializer.reset();
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        테이블_리스트 = Arrays.asList(테이블3번_EMPTY, 테이블4번_EMPTY);
        신규_테이블_그룹 = new TableGroup(1L, 테이블_리스트);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다")
    void create() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(테이블3번_EMPTY.getId(), 테이블4번_EMPTY.getId())))
            .thenReturn(Arrays.asList(테이블3번_EMPTY, 테이블4번_EMPTY));
        when(tableGroupDao.save(신규_테이블_그룹)).thenReturn(신규_테이블_그룹);

        // when
        TableGroup savedTableGroup = tableGroupService.create(신규_테이블_그룹);

        // then
        assertThat(savedTableGroup.getId()).isEqualTo(신규_테이블_그룹.getId());
        assertThat(savedTableGroup.getOrderTables()).containsExactly(테이블3번_EMPTY, 테이블4번_EMPTY);
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패(빈 테이블이거나 목록이 2보다 작음)")
    void create_failed1() {
        // given
        신규_테이블_그룹 = new TableGroup(1L, singletonList(테이블3번_EMPTY));

        // then
        assertThatThrownBy(() -> tableGroupService.create(신규_테이블_그룹))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패(주문 테이블 DB 검증 실패)")
    void create_failed2() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(테이블3번_EMPTY.getId(), 테이블4번_EMPTY.getId())))
            .thenReturn(singletonList(테이블3번_EMPTY));

        // then
        assertThatThrownBy(() -> tableGroupService.create(신규_테이블_그룹))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패(테이블이 비어있지 않거나 이미 그룹화 된 테이블)")
    void create_failed3() {
        // given
        신규_테이블_그룹 = new TableGroup(1L, Arrays.asList(테이블2번_USING, 테이블4번_EMPTY));
        when(orderTableDao.findAllByIdIn(Arrays.asList(테이블2번_USING.getId(), 테이블4번_EMPTY.getId())))
            .thenReturn(Arrays.asList(테이블2번_USING, 테이블4번_EMPTY));

        // then
        assertThatThrownBy(() -> tableGroupService.create(신규_테이블_그룹))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 테이블 그룹을 삭제한다")
    void ungroup() {
        // given
        when(orderTableDao.findAllByTableGroupId(테이블_그룹_삭제_가능.getId())).thenReturn(테이블_그룹_삭제_가능.getOrderTables());
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
            .thenReturn(false);

        // when
        tableGroupService.ungroup(테이블_그룹_삭제_가능.getId());

        // then
        테이블_그룹_삭제_가능.getOrderTables().forEach(orderTable -> {
            assertThat(orderTable.getTableGroupId()).isNull();
        });
    }

    @Test
    @DisplayName("특정 테이블 그룹 삭제가 실패한다(조리/식사 테이블이 존재)")
    void ungroup_failed() {
        // given
        when(orderTableDao.findAllByTableGroupId(테이블_그룹_삭제_불가.getId())).thenReturn(테이블_그룹_삭제_불가.getOrderTables());
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
            .thenReturn(true);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹_삭제_불가.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
