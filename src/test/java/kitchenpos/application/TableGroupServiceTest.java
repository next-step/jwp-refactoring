package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 그룹 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderService orderService;
    @Mock
    private TableService tableService;
    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 테이블_그룹;
    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;
    private OrderTableRequest 주문_테이블_요청1;
    private OrderTableRequest 주문_테이블_요청2;
    private List<OrderTableRequest> 주문_테이블_요청_목록;
    private List<OrderTable> 주문_테이블_목록;
    private TableGroupRequest 테이블_그룹_요청;

    @BeforeEach
    void setUp() {
        주문_테이블1 = OrderTable.of(0, false);
        주문_테이블2 = OrderTable.of(0, false);
        주문_테이블_목록 = Lists.newArrayList(주문_테이블1, 주문_테이블2);
        테이블_그룹 = TableGroup.of(1L, 주문_테이블_목록);

        주문_테이블_요청1 = OrderTableRequest.of(0, false);
        주문_테이블_요청2 = OrderTableRequest.of(0, false);
        주문_테이블_요청_목록 = Lists.newArrayList(주문_테이블_요청1, 주문_테이블_요청2);
        테이블_그룹_요청 = TableGroupRequest.from(Lists.newArrayList(1L, 2L));
    }

    @DisplayName("주문 테이블 단체 지정을 등록한다.")
    @Test
    void create() {
        given(tableService.findAllById(anyList())).willReturn(주문_테이블_목록);
        given(tableGroupRepository.save(any())).willReturn(테이블_그룹);
        final TableGroupResponse actual = tableGroupService.create(테이블_그룹_요청);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTableRespons()).hasSize(2)
        );
    }

    @DisplayName("주문 테이블 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(테이블_그룹));

        tableGroupService.ungroup(1L);

        assertAll(
                () -> assertThat(주문_테이블1.getTableGroup()).isNull(),
                () -> assertThat(주문_테이블2.getTableGroup()).isNull()
        );
    }
}
