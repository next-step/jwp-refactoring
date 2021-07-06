package kitchenpos.application.unit;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.unit.TableServiceTest.주문테이블_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("단체지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체지정 등록")
    @Test
    public void 단체지정_등록_확인() throws Exception {
        //given
        OrderTable orderTable1 = 주문테이블_등록됨(1L, null, true, 5);
        OrderTable orderTable2 = 주문테이블_등록됨(2L, null, true, 5);
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(orderTable1, orderTable2));
        TableGroup tableGroup = 단체지정_등록됨(1L, Arrays.asList(orderTable1, orderTable2), LocalDateTime.now());
        TableGroup createTableGroup = 단체지정_생성(Arrays.asList(orderTable1, orderTable2), LocalDateTime.now());
        given(tableGroupDao.save(createTableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        //when
        TableGroup saveTableGroup = tableGroupService.create(createTableGroup);

        //then
        assertThat(saveTableGroup.getId()).isNotNull();
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 없는 경우")
    @Test
    public void 주문테이블이없는경우_단체지정_등록_예외() throws Exception {
        //given
        TableGroup createTableGroup = 단체지정_생성(Arrays.asList(), LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 2개 미만인 경우")
    @Test
    public void 주문테이블이2개미만인경우_단체지정_등록_예외() throws Exception {
        //given
        OrderTable orderTable = 주문테이블_등록됨(1L, null, true, 5);
        TableGroup createTableGroup = 단체지정_생성(Arrays.asList(orderTable), LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 입력한 주문테이블의 수와 실제 저장되었던 주문테이블 수가 다른 경우")
    @Test
    public void 입력한주문테이블수와저장된주문테이블수가다른경우_단체지정_등록_예외() throws Exception {
        //given
        OrderTable orderTable1 = 주문테이블_등록됨(1L, null, true, 5);
        OrderTable orderTable2 = 주문테이블_등록됨(2L, null, true, 5);
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(orderTable1));

        //when
        //then
        TableGroup createTableGroup = 단체지정_생성(Arrays.asList(orderTable1, orderTable2), LocalDateTime.now());
        assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 빈테이블이 아닌경우")
    @Test
    public void 주문테이블이빈테이블이아닌경우_단체지정_등록_예외() throws Exception {
        //given
        OrderTable orderTable1 = 주문테이블_등록됨(1L, null, false, 5);
        OrderTable orderTable2 = 주문테이블_등록됨(2L, null, true, 5);
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(orderTable1, orderTable2));

        //when
        //then
        TableGroup createTableGroup = 단체지정_생성(Arrays.asList(orderTable1, orderTable2), LocalDateTime.now());
        assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 단체지정이 이미 되어있는 경우")
    @Test
    public void 주문테이블이이미단체지정이되어있는경우_단체지정_등록_예외() throws Exception {
        //given
        OrderTable orderTable1 = 주문테이블_등록됨(1L, 1L, true, 5);
        OrderTable orderTable2 = 주문테이블_등록됨(2L, null, true, 5);
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).willReturn(Arrays.asList(orderTable1, orderTable2));

        //when
        //then
        TableGroup createTableGroup = 단체지정_생성(Arrays.asList(orderTable1, orderTable2), LocalDateTime.now());
        assertThatThrownBy(() -> tableGroupService.create(createTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 해제")
    @Test
    public void 단체지정_해제_확인() throws Exception {
        //given
        OrderTable orderTable1 = 주문테이블_등록됨(1L, 1L, false, 5);
        OrderTable orderTable2 = 주문테이블_등록됨(2L, 1L, false, 5);
        단체지정_등록됨(1L, Arrays.asList(orderTable1, orderTable2), LocalDateTime.now());
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        orderTable1.setTableGroupId(null);
        orderTable2.setTableGroupId(null);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        //when
        tableGroupService.ungroup(1L);

        //then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    @DisplayName("단체지정 해제 예외 - 주문상태가 조리나 식사인 경우")
    @Test
    public void 주문상태가조리나식사인경우_단체지정_해제_확인() throws Exception {
        //given
        OrderTable orderTable1 = 주문테이블_등록됨(1L, 1L, false, 5);
        OrderTable orderTable2 = 주문테이블_등록됨(2L, 1L, false, 5);
        단체지정_등록됨(1L, Arrays.asList(orderTable1, orderTable2), LocalDateTime.now());
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }

    public TableGroup 단체지정_등록됨(Long id, List<OrderTable> orderTables, LocalDateTime createdDate) {
        TableGroup tableGroup = 단체지정_생성(orderTables, createdDate);
        tableGroup.setId(id);
        return tableGroup;
    }

    public TableGroup 단체지정_생성(List<OrderTable> orderTables, LocalDateTime createdDate) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        tableGroup.setCreatedDate(createdDate);
        return tableGroup;
    }
}
