package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@DisplayName("애플리케이션 테스트 보호 - 단체 지정 서비스")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    private Order 주문;
    private OrderLineItem 주문_항목;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private List<OrderLineItem> orderLineItems;
    private TableGroup 단체;
    private List<Long> orderTableIds;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @BeforeEach
    public void setup() {
        주문테이블1 = new OrderTable();
        주문테이블1.setId(1L);
        주문테이블1.setNumberOfGuests(0);
        주문테이블1.setEmpty(true);

        주문테이블2 = new OrderTable();
        주문테이블2.setId(2L);
        주문테이블2.setNumberOfGuests(0);
        주문테이블2.setEmpty(true);

        주문_항목 = new OrderLineItem();
        주문_항목.setSeq(1L);
        주문_항목.setQuantity(1);

        주문 = new Order();
        주문.setId(1L);
        주문.setOrderTableId(주문테이블1.getId());

        orderLineItems = new ArrayList<>();
        orderLineItems.add(주문_항목);
        주문.setOrderLineItems(orderLineItems);

        단체 = new TableGroup();
        단체.setId(1L);
        List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(주문테이블1, 주문테이블2));
        단체.setOrderTables(orderTables);

        orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    @DisplayName("단체 지정 생성")
    @Test
    void create() {

        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(단체.getOrderTables());
        given(tableGroupDao.save(단체)).willReturn(단체);
        given(orderTableDao.save(주문테이블1)).willReturn(주문테이블1);
        given(orderTableDao.save(주문테이블2)).willReturn(주문테이블2);

        TableGroup savedTableGroup = tableGroupService.create(단체);

        assertThat(savedTableGroup).isEqualTo(단체);

    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블 목록이 비어있음")
    @Test
    void createThrowExceptionWhenOrderTablesIsEmpty() {
        단체.setOrderTables(null);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체));
    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블 목록 갯수가 2보다 적음")
    @Test
    void createThrowExceptionWhenOrderTablesSizeLessThen2() {
        단체.getOrderTables().remove(단체.getOrderTables().size()-1);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체));
    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블이 저장된 데이터가 아님")
    @Test
    void createThrowExceptionWhenOrderTablesNotExistsInStorage() {
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(new ArrayList<>());
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체));
    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블 중에 빈 테이블이 아닌 것이 있음")
    @Test
    void createThrowExceptionWhenHasNotEmptyOrderTable() {
        주문테이블1.setEmpty(false);
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(단체.getOrderTables());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체));
    }

    @DisplayName("단체 지정 생성 예외: 주문 테이블 중에 이미 단체지정된 것이 있음")
    @Test
    void createThrowExceptionWhenHasAlreadyTableGroupOrderTable() {
        주문테이블1.setTableGroupId(2L);
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(단체.getOrderTables());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(단체));
    }

    @DisplayName("주문 테이블의 단체 지정 삭제")
    @Test
    void ungroup() {
        주문테이블1.setTableGroupId(단체.getId());
        주문테이블2.setTableGroupId(단체.getId());

        given(orderTableDao.findAllByTableGroupId(단체.getId())).willReturn(단체.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(주문테이블1)).willReturn(주문테이블1);
        given(orderTableDao.save(주문테이블2)).willReturn(주문테이블2);

        tableGroupService.ungroup(단체.getId());

        assertThat(주문테이블1.getTableGroupId()).isNull();
        assertThat(주문테이블2.getTableGroupId()).isNull();

    }

}
