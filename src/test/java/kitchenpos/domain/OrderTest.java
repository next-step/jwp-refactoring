package kitchenpos.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    OrderTable 테이블;
    OrderTable 빈_테이블;
    OrderLineItem 커플_메뉴_1개;
    OrderLineItem 싱글_메뉴_2개;

    @BeforeEach
    void init() {
        // given
        Product 샐러드 = new Product("샐러드", BigDecimal.valueOf(100));
        Product 스테이크 = new Product("스테이크", BigDecimal.valueOf(200));
        Product 에이드 = new Product("에이드", BigDecimal.valueOf(50));
        MenuGroup 양식 = new MenuGroup("양식");
        MenuProduct 샐러드_1개 = new MenuProduct(샐러드, 1);
        MenuProduct 스테이크_1개 = new MenuProduct(스테이크, 1);
        MenuProduct 에이드_1개 = new MenuProduct(에이드, 2);
        MenuProduct 에이드_2개 = new MenuProduct(에이드, 2);
        BigDecimal 메뉴_구성_상품_금액_총합 = 샐러드_1개.getPrice()
                .add(스테이크_1개.getPrice())
                .add(에이드_2개.getPrice());
        Menu 커플_메뉴 = Menu.createMenu(
                "커플 메뉴",
                메뉴_구성_상품_금액_총합,
                양식,
                Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_2개)
        );
        Menu 싱글_메뉴 = Menu.createMenu(
                "커플 메뉴",
                메뉴_구성_상품_금액_총합,
                양식,
                Arrays.asList(샐러드_1개, 스테이크_1개, 에이드_1개)
        );
        커플_메뉴_1개 = new OrderLineItem(커플_메뉴, 1);
        싱글_메뉴_2개 = new OrderLineItem(커플_메뉴, 2);
        테이블 = new OrderTable(null, 0, false);
        빈_테이블 = new OrderTable(null, 0, true);
    }

    @DisplayName("주문 생성에 성공한다.")
    @Test
    void 생성() {
        // when
        Order order = Order.createOrder(테이블, Arrays.asList(커플_메뉴_1개, 싱글_메뉴_2개));

        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("주문 항목이 1개 미만이면 주문 생성에 실패한다.")
    @Test
    void 생성_예외_주문_항목_1개_미만() {
        // when, then
        assertThatThrownBy(() -> Order.createOrder(테이블, Arrays.asList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 하나 이상이어야 합니다.");

    }

    @DisplayName("빈 테이블이면 주문 생성에 실패한다.")
    @Test
    void 생성_예외_빈_테이블() {
        // when, then
        assertThatThrownBy(() -> Order.createOrder(빈_테이블, Arrays.asList(커플_메뉴_1개, 싱글_메뉴_2개)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 등록할 수 없습니다.");
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void 상태_변경() {
        // given
        Order order = Order.createOrder(테이블, Arrays.asList(커플_메뉴_1개, 싱글_메뉴_2개));

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("계산 완료 상태이면 상태 변경에 실패한다.")
    @Test
    void 상태_변경_예외_계산_완료() {
        // given
        Order order = Order.createOrder(테이블, Arrays.asList(커플_메뉴_1개, 싱글_메뉴_2개));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalStateException.class);
    }
}
