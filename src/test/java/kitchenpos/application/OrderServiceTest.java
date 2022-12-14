package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.application.MenuGroupServiceTest.메뉴_그룹;
import static kitchenpos.application.TableServiceTest.주문_테이블;
import static kitchenpos.domain.MenuProductTest.메뉴_상품;
import static kitchenpos.domain.MenuTest.메뉴;
import static kitchenpos.domain.OrderTableTest.두_명의_방문객;
import static kitchenpos.domain.OrderTableTest.비어있지_않은_상태;
import static kitchenpos.domain.OrderTableTest.빈_상태;
import static kitchenpos.domain.OrderTest.주문;
import static kitchenpos.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@DisplayName("주문 테스트")
@SpringBootTest
public class OrderServiceTest {

    public static final OrderStatus 조리_상태 = OrderStatus.COOKING;
    private static final OrderStatus 식사_상태 = OrderStatus.MEAL;
    private static final OrderStatus 계산_완료_상태 = OrderStatus.COMPLETION;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 주문 = 주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)));
        //when,then:
        assertThat(orderService.create(주문)).isEqualTo(주문);
    }

    @DisplayName("생성 성공 - 주문 상태는 조리 상태여야 한다")
    @Test
    void 생성_성공_조리_상태() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 주문 = 주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)));
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
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Collections.emptyList());
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("생성 예외 - 주문 항목의 메뉴 갯수와 실제 존재하는 메뉴 갯수가 일치하지 않는 경우")
    @Test
    void 생성_예외_주문_항목의_갯수와_실제_메뉴의_갯수가_일치하지_않는_경우() {
        //given:
        final Menu 저장되지_않은_메뉴 =
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1)));

        final Order 주문 = 주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장되지_않은_메뉴.getId(), 1L)));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("생성 예외 - 주문 테이블이 존재하지 않는 경우")
    @Test
    void 생성_예외_주문_테이블이_존재하지_않는_경우() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 주문 = 주문(
                주문_테이블(2, 비어있지_않은_상태).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("생성 예외 - 주문 테이블이 빈 테이블일 경우")
    @Test
    void 생성_예외_주문_테이블이_빈_테이블일_경우() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 주문 = 주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 빈_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(주문));
    }

    @DisplayName("목록 조회 성공 - 주문 항목도 함께 조회 된다")
    @Test
    void 목록_조회_성공() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final OrderLineItem 주문_항목 = new OrderLineItem(저장된_메뉴.getId(), 1L);

        final Order 주문 = 주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(주문_항목));

        final Order 생성된_주문 = orderService.create(주문);
        //when,then:
        assertThat(orderService.list().stream()
                .filter(it -> it.equals(생성된_주문))
                .findFirst()
                .get()
                .getOrderLineItems()).contains(주문_항목);
    }

    @DisplayName("상태 변경 성공 - 조리 상태에서 식사 상태")
    @Test
    void 상태_변경_성공_조리_상태에서_식사_상태() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 생성된_주문 = orderService.create(주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                조리_상태.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        final Order 계산_완료_주문 = 생성된_주문.changeStatus(식사_상태);

        //when,then:
        assertThat(orderService.changeOrderStatus(생성된_주문.getId(), 계산_완료_주문).isStatus(식사_상태)).isTrue();
    }

    @DisplayName("상태 변경 성공 - 식사 상태에서 계산 완료 상태")
    @Test
    void 상태_변경_성공_식사_상태에서_계산_완료_상태() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 생성된_주문 = orderService.create(주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                조리_상태.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        final Order 계산_완료_주문 = 생성된_주문.changeStatus(식사_상태);

        //when,then:
        assertThat(orderService.changeOrderStatus(생성된_주문.getId(), 계산_완료_주문).isStatus(식사_상태)).isTrue();
    }

    @DisplayName("상태 변경 예외 - 주문이 존재하지 않는 경우")
    @Test
    void 상태_변경_예외_주문이_존재하지_않는_경우() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 생성된_주문 = 주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)));

        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.changeOrderStatus(생성된_주문.getId(), 생성된_주문.changeStatus(계산_완료_상태)));
    }

    @DisplayName("상태 변경 예외 - 주문이 계산 완료 상태인 경우")
    @Test
    void 상태_변경_예외_주문이_계산_완료_상태인_경우() {
        //given:
        final Menu 저장된_메뉴 = menuService.create(
                메뉴("자메이카 통다리 1인 세트",
                        BigDecimal.ONE,
                        menuGroupDao.save(메뉴_그룹("추천 메뉴")).getId(),
                        Arrays.asList(
                                메뉴_상품(productDao.save(상품("통다리", BigDecimal.ONE)).getId(), 5),
                                메뉴_상품(productDao.save(상품("콜라", BigDecimal.ONE)).getId(), 1))));

        final Order 생성된_주문 = orderService.create(주문(
                orderTableDao.save(주문_테이블(두_명의_방문객, 비어있지_않은_상태)).getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        final Order 계산_완료_주문 = orderService.changeOrderStatus(생성된_주문.getId(), 생성된_주문.changeStatus(계산_완료_상태));

        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() ->
                orderService.changeOrderStatus(계산_완료_주문.getId(), 계산_완료_주문.changeStatus(식사_상태)));
    }

}
