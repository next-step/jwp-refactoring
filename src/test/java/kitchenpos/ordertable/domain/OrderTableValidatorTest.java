package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 validation service 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableValidatorTest {

    @InjectMocks
    OrderTableValidator orderTableValidator;

    @DisplayName("단체 지정되어 있는 경우 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 단체_지정_주문_테이블_빈_테이블_여부_수정_예외처리() {
        TableGroup 단체 = new TableGroup(1L, null);
        Long 단체_지정된_주문_테이블_id = 2L;
        OrderTable 단체_지정된_주문_테이블 = new OrderTable(단체_지정된_주문_테이블_id, 단체.getId(), new NumberOfGuests(0), true);

        assertThatThrownBy(() -> orderTableValidator.validateEmptyChangeable(단체_지정된_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }
}
