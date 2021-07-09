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
    private Menu menu;

    @BeforeEach
    public void setUp() {
        orderTable1 = new OrderTable(5, true);
        orderTable2 = new OrderTable(5, true);
        orderTableRepository.save(orderTable1);
        orderTableRepository.save(orderTable2);
        menu = new Menu("메뉴", BigDecimal.valueOf(1_000), null);
        menuRepository.save(menu);
    }

    @DisplayName("단체지정 등록 예외 - 입력한 주문테이블의 수와 실제 저장되었던 주문테이블 수가 다른 경우")
    @Test
    public void 입력한주문테이블수와저장된주문테이블수가다른경우_단체지정_등록_예외() throws Exception {
        //when
        //then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(100L, 200L));
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .hasMessage("요청한 단체지정의 주문테이블 수와 디비의 주문테이블 수가 불일치합니다.")
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

    @DisplayName("단체지정 해제 예외 - 단체지정이 존재하지 않는 경우")
    @Test
    public void 단체지정이존재하지않는경우_단체지정_해제_확인() throws Exception {
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(-1L))
                .hasMessage("단체지정이 존재하지 않습니다.")
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
