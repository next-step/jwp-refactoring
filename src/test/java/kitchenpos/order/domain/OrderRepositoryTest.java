package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderRepositoryTest {
    @DisplayName("단체 지정")
    @Test
    void group() {
    }

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경")
    @Test
    void changeEmpty() {
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 단체 지정인 경우")
    @Test
    void validExistTableGroup() {
    }


    @DisplayName("손님의 수 변경")
    @Test
    void changeNumberOfGuests() {
    }

    @DisplayName("손님의 수 변경 예외 - 0보다 작을 경우")
    @Test
    void validNumberOfGuests() {
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 요리중이거나 식사중인 경우")
    @Test
    void validOrderStatusCookingOrMeal() {
    }
}
