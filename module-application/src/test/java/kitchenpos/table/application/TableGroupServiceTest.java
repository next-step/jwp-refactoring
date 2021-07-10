package kitchenpos.table.application;

import static kitchenpos.util.TestDataSet.테이블_1번;
import static kitchenpos.util.TestDataSet.테이블_2번;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.constant.OrderStatus;
import kitchenpos.order.Order;
import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.OrderTables;
import kitchenpos.table.TableGroup;
import kitchenpos.table.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroupRequest request;

    @BeforeEach
    void setUp() {
        request = new TableGroupRequest(
            Arrays.asList(new OrderTableRequest(테이블_1번.getId()), new OrderTableRequest(테이블_2번.getId())));
    }

    @Test
    @DisplayName("테이블 그룹 정상 생성 케이스")
    void create() {
        // given
        TableGroup 산악회 = new TableGroup(1L, LocalDateTime.now(),
            new OrderTables(Arrays.asList(new OrderTable(1L, 4, true), new OrderTable(2L, 3, true))));
        given(orderTableRepository.findAllById(any())).willReturn(산악회.getOrderTables());
        given(tableGroupRepository.save(any())).willReturn(산악회);

        // when
        TableGroupResponse result = tableGroupService.create(request);

        // then
        assertThat(result.getId()).isEqualTo(산악회.getId());
        assertThat(result.getOrderTables().get(0).getId()).isEqualTo(테이블_1번.getId());
        assertThat(result.getOrderTables().get(1).getId()).isEqualTo(테이블_2번.getId());

        verify(orderTableRepository, times(1)).findAllById(any());
        verify(tableGroupRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("주문_테이블이 2개 미만일 경우 실패한다.")
    void underTwo() {
        // when
        TableGroupRequest 테이블이_1개 = new TableGroupRequest(
            Arrays.asList(new OrderTableRequest(테이블_1번.getId())));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(테이블이_1개);
        });
    }

    @Test
    @DisplayName("주문_테이블이 존재하지 않는 경우 실패한다.")
    void noTable() {
        // when
        given(orderTableRepository.findAllById(any())).willReturn(Collections.emptyList());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(request);
        });

        verify(orderTableRepository, times(1)).findAllById(any());
    }

    @Test
    @DisplayName("중복된 주문이 존재할 시 실패한다.")
    void overlapTable() {
        // when
        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(테이블_1번));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(request);
        });

        verify(orderTableRepository, times(1)).findAllById(any());
    }

    @Test
    @DisplayName("주문_테이블이 다른 그룹에 속해있을 경우 실패한다.")
    void aleadyTable() {
        // when
        //OrderTable already = new OrderTable(1L, 10, true);
        OrderTable already = new OrderTable(1L, 1L, 10, true, Collections.emptyList());
        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(already, new OrderTable(2L, 10, true)));
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(request);
        });
    }

    @Test
    @DisplayName("테이블이 비지 않았을 경우 실패한다.")
    void noEmptyTable() {
        // when
        given(orderTableRepository.findAllById(any()))
            .willReturn(Arrays.asList(new OrderTable(1L, 10, false), new OrderTable(2L, 10, false)));
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.create(request);
        });

        verify(orderTableRepository, times(1)).findAllById(any());
    }

    @ParameterizedTest
    @MethodSource("cookingSet")
    @DisplayName("테이블 그룹에서 개별 테이블의 주문상태가 조리중이거나 식사중이면 테이블 그룹 제거 실패한다.")
    void notAbliableRemove(TableGroup tableGroup) {
        // given
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(tableGroup));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            tableGroupService.ungroup(tableGroup.getId());
        });

        verify(tableGroupRepository, times(1)).findById(any());
    }

    private static Stream<Arguments> cookingSet() {
        Order isCooking = new Order(1L, OrderStatus.COOKING, 1L, null);
        OrderTables orderTablesCooking = new OrderTables(
            Arrays.asList(
                new OrderTable(1L, 1L, 10, false, Arrays.asList(isCooking))));
        TableGroup isCookingGroup = new TableGroup(1L, LocalDateTime.now(), orderTablesCooking);

        Order isMeal = new Order(1L, OrderStatus.MEAL, 1L, null);
        OrderTables orderTablesMeal = new OrderTables(
            Arrays
                .asList(new OrderTable(1L, 1L, 10, false, Arrays.asList(isMeal))));
        TableGroup isMealGroup = new TableGroup(1L, LocalDateTime.now(), orderTablesMeal);

        return Stream.of(Arguments.of(isCookingGroup), Arguments.of(isMealGroup));
    }

}
