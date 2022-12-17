package kitchenpos.table.application;

import kitchenpos.order.constant.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.OrderTableServiceTest.generateOrderTable;
import static kitchenpos.application.OrderTableServiceTest.generateOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체지정")
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    private OrderTable 메뉴테이블1;
    private OrderTable 메뉴테이블2;
    private OrderTable 메뉴테이블3;
    private OrderTable 메뉴테이블4;
    private OrderTable 메뉴테이블5;

    private OrderTableRequest 메뉴테이블요청1;
    private OrderTableRequest 메뉴테이블요청2;
    private OrderTableRequest 메뉴테이블요청3;
    private OrderTableRequest 메뉴테이블요청4;

    private List<OrderTableRequest> 메뉴테이블요청들;

    List<Long> 메뉴테이블_아이디들;

    private TableGroup 단체지정;

    private TableGroupRequest 단체지정요청;


    @BeforeEach
    void setUp() {
        단체지정 = generateTableGroup();

        메뉴테이블1 = generateOrderTable(단체지정, 5, true);
        메뉴테이블2 = generateOrderTable(단체지정, 3, true);
        메뉴테이블3 = generateOrderTable(단체지정, 1, true);
        메뉴테이블4 = generateOrderTable(단체지정, 3, true);
        메뉴테이블5 = generateOrderTable(단체지정, 3, false);

        메뉴테이블요청1 = generateOrderTableRequest(메뉴테이블1.getId());
        메뉴테이블요청2 = generateOrderTableRequest(메뉴테이블2.getId());
        메뉴테이블요청3 = generateOrderTableRequest(메뉴테이블3.getId());
        메뉴테이블요청4 = generateOrderTableRequest(메뉴테이블4.getId());

        메뉴테이블요청들 = Arrays.asList(메뉴테이블요청1, 메뉴테이블요청2, 메뉴테이블요청3, 메뉴테이블요청4);

        메뉴테이블_아이디들 = Arrays.asList(1L, 2L);

        단체지정요청 = generateTableGroupRequest(메뉴테이블요청들);
    }

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void tableGroupTest1() {
        given(orderTableRepository.findAllByIdIn(any(List.class))).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블2, 메뉴테이블3, 메뉴테이블4));
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체지정);

        TableGroupRequest 추가할_단체지정 = generateTableGroupRequest(메뉴테이블요청들);
        TableGroupResponse 추가된_단체지정 = tableGroupService.create(추가할_단체지정);

        assertThat(추가된_단체지정.getId()).isEqualTo(단체지정.getId());
    }

    @Test
    @DisplayName("단체 지정 : 주문 테이블은 필수값이며, 2개 미만이어선 안된다.")
    void tableGroupTest2() {
        given(orderTableRepository.findAllByIdIn(any(List.class))).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블2));

        TableGroupRequest 주문테이블이_NULL인_단체지정 = generateTableGroupRequest(null);
        TableGroupRequest 주문테이블이_2개미만인_단체지정 = generateTableGroupRequest(Arrays.asList(메뉴테이블요청1));

        assertThatThrownBy(() -> tableGroupService.create(주문테이블이_NULL인_단체지정))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tableGroupService.create(주문테이블이_2개미만인_단체지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 : 주문 테이블은 존재하는 주문 테이블들로만 구성되야 한다.")
    void tableGroupTest3() {
        given(orderTableRepository.findAllByIdIn(any(List.class))).willReturn(Arrays.asList(메뉴테이블1));

        assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 : 다른 테이블 그룹에 속한 주문 테이블로는 요청할 수 없다.")
    void tableGroupTest4() {
        given(orderTableRepository.findAllByIdIn(any(List.class))).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블3));

        assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정 : 주문 테이블이 비어있지 않으면 요청할 수 없다.")
    void tableGroupTest5() {
        given(orderTableRepository.findAllByIdIn(any(List.class))).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블4));

        assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 제거할 수 있다.")
    void tableGroupTest6() {
        given(tableGroupRepository.findById(단체지정.getId())).willReturn(Optional.of(단체지정));
        given(orderTableRepository.findAllByTableGroup(단체지정)).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(메뉴테이블1.getId(), 메뉴테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableRepository.save(메뉴테이블1)).willReturn(메뉴테이블1);
        given(orderTableRepository.save(메뉴테이블2)).willReturn(메뉴테이블2);

        tableGroupService.ungroup(단체지정.getId());
    }

    @Test
    @DisplayName("단체 지정 제거 : 주문 테이블의 상태가 조리중이거나 식사중이면 안된다.")
    void tableGroupTest7() {
        given(tableGroupRepository.findById(단체지정.getId())).willReturn(Optional.of(단체지정));
        given(orderTableRepository.findAllByTableGroup(단체지정)).willReturn(Arrays.asList(메뉴테이블1, 메뉴테이블2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(메뉴테이블1.getId(), 메뉴테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(단체지정.getId()))
                .isInstanceOf(IllegalArgumentException.class);

    }

    public static TableGroup generateTableGroup() {
        return new TableGroup(LocalDateTime.now());
    }

    private TableGroupRequest generateTableGroupRequest(List<OrderTableRequest> orderTables) {
        return new TableGroupRequest(orderTables);
    }

}
