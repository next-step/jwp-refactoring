package kitchenpos.tablegroup.validator;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.common.Messages.*;
import static kitchenpos.table.fixture.TableFixture.*;
import static kitchenpos.tablegroup.fixture.TableGroupFixture.단체_주문_테이블_그룹;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @InjectMocks
    private TableGroupValidator tableGroupValidator;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("테이블 그룹 등록시 유효성 검사가 정상적으로 성공된다.")
    void validateCreate() {
        // given
        TableGroupRequest 테이블_개수_2개 = TableGroupRequest.of(
                Arrays.asList(
                        OrderTableIdRequest.of(단체_주문_테이블_4명.getId()),
                        OrderTableIdRequest.of(단체_주문_테이블_6명.getId()))
        );

        List<Long> 테이블_ID_2개 = Arrays.asList(단체_주문_테이블_4명.getId(), 단체_주문_테이블_6명.getId());

        // when
        when(orderTableRepository.findByIdIn(anyList())).thenReturn(Arrays.asList(단체_주문_테이블_4명, 단체_주문_테이블_6명));

        // then
        Assertions.assertAll(
                () -> assertDoesNotThrow(() -> tableGroupValidator.validateCreateRequestTableGroup(테이블_개수_2개)),
                () -> assertDoesNotThrow(() -> tableGroupValidator.validateCreateOrderTableGroup(테이블_ID_2개))
        );
    }

    @Test
    @DisplayName("테이블 그룹 등록시 메뉴가 1개일 경우 테이블 그룹 등록이 실패 된다")
    void validateRequestTableGroup() {
        // given
        TableGroupRequest 테이블_개수_1개 = TableGroupRequest.of(Arrays.asList(OrderTableIdRequest.of(단체_주문_테이블_4명.getId())));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupValidator.validateCreateRequestTableGroup(테이블_개수_1개));
    }

    @Test
    @DisplayName("테이블 그룹 등록시 조회된 테이블 개수가 다른 경우 테이블 등록 실패 된다")
    void tableGroupOrderIdsFindInNoSuch() {
        // given
        List<Long> 테이블_개수_2개 = Arrays.asList(단체_주문_테이블_4명.getId(), 단체_주문_테이블_6명.getId());

        // when
        when(orderTableRepository.findByIdIn(anyList())).thenReturn(Collections.singletonList(단체_주문_테이블_6명));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupValidator.validateCreateOrderTableGroup(테이블_개수_2개))
                .withMessage(TABLE_GROUP_ORDER_IDS_FIND_IN_NO_SUCH)
        ;
    }

    @Test
    @DisplayName("테이블 그룹 등록시 테이블 그룹이 이미 등록되어 있는 경우 실패 된다.")
    void tableGroupOrderNotEmpty() {
        // given
        List<Long> 테이블_개수_2개 = Arrays.asList(단체_주문_테이블_4명.getId(), 주문_테이블_여섯이_있음.getId());

        // when
        when(orderTableRepository.findByIdIn(any())).thenReturn(Arrays.asList(단체_주문_테이블_4명, 주문_테이블_여섯이_있음));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupValidator.validateCreateOrderTableGroup(테이블_개수_2개))
                .withMessage(TABLE_GROUP_ORDER_NOT_EMPTY)
        ;
    }

    @Test
    @DisplayName("테이블 그룹 해제시 정상적으로 유효성 검사가 성공 된다.")
    void validateUngroup() {
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(단체_주문_테이블_4명));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        assertDoesNotThrow(() -> tableGroupValidator.validateUnGroup(단체_주문_테이블_그룹));
    }

    @Test
    @DisplayName("테이블 그룹 해제시 테이블내 주문 상태가 식사, 요리 상태라면 테이블 그룹 해제가 실패한다")
    void orderTableStatusCannotUpdate() {
        // when
        when(orderTableRepository.findAllByTableGroupId(any())).thenReturn(Arrays.asList(단체_주문_테이블_4명));
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupValidator.validateUnGroup(단체_주문_테이블_그룹))
                .withMessage(ORDER_TABLE_STATUS_CANNOT_UPDATE);
    }
}
