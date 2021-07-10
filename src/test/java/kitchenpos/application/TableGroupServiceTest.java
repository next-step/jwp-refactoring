//package kitchenpos.application;
//
//import kitchenpos.application.order.TableGroupService;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.order.OrderTable;
//import kitchenpos.domain.order.OrderTableGroup;
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@DisplayName("주문 테이블 그룹 관리")
//@ExtendWith(MockitoExtension.class)
//class TableGroupServiceTest {
//    private TableGroupService tableGroupService;
//
//    @Mock
//    private OrderDao orderDao;
//
//    @Mock
//    private OrderTableDao orderTableDao;
//
//    @Mock
//    private TableGroupDao tableGroupDao;
//
//    private OrderTable orderTable1;
//    private OrderTable orderTable2;
//    private List<OrderTable> orderTables;
//
//    @BeforeEach
//    void setUp() {
//        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
//        orderTable1 = OrderTable.of(1L, null, 0, true);
//        orderTable2 = OrderTable.of(2L, null, 0, true);
//        orderTables = Lists.list(orderTable1, orderTable2);
//    }
//
//    @DisplayName("테이블 그룹을 추가한다.")
//    @Test
//    void create() {
//        //given
//        OrderTableGroup tableGroup = OrderTableGroup.of(1L, null, orderTables);
//
//        //and
//        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
//        given(tableGroupDao.save(any())).willReturn(tableGroup);
//
//        //when
//        OrderTableGroup actual = tableGroupService.create(tableGroup);
//
//        //then
//        assertThat(actual).isEqualTo(tableGroup);
//        assertThat(actual.getId()).isEqualTo(1L);
//    }
//
//    @DisplayName("추가할 땐 주문 테이블을 지정해야한다.")
//    @Test
//    void createTableGroupExceptionIfOrderTableIsNull() {
//        //given
//        OrderTableGroup tableGroup = OrderTableGroup.of(null, null, Lists.list());
//
//        //when
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("주문 테이블이 2개 이상이어야 한다.")
//    @Test
//    void createTableGroupExceptionIfOrderTableIsNotBiggerTwo() {
//        //given
//        OrderTableGroup tableGroup = OrderTableGroup.of(null, null, Lists.list(orderTable1));
//
//        //when
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("주문 테이블이 존재해야만 한다.")
//    @Test
//    void createTableGroupExceptionIfOrderTableIsNotExist() {
//        //given
//        OrderTableGroup tableGroup = OrderTableGroup.of(null, null, orderTables);
//        given(orderTableDao.findAllByIdIn(any())).willReturn(Lists.list());
//
//        //when
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("테이블 그룹을 지정할 때 주문 테이블은 주문 불가능 상태여야만 한다.")
//    @Test
//    void createTableGroupExceptionIfOrderTableEmptyIsTrue() {
//        //given
//        OrderTableGroup tableGroup = OrderTableGroup.of(null, null, orderTables);
//        orderTable1.setEmpty(false); // 주문 가능
//        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
//
//        //when
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("다른 주문 테이블 그룹에 속하지 않아야만 한다.")
//    @Test
//    void createTableGroupExceptionIfTheOtherTableGroupContains() {
//        //given
//        OrderTableGroup tableGroup = OrderTableGroup.of(null, null, orderTables);
//        orderTable1.setTableGroupId(99L); // 다른 테이블 그룹에 속함
//        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
//
//        //when
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//
//    @DisplayName("특정 테이블 그룹을 삭제한다.")
//    @Test
//    void ungroup() {
//        //given
//        orderTable1.setTableGroupId(1L);
//        orderTable2.setTableGroupId(1L);
//        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
//
//        //when
//        tableGroupService.ungroup(1L);
//
//        //then
//        for (OrderTable orderTable : orderTables) {
//            assertThat(orderTable.getTableGroupId()).isNull();
//        }
//    }
//
//    @DisplayName("테이블 그룹내 속한 주문 테이블 중 조리중, 식사중 상태이면 삭제할 수 없다.")
//    @Test
//    void ungroupExceptionIfOrderTableStatusIsCookingOrMeal() {
//        //given
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);
//
//        //when
//        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
//                .isInstanceOf(IllegalArgumentException.class); //then
//    }
//}
