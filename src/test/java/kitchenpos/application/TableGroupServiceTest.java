package kitchenpos.application;


import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.application.TableServiceTest.테이블_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 관련 기능")
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = 테이블_등록(1L, true);
        orderTable2 = 테이블_등록(2L, true);
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // when
        TableGroup tableGroup = 단체_지정_등록();

        // then
        assertThat(tableGroup.getOrderTables()).hasSize(2);
    }

    @Test
    @DisplayName("테이블 그룹을 등록시, 테이블 갯수가 2보다 작으면 실패한다.")
    void createWithUnderTwoOrderTable() {
        // given
        TableGroup tableGroup = 단체_지정(1L, Arrays.asList());

        // when-then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 등록되어 있지 않으면 실패한다.")
    void createWithDifferentOrderTable() {
        // given
        TableGroup tableGroup = 단체_지정(1L, Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1));

        // when-then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 비어있지 않거나 다른 그룹에 등록되어 있으면 실패한다.")
    void createWithNotEmptyOrderTableOrNonNullTableGroupId() {
        // given
        orderTable1.setEmpty(false);
        orderTable2.setTableGroupId(2L);
        TableGroup tableGroup = 단체_지정(1L, Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when-then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다.")
    void ungroup() {
        // given
        TableGroup tableGroup = 단체_지정_등록();
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(orderTable1);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("단체 지정을 해제할 때 식사중이거나 조리중인 테이블이 있으면 실패한다.")
    void ungroupWithCookingOrMealOrderTable() {
        // given
        TableGroup tableGroup = 단체_지정_등록();
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when-then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    public TableGroup 단체_지정_등록() {
        TableGroup tableGroup = 단체_지정(1L, Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupDao.save(any())).willReturn(tableGroup);
        return tableGroupService.create(tableGroup);
    }

    public static TableGroup 단체_지정(long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
