package kitchenpos.application.integration;

import kitchenpos.application.OrderService;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.*;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단체지정 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderService orderService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    public void setUp() {
        orderTable1 = new OrderTable(5, true);
        orderTable2 = new OrderTable(5, true);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 2개 미만인 경우")
    @Test
    public void 주문테이블이2개미만인경우_단체지정_등록_예외() throws Exception {
        //when
        //then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId()));
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 입력한 주문테이블의 수와 실제 저장되었던 주문테이블 수가 다른 경우")
    @Test
    public void 입력한주문테이블수와저장된주문테이블수가다른경우_단체지정_등록_예외() throws Exception {
        //when
        //then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(100L, 200L));
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 빈테이블이 아닌경우")
    @Test
    public void 주문테이블이빈테이블이아닌경우_단체지정_등록_예외() throws Exception {
        //given
        orderTable1.changeEmpty(false);

        //when
        //then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 단체지정이 이미 되어있는 경우")
    @Test
    public void 주문테이블이이미단체지정이되어있는경우_단체지정_등록_예외() throws Exception {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        tableGroupService.create(tableGroupRequest);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록")
    @Test
    public void 단체지정_등록_확인() throws Exception {
        //when
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(tableGroupResponse.getId()).isNotNull();
        assertThat(tableGroupResponse.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("단체지정 해제 예외 - 주문상태가 조리나 식사인 경우")
    @Test
    public void 주문상태가조리나식사인경우_단체지정_해제_확인() throws Exception {
        //give
        Menu menu = menuRepository.save(new Menu("메뉴", BigDecimal.valueOf(1000), null));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        orderTable1.changeEmpty(false);
        OrderRequest orderRequest = new OrderRequest(orderTable1.getId(), Arrays.asList(orderLineItemRequest));
        OrderResponse orderResponse = orderService.create(orderRequest);
        orderService.changeOrderStatus(orderResponse.getId(), new OrderStatusRequest(OrderStatus.MEAL));

        orderTable1.changeEmpty(true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupResponse.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 해제")
    @Test
    public void 단체지정_해제_확인() throws Exception {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        //when
        tableGroupService.ungroup(tableGroupResponse.getId());

        //then
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }
}
