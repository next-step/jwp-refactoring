package kitchenpos.tableGroup.application;

import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tableGroup.domain.TableGroupRepository;
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

        단체_지정 = new TableGroup();
        단체_지정.setId(1L);
        단체_지정.setOrderTables(Arrays.asList(테이블1번, 테이블2번));
    }

    @DisplayName("단체 지정을 주문 테이블 목록 으로 등록 할 수 있다.")
    @Test
    void create() {
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(테이블1번, 테이블2번));
        given(tableGroupRepository.save(any())).willReturn(단체_지정);
        given(orderTableRepository.save(any())).willReturn(테이블1번);
        given(orderTableRepository.save(any())).willReturn(테이블2번);

        TableGroup createTableGroup = tableGroupService.create(단체_지정);

        assertAll(
                () -> assertThat(createTableGroup).isNotNull(),
                () -> assertThat(테이블1번.isEmpty()).isFalse(),
                () -> assertThat(테이블2번.isEmpty()).isFalse()
        );
    }

    @DisplayName("단체 지정 할 때 주문 테이블이 2개 이상이여야 한다.")
    @Test
    void createOrderTableSizeError() {
        OrderTable 테이블 = OrderTableFixture.생성(0,true);

        TableGroup 단체_지정_주문_테이블이_1개 = new TableGroup();
        단체_지정_주문_테이블이_1개.setId(2L);
        단체_지정_주문_테이블이_1개.setOrderTables(Arrays.asList(테이블));

        assertThatThrownBy(
                () -> tableGroupService.create(단체_지정_주문_테이블이_1개)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 할때 주문 테이블은 빈 테이블이어야한다.")
    @Test
    void shouldBeEmptyTable() {
        OrderTable 손님이_채워진_테이블3번 = OrderTableFixture.생성(0,false);
        OrderTable 손님이_채워진_테이블4번 = OrderTableFixture.생성(0,false);
        TableGroup 단체_지정 = new TableGroup();
        단체_지정.setId(2L);
        단체_지정.setOrderTables(Arrays.asList(손님이_채워진_테이블3번, 손님이_채워진_테이블4번));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(손님이_채워진_테이블3번, 손님이_채워진_테이블4번));

        assertThatThrownBy(
                () -> tableGroupService.create(단체_지정)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해체 할 수 있다.")
    @Test
    void ungroup() {
        OrderTable 테이블3번 = OrderTableFixture.생성(0,true);
        OrderTable 테이블4번 = OrderTableFixture.생성(0,true);
        TableGroup 단체_지정 = new TableGroup();
        단체_지정.setId(2L);
        단체_지정.setOrderTables(Arrays.asList(테이블3번, 테이블4번));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(테이블3번, 테이블4번));
        given(tableGroupRepository.save(단체_지정)).willReturn(단체_지정);
        given(orderTableRepository.save(any())).willReturn(테이블3번);
        given(orderTableRepository.save(any())).willReturn(테이블4번);
        tableGroupService.create(단체_지정);
        given(orderTableRepository.findAllByTableGroupId(단체_지정.getId())).willReturn(Arrays.asList(테이블3번, 테이블4번));
        given(orderTableRepository.save(any())).willReturn(테이블3번);
        given(orderTableRepository.save(any())).willReturn(테이블4번);

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
        단체_지정.setOrderTables(Arrays.asList(테이블3번, 테이블4번));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(테이블3번, 테이블4번));
        given(tableGroupRepository.save(단체_지정)).willReturn(단체_지정);
        given(orderTableRepository.save(any())).willReturn(테이블3번);
        given(orderTableRepository.save(any())).willReturn(테이블4번);
        tableGroupService.create(단체_지정);
        given(orderTableRepository.findAllByTableGroupId(단체_지정.getId())).willReturn(Arrays.asList(테이블3번, 테이블4번));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(단체_지정.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

}
