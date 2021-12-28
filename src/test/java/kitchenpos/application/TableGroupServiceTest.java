package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.KitchenposException;

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

    private List<OrderTable> orderTables;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, true),
            new OrderTable(2L, null, 2, true));

        tableGroup = new TableGroup(1L, new OrderTables(orderTables));

    }

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        // given
        테이블_조회_결과_반환(orderTables);
        테이블_그룹_저장_결과_반환();

        List<OrderTableIdRequest> requestTables = Arrays.asList(
            new OrderTableIdRequest(1L),
            new OrderTableIdRequest(2L));
        TableGroupRequest request = new TableGroupRequest(requestTables);

        // when
        TableGroupResponse actual = tableGroupService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getOrderTables()).hasSize(2),
            () -> assertThat(actual.getOrderTables().get(0)).isNotNull(),
            () -> assertThat(actual.getOrderTables().get(0).isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블이 없거나 2개 미만일 시 생성 불가능")
    @Test
    void createTableGroupFailWhenOrderTablesSizeIsLessThanTwo() {
        // given
        List<OrderTableIdRequest> requestTables = Arrays.asList(new OrderTableIdRequest(2L));
        TableGroupRequest request = new TableGroupRequest(requestTables);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableGroupService.create(request))
            .withMessage("테이블 그룹을 생성하기 위해 2개 이상의 테이블이 필요합니다.");
    }

    @DisplayName("입력받은 주문 테이블 개수와 실제 주문 테이블 개수가 다른 경우(메뉴에 없는 주문 테이블) 생성 불가능")
    @Test
    void createTableGroupFailWhenTableNumberIsDifferent() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(2L, null, 2, true));
        테이블_조회_결과_반환(orderTables);

        List<OrderTableIdRequest> requestTables = Arrays.asList(
            new OrderTableIdRequest(1L),
            new OrderTableIdRequest(2L));
        TableGroupRequest request = new TableGroupRequest(requestTables);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableGroupService.create(request))
            .withMessage("주문 테이블의 개수가 다릅니다.");
    }

    @DisplayName("사용중인 테이블이 포함되어 있는 경우 생성 불가능")
    @Test
    void createTableGroupFailWhenOrderTableIsEmpty() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, false),
            new OrderTable(2L, null, 2, true));
        테이블_조회_결과_반환(orderTables);

        List<OrderTableIdRequest> requestTables = Arrays.asList(
            new OrderTableIdRequest(1L),
            new OrderTableIdRequest(2L));
        TableGroupRequest request = new TableGroupRequest(requestTables);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableGroupService.create(request))
            .withMessage("사용중인 테이블이 있습니다.");
    }

    @DisplayName("다른 그룹에 등록되어 있는 주문 테이블이 포함되어 있는 경우 생성 불가능")
    @Test
    void createTableGroupFailWhenOrderTableIsInOtherGroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, true),
            new OrderTable(2L, new TableGroup(), 2, true));
        테이블_조회_결과_반환(orderTables);

        List<OrderTableIdRequest> requestTables = Arrays.asList(
            new OrderTableIdRequest(1L),
            new OrderTableIdRequest(2L));
        TableGroupRequest request = new TableGroupRequest(requestTables);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableGroupService.create(request))
            .withMessage("사용중인 테이블이 있습니다.");
    }

    @DisplayName("테이블 그룹 삭제")
    @Test
    void ungroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, true),
            new OrderTable(2L, null, 2, true));
        테이블_그룹_ID로_조회_결과_반환(orderTables);

        요리_또는_식사중인_테이블_존재_여부_반환(false);

        // when
        tableGroupService.ungroup(1L);
    }

    @DisplayName("요리 중이나 식사 중인 주문 테이블을 포함하고 있다면 삭제 불가능")
    @Test
    void unGroupFailWhenContainsMealOrCooking() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
            new OrderTable(1L, null, 4, true),
            new OrderTable(2L, null, 2, true));
        테이블_그룹_ID로_조회_결과_반환(orderTables);

        요리_또는_식사중인_테이블_존재_여부_반환(true);

        // when and then
        assertThatExceptionOfType(KitchenposException.class)
            .isThrownBy(() -> tableGroupService.ungroup(1L))
            .withMessage("사용중인 테이블이 있습니다.");
    }

    private void 테이블_그룹_저장_결과_반환() {
        Mockito.when(tableGroupDao.save(Mockito.any()))
            .thenReturn(tableGroup);
    }

    private void 테이블_조회_결과_반환(List<OrderTable> orderTables) {
        Mockito.when(orderTableDao.findAllByIdIn(Mockito.anyList()))
            .thenReturn(orderTables);
    }

    private void 테이블_그룹_ID로_조회_결과_반환(List<OrderTable> orderTables) {
        Mockito.when(orderTableDao.findAllByTableGroup_Id(Mockito.anyLong()))
            .thenReturn(orderTables);
    }

    private void 요리_또는_식사중인_테이블_존재_여부_반환(boolean b) {
        Mockito.when(orderDao.existsByOrderTable_IdInAndOrderStatusIn(Mockito.anyList(), Mockito.anyList()))
            .thenReturn(b);
    }
}