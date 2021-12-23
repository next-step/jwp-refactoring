//package kitchenpos.application;
//
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//
//import static kitchenpos.fixture.TableFixture.회사A_테이블1;
//import static kitchenpos.fixture.TableFixture.회사A_테이블2;
//import static kitchenpos.fixture.TableGroupFixture.회사A_단체_테이블;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//class TableGroupServiceTest {
//
//    @InjectMocks
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
//    @DisplayName("테이블 그룹 생성 성공 테스트")
//    @Test
//    void create_success() {
//        // given
//        OrderTable 테이블1 = new OrderTable();
//        테이블1.setId(1L);
//        테이블1.setEmpty(true);
//
//        OrderTable 테이블2 = new OrderTable();
//        테이블2.setId(2L);
//        테이블2.setEmpty(true);
//
//        TableGroup 요청_테이블_그룹 = new TableGroup();
//        요청_테이블_그룹.setOrderTables(Arrays.asList(테이블1, 테이블2));
//
//        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블2));
//        given(tableGroupDao.save(any(TableGroup.class))).willReturn(회사A_단체_테이블);
//        given(orderTableDao.save(테이블1)).willReturn(회사A_테이블1);
//        given(orderTableDao.save(테이블2)).willReturn(회사A_테이블2);
//
//        // when
//        TableGroup 생성된_테이블_그룹 = tableGroupService.create(요청_테이블_그룹);
//
//        // then
//        assertThat(생성된_테이블_그룹).isEqualTo(회사A_단체_테이블);
//    }
//
//    @DisplayName("테이블 그룹 생성 실패 테스트 - 테이블 그룹이될 주문 테이블 수가 2개 미만")
//    @Test
//    void create_failure_invalidSize() {
//        // given
//        OrderTable 테이블1 = new OrderTable();
//        테이블1.setId(1L);
//        테이블1.setEmpty(true);
//
//        TableGroup 요청_테이블_그룹 = new TableGroup();
//        요청_테이블_그룹.setOrderTables(Arrays.asList(테이블1));
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> tableGroupService.create(요청_테이블_그룹));
//    }
//
//    @DisplayName("테이블 그룹 생성 실패 테스트 - 테이블 그룹이 될 주문 테이블이 존재하지 않음")
//    @Test
//    void create_failure_notFoundOrderTable() {
//        // given
//        OrderTable 테이블1 = new OrderTable();
//        테이블1.setId(1L);
//        테이블1.setEmpty(true);
//
//        OrderTable 테이블2 = new OrderTable();
//        테이블2.setId(2L);
//        테이블2.setEmpty(true);
//
//        TableGroup 요청_테이블_그룹 = new TableGroup();
//        요청_테이블_그룹.setOrderTables(Arrays.asList(테이블1, 테이블2));
//
//        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1));
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> tableGroupService.create(요청_테이블_그룹));
//    }
//
//    @DisplayName("테이블 그룹 생성 실패 테스트 - 주문 테이블에 다른 테이블 그룹이 존재함")
//    @Test
//    void create_failure_alreadyExistTableGroup() {
//        // given
//        OrderTable 테이블1 = new OrderTable();
//        테이블1.setId(1L);
//        테이블1.setTableGroupId(2L);
//        테이블1.setEmpty(true);
//
//        OrderTable 테이블2 = new OrderTable();
//        테이블2.setId(2L);
//        테이블1.setTableGroupId(2L);
//        테이블2.setEmpty(true);
//
//        TableGroup 요청_테이블_그룹 = new TableGroup();
//        요청_테이블_그룹.setOrderTables(Arrays.asList(테이블1, 테이블2));
//
//        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블2));
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> tableGroupService.create(요청_테이블_그룹));
//    }
//
//    @DisplayName("테이블 그룹 생성 실패 테스트 - 주문 테이블이 비어있지 않음")
//    @Test
//    void create_failure_notEmpty() {
//        // given
//        OrderTable 테이블1 = new OrderTable();
//        테이블1.setId(1L);
//        테이블1.setEmpty(false);
//
//        OrderTable 테이블2 = new OrderTable();
//        테이블2.setId(2L);
//        테이블2.setEmpty(false);
//
//        TableGroup 요청_테이블_그룹 = new TableGroup();
//        요청_테이블_그룹.setOrderTables(Arrays.asList(테이블1, 테이블2));
//
//        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블2));
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> tableGroupService.create(요청_테이블_그룹));
//    }
//
//    @DisplayName("테이블 그룹 해제 성공 테스트")
//    @Test
//    void ungroup_success() {
//        // given
//        given(orderTableDao.findAllByTableGroupId(any(Long.class))).willReturn(Arrays.asList(회사A_테이블1, 회사A_테이블2));
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
//
//        // when
//        tableGroupService.ungroup(회사A_단체_테이블.getId());
//
//        // then
//        assertAll(
//                () -> assertThat(회사A_테이블1.getTableGroupId()).isNull()
//                , () -> assertThat(회사A_테이블1.getTableGroupId()).isNull()
//        );
//    }
//
//    @DisplayName("테이블 그룹 해제 실패 테스트 - 테이블 그룹에 속해있는 주문 테이블이 COOKING 또는 MEAL")
//    @Test
//    void ungroup_failure_orderStatus() {
//        // given
//        given(orderTableDao.findAllByTableGroupId(any(Long.class))).willReturn(Arrays.asList(회사A_테이블1, 회사A_테이블2));
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);
//
//        // when & then
//        assertThatIllegalArgumentException()
//                .isThrownBy(() -> tableGroupService.ungroup(회사A_단체_테이블.getId()));
//    }
//}
