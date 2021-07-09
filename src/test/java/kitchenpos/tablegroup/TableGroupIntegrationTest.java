package kitchenpos.tablegroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableGroupIntegrationTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable 비지않은_주문_테이블_1;
    private OrderTable 비지않은_주문_테이블_2;
    private TableGroup 테이블_그룹_1;

    @BeforeEach
    void setUp() {
        비지않은_주문_테이블_1 = orderTableRepository.save(new OrderTable(3, false));
        비지않은_주문_테이블_2 = orderTableRepository.save(new OrderTable(10, false));

        OrderTable 그룹핑된_주문_테이블_1 = orderTableRepository.save(new OrderTable(10, false));
        TableGroup tableGroup = new TableGroup(Arrays.asList(그룹핑된_주문_테이블_1));
        테이블_그룹_1 = tableGroupRepository.save(tableGroup);
    }


    @DisplayName("주문 테이블 그룹 생성 통합 테스트")
    @Test
    void createTest() {
        // given
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(비지않은_주문_테이블_1.getId()),
                                                                        new OrderTableIdRequest(비지않은_주문_테이블_2.getId())));

        // when
        TableGroupResponse actual = tableGroupService.create(request);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getOrderTables()
                         .stream()
                         .map(OrderTableResponse::getTableGroupId)
                         .collect(Collectors.toList()))
            .isNotEmpty()
            .doesNotContainNull();
    }

    @DisplayName("주문 테이블 그룹 해제 통합 테스트")
    @Test
    void ungroupTest() {
        // when
        assertThatCode(() -> tableGroupService.ungroup(테이블_그룹_1.getId()))
            .doesNotThrowAnyException();
    }

}
