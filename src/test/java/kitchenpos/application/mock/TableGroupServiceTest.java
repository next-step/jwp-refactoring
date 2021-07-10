package kitchenpos.application.mock;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup givenTableGroup = new TableGroup();

    @BeforeEach
    void setUp() {
        List<OrderTable> orderTables =  createOrderTables();
        givenTableGroup.setOrderTables(orderTables);
        givenTableGroup.setCreatedDate(LocalDateTime.parse("2021-07-04T12:00:00"));
        givenTableGroup.setId(1L);
    }

    private List<OrderTable> createOrderTables() {
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(givenOrderTable());
        orderTables.add(secondOrderTable());
        return orderTables;
    }

    @DisplayName("1개 이하 주문 테이블은 단체 지정 할 수 없다.")
    @Test
    void createFailBecauseOfOrderTablesSizeTest() {
        //given
        givenTableGroup.setOrderTables(new ArrayList<>());

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 2개 이상이어야 합니다.");

        //given

        List<OrderTable> orderTables = Arrays.asList(givenOrderTable());

        givenTableGroup.setOrderTables(orderTables);

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블이 2개 이상이어야 합니다.");
    }

    @DisplayName("주문 테이블은 모두 등록 되어 있어야 한다.")
    @Test
    void createFailBecauseOfNotExistOrderTableTest() {
        //given
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(givenOrderTable()));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블이 있습니다.");
    }

    @DisplayName("빈 테이블이어야 한다.")
    @Test
    void createFailBecauseOfNotEmptyTableTest() {
        //given
        OrderTable notEmptyOrderTable = givenOrderTable();
        notEmptyOrderTable.setEmpty(false);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(notEmptyOrderTable, secondOrderTable()));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블이 아닙니다.");
    }

    @DisplayName("이미 단체 지정된 테이블이어서는 안된다.")
    @Test
    void createFailBecauseOfHasTableGroupIdTest() {
        //given
        OrderTable hasTableGroupIdTable = givenOrderTable();
        hasTableGroupIdTable.setTableGroupId(1L);
        givenTableGroup.setOrderTables(Arrays.asList(hasTableGroupIdTable,secondOrderTable()));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(hasTableGroupIdTable, secondOrderTable()));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(givenTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 단체 지정된 테이블이 있습니다.");
    }

    @DisplayName("단체 지정.")
    @Test
    void createTest() {
        //given
        given(orderTableDao.findAllByIdIn(any())).willReturn(givenTableGroup.getOrderTables());
        given(tableGroupDao.save(givenTableGroup)).willReturn(givenTableGroup);

        //when
        tableGroupService.create(givenTableGroup);

        //then
        verify(tableGroupDao).save(givenTableGroup);
    }

    @DisplayName("주문 테이블의 주문 상태가 `조리`, `식사` 이면 해제할 수 없다.")
    @Test
    void ungroupFailBecauseOfOrderStatusTest() {
        //given
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(),any())).willReturn(true);

        //when && then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 조리나 식사 상태입니다.");
    }

    @DisplayName("단체 지정 해제.")
    @Test
    void ungroupTest() {
        //given
        OrderTable expect1 = givenOrderTable();
        expect1.setTableGroupId(1L);
        OrderTable expect2 = givenOrderTable();
        expect2.setTableGroupId(1L);
        givenTableGroup.setOrderTables(Arrays.asList(expect1,expect2));
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(givenTableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(),any())).willReturn(false);


        //when
        tableGroupService.ungroup(any());

        //then
        verify(orderTableDao).save(expect1);
        verify(orderTableDao).save(expect2);
    }


    private OrderTable givenOrderTable() {
        OrderTable givenOrderTable = new OrderTable();
        givenOrderTable.setId(1L);
        givenOrderTable.setEmpty(true);
        givenOrderTable.setNumberOfGuests(1);
        return givenOrderTable;
    }

    private OrderTable secondOrderTable() {
        OrderTable givenOrderTable = new OrderTable();
        givenOrderTable.setId(2L);
        givenOrderTable.setEmpty(true);
        givenOrderTable.setNumberOfGuests(1);
        return givenOrderTable;
    }

}