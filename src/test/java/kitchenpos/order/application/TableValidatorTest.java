package kitchenpos.table.application;

import kitchenpos.common.exception.InvalidOrderStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.common.fixture.OrderTableFixture.주문테이블_데이터_생성;
import static kitchenpos.common.fixture.TableGroupFixture.단체_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {

    private TableValidator tableValidator;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        tableValidator = new TableValidator(orderRepository);
    }

    @DisplayName("그룹되어 있으면 빈테이블로 변경할 수 없다.")
    @Test
    void changeEmpty_fail_group() {
        //given
        TableGroup tableGroup = 단체_데이터_생성(1L);
        OrderTable orderTable = 주문테이블_데이터_생성(1L, tableGroup, 4, true);

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
        Long tableGroupId = 1L;
        TableGroup tableGroup = 단체_데이터_생성(tableGroupId);
        OrderTable table1 = 주문테이블_데이터_생성(1L, null, 4, true);
        OrderTable table2 = 주문테이블_데이터_생성(2L, null, 4, true);
        tableGroup.group(Arrays.asList(table1, table2));

        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //when //then
        assertThatExceptionOfType(InvalidOrderStatusException.class)
                .isThrownBy(() -> tableValidator.checkValidUngroup(tableGroup));
    }

}