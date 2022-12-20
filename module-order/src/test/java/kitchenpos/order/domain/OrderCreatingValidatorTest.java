package kitchenpos.order.domain;

import kitchenpos.DomainServiceTest;
import kitchenpos.menu.domain.MenuServiceTestSupport;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.exception.EmptyOrderLineItemsException;
import kitchenpos.order.exception.InvalidOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OrderCreatingValidator 클래스 테스트")
class OrderCreatingValidatorTest extends DomainServiceTest {

    @Autowired
    private OrderCreatingValidator orderCreatingValidator;

    private Menu 강정치킨;
    private OrderTable 테이블;
    private OrderTable 빈테이블;

    @BeforeEach
    void setUp(@Autowired MenuServiceTestSupport menuServiceTestSupport,
               @Autowired OrderTableRepository orderTableRepository) {
        강정치킨 = menuServiceTestSupport.신메뉴_강정치킨_가져오기();
        테이블 = orderTableRepository.save(new OrderTable(5, false));
        빈테이블 = orderTableRepository.save(new OrderTable(0, true));
    }

    @DisplayName("주문 생성에 대한 정책을 검증한다.")
    @Test
    void validate() {
        assertThatNoException().isThrownBy(() -> {
            orderCreatingValidator.validate(Arrays.asList(강정치킨.getId()), 테이블.getId());
        });
    }

    @DisplayName("메뉴 없이 주문 생성에 대한 정책을 검증한다.")
    @Test
    void validateWithEmptyMenus() {
        assertThatThrownBy(() -> {
            orderCreatingValidator.validate(Collections.emptyList(), 테이블.getId());
        }).isInstanceOf(EmptyOrderLineItemsException.class)
        .hasMessageContaining("주문 항목이 비었습니다.");
    }

    @DisplayName("존재하지 않는 메뉴가 포함된 주문 생성에 대한 정책을 검증한다.")
    @Test
    void validateWithNotFountMenu() {
        Long 존재하지_않는_메뉴_id = Long.MAX_VALUE;
        assertThatThrownBy(() -> {
            orderCreatingValidator.validate(Arrays.asList(존재하지_않는_메뉴_id), 테이블.getId());
        }).isInstanceOf(InvalidOrderException.class)
        .hasMessageContaining("존재하지 않는 메뉴가 있습니다.");
    }

    @DisplayName("빈테이블로 주문 생성에 대한 정책을 검증한다.")
    @Test
    void validateWithEmptyTable() {
        assertThatThrownBy(() -> {
            orderCreatingValidator.validate(Arrays.asList(강정치킨.getId()), 빈테이블.getId());
        }).isInstanceOf(InvalidOrderException.class)
        .hasMessageContaining("빈 테이블 입니다.");
    }
}
