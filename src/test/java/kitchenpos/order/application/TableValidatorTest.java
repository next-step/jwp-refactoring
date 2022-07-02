package kitchenpos.table.application;

import static kitchenpos.common.fixture.OrderTableFixture.주문테이블_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.common.exception.InvalidOrderStatusException;
import kitchenpos.order.application.TableValidatorImpl;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {

    private TableValidatorImpl tableValidator;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        tableValidator = new TableValidatorImpl(orderRepository);
    }

    @DisplayName("그룹되어 있으면 빈테이블로 변경할 수 없다.")
    @Test
    void changeEmpty_fail_group() {
        //given
        long tableGroupId = 1L;
        OrderTable orderTable = 주문테이블_데이터_생성(1L, tableGroupId, 4, true);

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableValidator.checkValidChangeEmpty(orderTable));
    }

    @DisplayName("요리중, 식사중 주문이 있으면 변경할 수 없다.")
    @Test
    void changeEmpty_fail_invalidOrderStatus() {
        //given
        OrderTable orderTable = 주문테이블_데이터_생성(1L, null, 4, true);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
                .isThrownBy(() -> tableValidator.checkValidChangeEmpty(orderTable));
    }

    @DisplayName("주문상태가 조리나 식사이면 그룹 해제할 수 없다.")
    @Test
    void ungroup_fail_invalidOrderStatus() {
        //given
        Long tableId1 = 1L;
        Long tableId2 = 2L;

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
                .isThrownBy(() -> tableValidator.checkValidUngroup(Arrays.asList(tableId1, tableId2)));
    }

}