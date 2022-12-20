//package kitchenpos.appliction;
//
//import kitchenpos.table.application.TableGroupService;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.order.domain.OrderStatus;
//import kitchenpos.table.domain.OrderTable;
//import kitchenpos.table.domain.TableGroup;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//
//import static kitchenpos.domain.OrderTableFixture.주문테이블;
//import static kitchenpos.domain.TableGroupFixture.테이블그룹;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("테이블 그룹 테스트")
//public class TableGroupServiceTest {
//
//    @Mock
//    private OrderDao orderDao;
//    @Mock
//    private OrderTableDao orderTableDao;
//    @Mock
//    private TableGroupDao tableGroupDao;
//    @InjectMocks
//    private TableGroupService tableGroupService;
//
//    private OrderTable 테이블1;
//    private OrderTable 테이블2;
//    private OrderTable 테이블3;
//
//    private TableGroup 테이블그룹;
//
//    @BeforeEach
//    void setup() {
//        테이블1 = 주문테이블(1L, null, 0, true);
//        테이블2 = 주문테이블(2L, null, 0, true);
//        테이블3 = 주문테이블(3L, null, 0, true);
//
//        테이블그룹 = 테이블그룹(1L, Arrays.asList(테이블1, 테이블2, 테이블3));
//    }
//
//    @DisplayName("테이블 그룹을 생성한다")
//    @Test
//    void 테이블_그룹_생성() {
//        // given
//        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L, 3L))).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));
//        given(tableGroupDao.save(테이블그룹)).willReturn(테이블그룹);
//        given(orderTableDao.save(테이블1)).willReturn(테이블1);
//        given(orderTableDao.save(테이블2)).willReturn(테이블2);
//        given(orderTableDao.save(테이블3)).willReturn(테이블3);
//
//        // when
//        TableGroup tableGroup = tableGroupService.create(테이블그룹);
//
//        // then
//        assertAll(
//                () -> assertThat(tableGroup.getOrderTables()).hasSize(3),
//                () -> assertThat(테이블그룹.getOrderTables().stream()
//                        .allMatch(orderTable -> orderTable.getTableGroupId().equals(테이블그룹.getId()))).isTrue()
//        );
//    }
//
//    @DisplayName("테이블 그룹을 해제한다")
//    @Test
//    void 테이블_그룹_해제() {
//        // given
//        given(orderTableDao.findAllByTableGroupId(테이블그룹.getId())).willReturn(테이블그룹.getOrderTables());
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(테이블1.getId(), 테이블2.getId(), 테이블3.getId()),
//                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
//
//        // when
//        tableGroupService.ungroup(테이블그룹.getId());
//
//        // then
//        assertThat(테이블그룹.getOrderTables().stream()
//                .allMatch(orderTable -> orderTable.getTableGroupId() == null)).isTrue();
//
//    }
//
//    @DisplayName("단일 테이블로 테이블 그룹을 생성한다")
//    @Test
//    void 단일_테이블로_테이블_그룹_생성() {
//        // given
//        테이블그룹.setOrderTables(Arrays.asList(테이블1));
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableGroupService.create(테이블그룹)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("존재하지 않은 테이블로 테이블 그룹을 생성한다")
//    @Test
//    void 존재하지_않은_테이블로_테이블_그룹_생성() {
//        // given
//        given(orderTableDao.findAllByIdIn(Arrays.asList(테이블1.getId(), 테이블2.getId(), 테이블3.getId())))
//                .willReturn(Arrays.asList(테이블1, 테이블2));
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableGroupService.create(테이블그룹)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("비어있지 않은 테이블로 테이블 그룹을 생성한다")
//    @Test
//    void 비어_있지_않은_테이블로_테이블_그룹_생성() {
//        // given
//        테이블1.setEmpty(false);
//        given(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L, 3L))).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableGroupService.create(테이블그룹)
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("주문상태가 완료가 아닌 주문이 있는 테이블이 포함된 테이블 그룹을 해제한다")
//    @Test
//    void 주문상태_완료가_아닌_주문이_있는_테이블의_테이블그룹_해제() {
//        // given
//        given(orderTableDao.findAllByTableGroupId(테이블그룹.getId())).willReturn(테이블그룹.getOrderTables());
//        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(테이블1.getId(), 테이블2.getId(), 테이블3.getId()),
//                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);
//
//        // when & then
//        assertThatThrownBy(
//                () -> tableGroupService.ungroup(테이블그룹.getId())
//        ).isInstanceOf(IllegalArgumentException.class);
//    }
//}