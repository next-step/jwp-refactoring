package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductBag;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemBag;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_추천_메뉴;
import static kitchenpos.menu.domain.MenuProductTest.메뉴_상품;
import static kitchenpos.menu.domain.MenuTest.메뉴;
import static kitchenpos.order.domain.OrderTableTest.두_명의_방문객;
import static kitchenpos.order.domain.OrderTableTest.비어있지_않은_상태;
import static kitchenpos.order.domain.OrderTableTest.빈_상태;
import static kitchenpos.order.domain.OrderTest.계산_완료_상태;
import static kitchenpos.order.domain.OrderTest.식사_상태;
import static kitchenpos.order.domain.OrderTest.조리_상태;
import static kitchenpos.order.domain.OrderTest.주문;
import static kitchenpos.product.domain.ProductTest.상품_콜라;
import static kitchenpos.product.domain.ProductTest.상품_통다리;
import static kitchenpos.table.application.TableServiceTest.주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@DisplayName("주문 테스트")
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 주문 = 주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));
        //when,then:
        assertThat(orderService.create(주문)).isEqualTo(주문);
    }

    @DisplayName("생성 성공 - 주문 상태는 조리 상태여야 한다")
    @Test
    void 생성_성공_조리_상태() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 주문 = 주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));
        //when:
        final Order 생성된_주문 = orderService.create(주문);
        //then:
        assertThat(생성된_주문.isStatus(OrderStatus.COOKING)).isTrue();
    }

    @DisplayName("생성 예외 - 주문 항목을 포함하지 않는 경우")
    @Test
    void 생성_예외_주문_항몫을_포함하지_않는_경우() {
        //given:
        final Order 주문 = 주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Collections.emptyList()));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("생성 예외 - 주문 항목의 메뉴 갯수와 실제 존재하는 메뉴 갯수가 일치하지 않는 경우")
    @Test
    void 생성_예외_주문_항목의_갯수와_실제_메뉴의_갯수가_일치하지_않는_경우() {
        //given:
        final Menu 저장되지_않은_메뉴 =
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1))));

        final Order 주문 = 주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장되지_않은_메뉴.getId(), 1L))));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("생성 예외 - 주문 테이블이 존재하지 않는 경우")
    @Test
    void 생성_예외_주문_테이블이_존재하지_않는_경우() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 주문 = 주문(
                주문_테이블(2, 비어있지_않은_상태),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("생성 예외 - 주문 테이블이 빈 테이블일 경우")
    @Test
    void 생성_예외_주문_테이블이_빈_테이블일_경우() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 주문 = 주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 빈_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("목록 조회 성공 - 주문 항목도 함께 조회 된다")
    @Test
    void 목록_조회_성공() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final OrderLineItem 주문_항목 = new OrderLineItem(저장된_메뉴.getId(), 1L);

        final Order 주문 = 주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(주문_항목)));

        final Order 생성된_주문 = orderService.create(주문);
        //when,then:
        assertThat(orderService.list().stream()
                .filter(it -> it.equals(생성된_주문))
                .findFirst()
                .get()
                .getOrderLineItemBag().getOrderLineItemList()).contains(주문_항목);
    }

    @DisplayName("상태 변경 성공 - 조리 상태에서 식사 상태")
    @Test
    void 상태_변경_성공_조리_상태에서_식사_상태() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 생성된_주문 = orderService.create(주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)))));

        생성된_주문.changeStatus(식사_상태);

        //when,then:
        assertThat(orderService.changeOrderStatus(생성된_주문.getId(), 생성된_주문).isStatus(식사_상태)).isTrue();
    }

    @DisplayName("상태 변경 성공 - 식사 상태에서 계산 완료 상태")
    @Test
    void 상태_변경_성공_식사_상태에서_계산_완료_상태() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 생성된_주문 = orderService.create(주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)))));

        생성된_주문.changeStatus(식사_상태);

        //when,then:
        assertThat(orderService.changeOrderStatus(생성된_주문.getId(), 생성된_주문).isStatus(식사_상태)).isTrue();
    }

    @DisplayName("상태 변경 예외 - 주문이 존재하지 않는 경우")
    @Test
    void 상태_변경_예외_주문이_존재하지_않는_경우() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 생성된_주문 = 주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));
        생성된_주문.changeStatus(계산_완료_상태);
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.changeOrderStatus(생성된_주문.getId(), 생성된_주문));
    }

    @DisplayName("상태 변경 예외 - 주문이 계산 완료 상태인 경우")
    @Test
    void 상태_변경_예외_주문이_계산_완료_상태인_경우() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴(Name.from("자메이카 통다리 1인 세트"),
                        Price.from(BigDecimal.ONE),
                        menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                        MenuProductBag.from(Arrays.asList(
                                메뉴_상품(productRepository.save(상품_통다리()), 5),
                                메뉴_상품(productRepository.save(상품_콜라()), 1)))));

        final Order 생성된_주문 = orderService.create(주문(
                orderTableRepository.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)))));

        생성된_주문.changeStatus(계산_완료_상태);
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.changeOrderStatus(생성된_주문.getId(), 생성된_주문));
    }

}
