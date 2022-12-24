package kitchenpos.application;

import kitchenpos.common.exception.NoSuchDataException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.order.domain.OrderFixture.주문;
import static kitchenpos.domain.OrderTableFixture.주문테이블;
import static kitchenpos.domain.TableGroupFixture.테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@DisplayName("테이블 그룹 테스트")
public class TableGroupServiceTest {

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderTableRepository orderTableRepository;
    @MockBean
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    private TableGroupService tableGroupService;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;

    private TableGroup 테이블그룹;

    @BeforeEach
    void setup() {
        테이블1 = 주문테이블(1L, null, 0, true);
        테이블2 = 주문테이블(2L, null, 0, true);
        테이블3 = 주문테이블(3L, null, 0, true);

        tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository, publisher);
    }

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void 테이블_그룹_생성() {
        // given
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(테이블1));
        given(orderTableRepository.findById(2L)).willReturn(Optional.ofNullable(테이블2));
        given(orderTableRepository.findById(3L)).willReturn(Optional.ofNullable(테이블3));

        테이블그룹 = 테이블그룹(1L);
        given(tableGroupRepository.save(any())).willReturn(테이블그룹);

        // when
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId(), 테이블3.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(tableGroupResponse.getOrderTableResponses()).hasSize(3)
        );


    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void 테이블_그룹_해제() {
        // given
        테이블그룹 = 테이블그룹(1L);

        Order 주문1 = 주문(1L, OrderStatus.COMPLETION.name(), 테이블1.getId());
        Order 주문2 = 주문(2L, OrderStatus.COMPLETION.name(), 테이블2.getId());
        Order 주문3 = 주문(3L, OrderStatus.COMPLETION.name(), 테이블3.getId());

        given(tableGroupRepository.findById(테이블그룹.getId())).willReturn(Optional.ofNullable(테이블그룹));
        given(orderTableRepository.findOrderTablesByTableGroupId(테이블그룹.getId())).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));
//        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(테이블1));
//        given(orderTableRepository.findById(2L)).willReturn(Optional.ofNullable(테이블2));
//        given(orderTableRepository.findById(3L)).willReturn(Optional.ofNullable(테이블3));
        given(orderRepository.findOrderByOrderTableId(테이블1.getId())).willReturn(Arrays.asList(주문1));
        given(orderRepository.findOrderByOrderTableId(테이블2.getId())).willReturn(Arrays.asList(주문2));
        given(orderRepository.findOrderByOrderTableId(테이블3.getId())).willReturn(Arrays.asList(주문3));


        // when
        tableGroupService.ungroup(테이블그룹.getId());

        // then
        assertAll(
                () -> assertThat(테이블1.getTableGroup()).isNull(),
                () -> assertThat(테이블2.getTableGroup()).isNull(),
                () -> assertThat(테이블3.getTableGroup()).isNull()
        );
    }

    @DisplayName("단일 테이블로 테이블 그룹을 생성한다")
    @Test
    void 단일_테이블로_테이블_그룹_생성() {
        // when & then
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(테이블1));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId()));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroupRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 테이블로 테이블 그룹을 생성한다")
    @Test
    void 존재하지_않은_테이블로_테이블_그룹_생성() {
        // given
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(테이블1));
        given(orderTableRepository.findById(2L)).willReturn(Optional.ofNullable(null));

        // when & then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId()));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroupRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("비어있지 않은 테이블로 테이블 그룹을 생성한다")
    @Test
    void 비어_있지_않은_테이블로_테이블_그룹_생성() {
        // given
        OrderTable 테이블4 = 주문테이블(4L, null, 5, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블4));

        // when & then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블4.getId()));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroupRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태가 완료가 아닌 주문이 있는 테이블이 포함된 테이블 그룹을 해제한다")
    @Test
    void 주문상태_완료가_아닌_주문이_있는_테이블의_테이블그룹_해제() {
        // given
        테이블그룹 = 테이블그룹(1L);

        Order 주문1 = 주문(1L, OrderStatus.COMPLETION.name(), 테이블1.getId());
        Order 주문2 = 주문(2L, OrderStatus.COOKING.name(), 테이블2.getId());

        given(tableGroupRepository.findById(테이블그룹.getId())).willReturn(Optional.ofNullable(테이블그룹));
        given(orderTableRepository.findOrderTablesByTableGroupId(테이블그룹.getId())).willReturn(Arrays.asList(테이블1, 테이블2));
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(테이블1));
        given(orderTableRepository.findById(2L)).willReturn(Optional.ofNullable(테이블2));
//        given(orderRepository.findById(주문1.getId())).willReturn(Optional.of(주문1));
//        given(orderRepository.findById(주문2.getId())).willReturn(Optional.of(주문2));


        // when & then
        assertThatThrownBy(
                () -> tableGroupService.ungroup(테이블그룹.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}