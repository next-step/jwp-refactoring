package kitchenpos.order.validator;

import static kitchenpos.fixture.OrderFixture.주문요청_생성;
import static kitchenpos.fixture.TableFixture.테이블_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderValidator orderValidator;

    private OrderTable orderTable;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderTable = 테이블_생성(1L, 4, false);
        orderRequest = 주문요청_생성(orderTable.getId(), Arrays.asList(
                new OrderLineItem(1L, 1L, 1L),
                new OrderLineItem(2L, 2L, 1L)
        ));
    }

    @DisplayName("주문테이블이 존재하지 않으면, IllegalArgumentException 이 발생한다.")
    @Test
    void invalid_notExistsOrderTable() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderValidator.validate(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문테이블이 존재하지 않습니다.");
    }

    @DisplayName("빈 주문테이블로 등록하면, IllegalArgumentException 이 발생한다.")
    @Test
    void invalid_EmptyOrderTable() {
        //given
        orderTable.changeEmpty(true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> orderValidator.validate(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블은 등록할 수 없습니다.");
    }

    @DisplayName("주문항목이 1개 미만인 경우, IllegalArgumentException 이 발생한다.")
    @Test
    void invalid_validateOrderLineItem() {
        //given
        orderRequest = 주문요청_생성(orderTable.getId(), new ArrayList<>());
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(() -> orderValidator.validate(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 1개 이상이어야 합니다.");
    }

    @DisplayName("주문항목의 메뉴가 존재하지 않으면, IllegalArgumentException 이 발생한다.")
    @Test
    void invalid_notExistsMenu() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderValidator.validate(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴입니다.");
    }
}
