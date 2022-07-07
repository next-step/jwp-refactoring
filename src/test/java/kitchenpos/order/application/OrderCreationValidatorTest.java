package kitchenpos.order.application;

import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_생성_요청_객체;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.CreateOrderTableItemRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.utils.generator.ScenarioTestFixtureGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:OrderCreationValidator")
class OrderCreationValidatorTest extends ScenarioTestFixtureGenerator {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderCreationValidator orderCreationValidator;

    private OrderTable 비어있지_않은_주문_테이블_생성, 비어있는_주문_테이블_생성;
    private CreateOrderRequest 커플_냉삼_메뉴_주문_생성_요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        비어있지_않은_주문_테이블_생성 = 비어있지_않은_주문_테이블_생성();
        비어있는_주문_테이블_생성 = 비어있는_주문_테이블_생성();
        커플_냉삼_메뉴_주문_생성_요청 = 주문_생성_요청_객체(비어있지_않은_주문_테이블_생성, 커플_냉삼_메뉴);
    }

    @Test
    @DisplayName("주문에 포함된 주문테이블이 비어있는 경우(주문을 요청한 테이블이 `isEmpty() = true`인 경우) 예외가 발생 검증")
    public void throwException_When() {
        // When & Then
        given(menuRepository.findAllById(anyList())).willReturn(Collections.singletonList(커플_냉삼_메뉴));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(비어있는_주문_테이블_생성));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderCreationValidator.validate(커플_냉삼_메뉴_주문_생성_요청));
    }

    @Test
    @DisplayName("주문 항목이 없는 경우 예외 발생 검증")
    public void throwException_WhenOrderLineItemIsEmpty() {
        CreateOrderRequest 주문_생성_요청_객체 = 주문_생성_요청_객체(비어있지_않은_주문_테이블_생성);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderCreationValidator.validate(주문_생성_요청_객체));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 주문한 경우 예외 발생 검증")
    public void throwException_WhenOrderMenuCountIsOverThanPersistMenusCount() {
        CreateOrderTableItemRequest 존재하지_않는_메뉴의_주문_항목 = new CreateOrderTableItemRequest(1L, 1L);
        CreateOrderRequest 존재하지_않는_메뉴의_주문_항목이_포함된_주문_생성_요청 = new CreateOrderRequest(1L,
            Collections.singletonList(존재하지_않는_메뉴의_주문_항목));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderCreationValidator.validate(존재하지_않는_메뉴의_주문_항목이_포함된_주문_생성_요청));
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않는 주문을 생성하는 경우 예외 발생 검증")
    public void throwException_WhenOrderTableIsNotExist() {
        // Given
        given(menuRepository.findAllById(anyList())).willReturn(Collections.singletonList(커플_냉삼_메뉴));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderCreationValidator.validate(커플_냉삼_메뉴_주문_생성_요청));
    }
}
