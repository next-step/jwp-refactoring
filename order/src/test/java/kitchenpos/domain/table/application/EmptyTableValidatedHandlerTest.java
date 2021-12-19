package kitchenpos.domain.table.application;

import kitchenpos.domain.order.domain.EmptyTableValidatedEvent;
import kitchenpos.domain.table.domain.InMemoryOrderTableRepository;
import kitchenpos.domain.table.domain.OrderTable;
import kitchenpos.domain.table.domain.OrderTableRepository;
import kitchenpos.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static kitchenpos.domain.order.fixture.OrderFixture.주문_요청;
import static kitchenpos.domain.table.fixture.OrderTableFixture.주문_테이블;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EmptyTableValidatedHandlerTest {

    private OrderTableRepository orderTableRepository;
    private EmptyTableValidatedHandler eventHandler;
    private OrderTable 저장된_주문_테이블;

    @BeforeEach
    void setUp() {
        orderTableRepository = new InMemoryOrderTableRepository();
        eventHandler = new EmptyTableValidatedHandler(orderTableRepository);
    }

    @Test
    void create_주문_테이블이_올바르지_않으면_주문을_등록할_수_없다() {
        OrderTable 비어있는_주문_테이블 = orderTableRepository.save(주문_테이블(2, true));
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> eventHandler.handle(new EmptyTableValidatedEvent(비어있는_주문_테이블.getId())));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L})
    void create_주문_테이블이_존재하지_않으면_등록할_수_없다(Long 존재하지_않는_주문_테이블_아이디) {
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> eventHandler.handle(new EmptyTableValidatedEvent(존재하지_않는_주문_테이블_아이디)));
    }

}
