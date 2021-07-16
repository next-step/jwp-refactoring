package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.exception.MisMatchedOrderTablesSizeException;
import kitchenpos.table.exception.NonEmptyOrderTableNotFoundException;

@DisplayName("테이블 Validator 기능 검증")
@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @Test
    @DisplayName("비어있지 않은 테이블ID가 존재하지 않을 떄 오류 발생")
    void validateExistsOrderTableByIdAndEmptyIsFalse() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(10, true);
        given(orderTableRepository.findByIdAndEmptyIsFalse(anyLong())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> orderTableValidator.validateExistsOrderTableByIdAndEmptyIsFalse(100L))
                .isInstanceOf(NonEmptyOrderTableNotFoundException.class)
                .hasMessage("비어있지 않은 테이블 대상이 존재하지 않습니다. 입력 ID : 100");
    }
}
