package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("단체 그룹 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private List<OrderTable> orderTables;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        orderTables = new ArrayList<>();

        orderTable1 = 주문_테이블_생성(1L, 15, true);
        orderTable2 = 주문_테이블_생성(1L,  2, true);
    }

    @DisplayName("비어있는 주문 테이블이 2개 이상일때 등록이 가능하다.")
    @Test
    void create_주문테이블_2개_미만_예외() {
        TableGroup tableGroup = 단체에_주문테이블_1개_등록();

        주문테이블_1개인_단체등록시_예외_발생(tableGroup);
    }

    @DisplayName("단체 지정은 중복될수 없다.")
    @Test
    void create_중복_예외() {
        TableGroup tableGroup = 단체에_주문테이블_중복_등록();

        단체에_주문테이블_중복_등록시_예외발생(tableGroup);
    }

    @DisplayName("단체를 지정한다.")
    @Test
    void create() {
        TableGroup tableGroup = 단체에_주문테이블_정상_등록();

        단체_지정전_설정(tableGroup);

        TableGroup createdTableGroup = 단체_지정_요청(tableGroup);

        단체_지정됨(createdTableGroup, tableGroup);
    }

    @DisplayName("주문 테이블의 주문 상태가 조리 또는 식사인 경우 단체 지정을 해제할 수 없다.")
    @Test
    void ungroup_예외() {
        TableGroup tableGroup = 단체에_주문테이블_정상_등록();

        주문테이블_주문상태_조리_식사_설정(tableGroup);

        주문테이블_주문상태_조리_식사_변경_요청시_예외(tableGroup);
    }

    @DisplayName("단체를 해제한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = 단체에_주문테이블_정상_등록();

        List<Long> orderTableIds = 주문_테이블_조회(tableGroup);

        주문테이블_단체_지정(tableGroup);

        단체_해제전_설정(tableGroup, orderTableIds);

        단체_해제_요청(tableGroup.getId());

        단체_해제됨();
    }

    public static TableGroup 단체_생성(long id, LocalDateTime now) {
        return new TableGroup(id, now);
    }

    private TableGroup 단체에_주문테이블_정상_등록() {
        TableGroup tableGroup = 단체_생성(1L, LocalDateTime.now());
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        tableGroup.updateOrderTables(orderTables);
        return tableGroup;
    }

    private TableGroup 단체에_주문테이블_1개_등록() {
        TableGroup tableGroup = 단체_생성(1L, LocalDateTime.now());
        orderTables.add(orderTable1);
        tableGroup.updateOrderTables(orderTables);
        return tableGroup;
    }

    private void 주문테이블_1개인_단체등록시_예외_발생(TableGroup tableGroup) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private OrderTable 주문_테이블_생성(Long id, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, numberOfGuests, isEmpty);
    }

    private TableGroup 단체에_주문테이블_중복_등록() {
        TableGroup tableGroup = 단체_생성(1L, LocalDateTime.now());
        orderTables.add(orderTable1);
        orderTables.add(orderTable2);
        orderTables.add(orderTable1);
        tableGroup.updateOrderTables(orderTables);
        return tableGroup;
    }

    private void 단체에_주문테이블_중복_등록시_예외발생(TableGroup tableGroup) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private void 단체_지정전_설정(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);
    }

    private TableGroup 단체_지정_요청(TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    private void 단체_지정됨(TableGroup createdTableGroup, TableGroup tableGroup) {
        assertThat(createdTableGroup.getId()).isEqualTo(tableGroup.getId());
        assertThat(createdTableGroup.getOrderTables()).isEqualTo(tableGroup.getOrderTables());
    }

    private void 주문테이블_주문상태_조리_식사_설정(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        orderTable1.updateTableGroupId(tableGroup.getId());

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);
    }

    private void 주문테이블_주문상태_조리_식사_변경_요청시_예외(TableGroup tableGroup) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    private List<Long> 주문_테이블_조회(TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void 단체_해제전_설정(TableGroup tableGroup, List<Long> orderTableIds) {
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);
    }

    private void 주문테이블_단체_지정(TableGroup tableGroup) {
        orderTable1.updateTableGroupId(tableGroup.getId());
        orderTable2.updateTableGroupId(tableGroup.getId());
    }

    private void 단체_해제_요청(Long id) {
        tableGroupService.ungroup(id);
    }

    private void 단체_해제됨() {
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }
}