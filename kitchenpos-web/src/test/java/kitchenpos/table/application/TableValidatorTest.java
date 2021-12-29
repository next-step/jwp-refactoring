package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.exception.TableEmptyUpdateException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.order.fixtures.OrderFixtures.주문;
import static kitchenpos.table.fixtures.OrderTableFixtures.그룹화되지_않은_테이블;
import static kitchenpos.table.fixtures.OrderTableFixtures.그룹화된_테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.table.application
 * fileName : TableValidatorTest
 * author : haedoang
 * date : 2021/12/26
 * description :
 */
@DisplayName("테이블 Validator 테스트")
@ExtendWith(MockitoExtension.class)
public class TableValidatorTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableValidator tableValidator;

    @Test
    @DisplayName("테이블이 그룹 테이블인 경우 상태를 변경할 수 없다.")
    public void changeTableStatusFailByGroupTable() {
        // then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(그룹화된_테이블())).isInstanceOf(TableEmptyUpdateException.class);
    }

    @Test
    @DisplayName("테이블의 주문 상태가 조리, 식사인 경우 변경할 수 없다.")
    public void changeTableStatusFailByTableOrderStatus() throws Exception {
        given(orderRepository.findByOrderTableId(any())).willReturn(Lists.newArrayList(주문()));

        // then
        assertThatThrownBy(() -> tableValidator.validateChangeEmpty(그룹화되지_않은_테이블())).isInstanceOf(TableEmptyUpdateException.class);
    }

}
