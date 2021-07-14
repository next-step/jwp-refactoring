package kitchenpos.order.application;

import kitchenpos.util.TestSupport;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단체지정 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class TableGroupServiceTest extends TestSupport {
    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    public void setUp() {
        orderTable1 = 테이블_등록되어있음(5, true);
        orderTable2 = 테이블_등록되어있음(5, true);
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
}
