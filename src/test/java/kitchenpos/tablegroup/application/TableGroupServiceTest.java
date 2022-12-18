package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.table.domain.OrderTableTest.두_명의_방문객이_존재하는_테이블;
import static kitchenpos.tablegroup.dto.CreateTableGroupRequestTest.단체_지정_요청;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@DisplayName("단체 지정 테스트")
public class TableGroupServiceTest {

    private static final OrderStatus 단체_지정_해제가_불가능한_조리_상태 = OrderStatus.COOKING;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        assertThatNoException().isThrownBy(() -> tableGroupService.create(
                단체_지정_요청(Arrays.asList(
                        tableService.create(두_명의_방문객이_존재하는_테이블()).getId(),
                        tableService.create(두_명의_방문객이_존재하는_테이블()).getId()))));
    }

    @DisplayName("생성 예외 - 주문 테이블 목록이 비어있는 경우")
    @Test
    void 생성_예외_주문_테이블_목록이_비어_있는_경우() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(
                        단체_지정_요청(Collections.emptyList())));
    }

    @DisplayName("생성 예외 - 주문 테이블이 한개 이하인 경우")
    @Test
    void 생성_예외_주문_테이블이_한개_이하인_경우() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체_지정_요청(Arrays.asList(
                        tableService.create(두_명의_방문객이_존재하는_테이블()).getId()))));
    }

    @DisplayName("생성 예외 - 주문 테이블의 수가 실제 저장된 주문 테이블의 수와 일치하지 않는 경우")
    @Test
    void 생성_예외_주문_테이블의_수가_실제_저장된_주문_테이블_수와_일치하지_않는_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(
                단체_지정_요청(Arrays.asList(
                        tableService.create(두_명의_방문객이_존재하는_테이블()).getId(),
                        두_명의_방문객이_존재하는_테이블().getId()))));
    }

    @Transactional
    @DisplayName("단체 지정 해제 성공")
    @Test
    void 단체_지정_해제_성공() {
        //given:
        final TableGroup 단체_지정_테이블 = tableGroupService.create(
                단체_지정_요청(Arrays.asList(
                        tableService.create(두_명의_방문객이_존재하는_테이블()).getId(),
                        tableService.create(두_명의_방문객이_존재하는_테이블()).getId())));
        //when,then:
        assertThatNoException().isThrownBy(() -> tableGroupService.ungroup(단체_지정_테이블.getId()));
    }
}
