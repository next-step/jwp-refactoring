package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable 빈테이블일번;
    private OrderTable 빈테이블이번;
    private OrderTable 단체지정된빈테이블일번;
    private OrderTable 단체지정된빈테이블이번;
    private OrderTable 한명테이블;
    private TableGroup 테이블그룹;


    @BeforeEach
    public void setUp() {
        빈테이블일번 = new OrderTable(1L, null, 0, true);
        빈테이블이번 = new OrderTable(2L, null, 0, true);
        단체지정된빈테이블일번 = new OrderTable(1L, 1L, 0, true);
        단체지정된빈테이블이번 = new OrderTable(2L, 1L, 0, true);
        한명테이블 = new OrderTable(3L, null, 1, false);
        테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(빈테이블일번, 빈테이블이번));
    }

    @DisplayName("단체 지정 생성할 수 있다.")
    @Test
    void create() {
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(빈테이블일번, 빈테이블이번));
        given(tableGroupDao.save(any())).willReturn(테이블그룹);
        //when
        TableGroup tableGroup = tableGroupService.create(테이블그룹);
        //then
        assertAll(
                () -> assertThat(tableGroup.getId()).isNotNull(),
                () -> assertThat(tableGroup.getOrderTables()).contains(빈테이블일번, 빈테이블이번),
                () -> assertThat(tableGroup.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("주문 테이블 2개 이상부터 단체 지정할 수 있다.")
    @Test
    void createTwo() {
        //given
        TableGroup requestTableGroup = new TableGroup(1L, LocalDateTime.now(), Collections.singletonList(빈테이블일번));
        //when & then
        assertThatThrownBy(() -> tableGroupService.create(requestTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블만 단체 지정이 가능하다.")
    @Test
    void createEmpty() {
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.singletonList(빈테이블일번));
        //when & then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈테이블이 아니면 단체지정이 불가능하다.")
    @Test
    void createRegistered() {
        //given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(빈테이블일번, 한명테이블));
        //when & then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제 요청할 수 있다.")
    @Test
    void ungroup() {
        //given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(단체지정된빈테이블일번, 단체지정된빈테이블이번));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).willReturn(false);
        //when
        tableGroupService.ungroup(테이블그룹.getId());
        //then 
        Assertions.assertThat(단체지정된빈테이블일번.getTableGroupId()).isNull();
        Assertions.assertThat(단체지정된빈테이블이번.getTableGroupId()).isNull();
    }

    @DisplayName("주문 상태가 COMPLETION 가 아니면 그룹을 해제할 수 없다.")
    @Test
    void ungroupOrderStatus() {
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(단체지정된빈테이블일번, 단체지정된빈테이블이번));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).willReturn(true);
        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
