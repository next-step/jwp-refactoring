package kitchenpos.table.domain;

import static common.OrderTableFixture.두번째_주문테이블;
import static common.OrderTableFixture.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.exception.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTableValidationTest {

    private OrderTableDomainValidation orderTableDomainValidation;

    @BeforeEach
    void setUp() {
        orderTableDomainValidation = new OrderTableDomainValidation();
    }

    @Test
    void 빈_테이블이_아니면_생성불가() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_주문테이블(), 두번째_주문테이블());

        assertThatThrownBy(() -> {
            orderTableDomainValidation.valid(orderTables);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_TABLE_IS_NOT_EMPTY_TABLE_OR_ALREADY_GROUP.getMessage());
    }

    @Test
    void 최소사이즈보다_작으면_생성불가() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_주문테이블());

        assertThatThrownBy(() -> {
            orderTableDomainValidation.valid(orderTables);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.ORDER_TABLES_IS_SMALL_THAN_MIN_TABLE_SIZE.getMessage());
    }

}