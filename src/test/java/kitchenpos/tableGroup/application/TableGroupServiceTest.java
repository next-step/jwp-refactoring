package kitchenpos.tableGroup.application;

import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable 테이블1번;
    private OrderTable 테이블2번;
    private TableGroup 단체_지정;


    @BeforeEach
    void setUp() {
        테이블1번 = OrderTableFixture.생성(0,true);
        테이블2번 = OrderTableFixture.생성(0,true);

        단체_지정 = new TableGroup(Arrays.asList(테이블1번, 테이블2번));
    }

    @DisplayName("단체 지정을 주문 테이블 목록 으로 등록 할 수 있다.")
    @Test
    void create() {
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(테이블1번, 테이블2번));
        given(tableGroupRepository.save(any())).willReturn(단체_지정);

        TableGroup createTableGroup = tableGroupService.create(TableGroupFixture.샘플_Request());

        assertAll(
                () -> assertThat(createTableGroup).isNotNull(),
                () -> assertThat(테이블1번.isEmpty()).isFalse(),
                () -> assertThat(테이블2번.isEmpty()).isFalse()
        );
    }

    @DisplayName("단체 지정 할 때 주문 테이블이 2개 이상이여야 한다.")
    @Test
    void createOrderTableSizeError() {
        OrderTableIdRequest 테이블1 = new OrderTableIdRequest(1L);
        TableGroupRequest 단체_지정_주문테이블1개_Request = TableGroupFixture.생성_Request(Arrays.asList(테이블1));
        assertThatThrownBy(
                () -> tableGroupService.create(단체_지정_주문테이블1개_Request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 할때 주문 테이블은 빈 테이블이어야한다.")
    @Test
    void shouldBeEmptyTable() {
        OrderTableIdRequest 테이블1 = new OrderTableIdRequest(1L);
        OrderTableIdRequest 테이블2 = new OrderTableIdRequest(2L);
        TableGroupRequest 단체_지정_Request = TableGroupFixture.생성_Request(Arrays.asList(테이블1, 테이블2));

        assertThatThrownBy(
                () -> tableGroupService.create(단체_지정_Request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해체 할 수 있다.")
    @Test
    void ungroup() {
        OrderTable 테이블3번 = OrderTableFixture.생성(0,true);
        OrderTable 테이블4번 = OrderTableFixture.생성(0,true);
        TableGroup 단체_지정 = new TableGroup();
        단체_지정.setId(2L);
        단체_지정.addOrderTables(Arrays.asList(테이블3번, 테이블4번));
        given(tableGroupRepository.findById(any())).willReturn(java.util.Optional.of(단체_지정));
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(any(),any())).willReturn(false);

        tableGroupService.ungroup(단체_지정.getId());

        assertAll(
                () -> assertThat(테이블3번.getTableGroup()).isEqualTo(null),
                () -> assertThat(테이블4번.getTableGroup()).isEqualTo(null)
        );
    }

    @DisplayName("단체지정 해제는 주문 테이블 계산 완료일때만 가능하다.")
    @Test
    void shouldBeCompletionStatus() {
        OrderTable 테이블3번 = OrderTableFixture.생성(0,true);
        OrderTable 테이블4번 = OrderTableFixture.생성(0,true);
        TableGroup 단체_지정 = new TableGroup();
        단체_지정.setId(2L);
        단체_지정.addOrderTables(Arrays.asList(테이블3번, 테이블4번));

        given(tableGroupRepository.findById(any())).willReturn(java.util.Optional.of(단체_지정));
        given(orderRepository.existsByOrderTableInAndOrderStatusIn(any(),any())).willReturn(true);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체_지정.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
