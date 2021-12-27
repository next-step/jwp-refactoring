package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.OrderTableValidateEvent;
import kitchenpos.table.exception.TableException;

@DisplayName("주문 Validator : 통합 테스트")
@SpringBootTest
public class OrderValidatorTest {

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@DisplayName("주문 테이블 Validator 이벤트 Publish 테스트")
	@Test
	void publishEventTest() {
		// when then
		assertThatThrownBy(() -> {
			eventPublisher.publishEvent(OrderTableValidateEvent.from(1L));
		}).isInstanceOf(TableException.class)
			.hasMessageContaining(ErrorCode.ORDER_TABLE_IS_EMPTY.getMessage());
	}

}
