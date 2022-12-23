package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableFactory;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupFactory;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    TableGroupRepository tableGroupRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    private OrderTable 빈테이블일번;
    private OrderTable 빈테이블이번;
    private OrderTable 한명테이블;
    private TableGroup 테이블그룹;


    @BeforeEach
    public void setUp() {
        빈테이블일번 = OrderTableFactory.create(1L, null, 0, true);
        빈테이블이번 = OrderTableFactory.create(2L, null, 0, true);
        한명테이블 = OrderTableFactory.create(3L, null, 1, false);
        테이블그룹 = TableGroupFactory.create(1L, Arrays.asList(빈테이블일번, 빈테이블이번));
    }

    @DisplayName("이미 단체가 지정된 테이블에 단체 지정하면 에러가 발생한다.")
    @Test
    void create() {
        //given
        OrderTableIdRequest 테이블일번 = new OrderTableIdRequest(1L);
        OrderTableIdRequest 테이블이번 = new OrderTableIdRequest(2L);
        TableGroupRequest 단체지정요청 = new TableGroupRequest(Arrays.asList(테이블일번, 테이블이번));
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(빈테이블일번));
        given(orderTableRepository.findById(2L)).willReturn(Optional.ofNullable(빈테이블이번));
        //when & then
        Assertions.assertThatThrownBy(()-> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블 2개 이상부터 단체 지정할 수 있다.")
    @Test
    void createTwo() {
        //given
        OrderTableIdRequest 테이블일번 = new OrderTableIdRequest(1L);
        TableGroupRequest 단체지정요청 = new TableGroupRequest(Collections.singletonList(테이블일번));
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(빈테이블일번));
        //when & then
        Assertions.assertThatThrownBy(()-> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블만 단체 지정이 가능하다.")
    @Test
    void createEmpty() {
        //given
        OrderTableIdRequest 테이블일번 = new OrderTableIdRequest(1L);
        TableGroupRequest 단체지정요청 = new TableGroupRequest(Collections.singletonList(테이블일번));
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());
        //when & then
        Assertions.assertThatThrownBy(()-> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈테이블이 아니면 단체지정이 불가능하다.")
    @Test
    void createRegistered() {
        //given
        OrderTableIdRequest 테이블일번 = new OrderTableIdRequest(3L);
        TableGroupRequest 단체지정요청 = new TableGroupRequest(Collections.singletonList(테이블일번));
        given(orderTableRepository.findById(3L)).willReturn(Optional.ofNullable(한명테이블));
        //when & then
        assertThatThrownBy(() -> tableGroupService.create(단체지정요청))
                .isInstanceOf(IllegalArgumentException.class);
    }


}
