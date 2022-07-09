package kitchenpos.order.mapper;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static kitchenpos.menu.MenuGenerator.메뉴_상품_목록_생성;
import static kitchenpos.menu.MenuGenerator.메뉴_생성;
import static kitchenpos.order.OrderGenerator.주문_물품_생성_요청;
import static kitchenpos.order.OrderGenerator.주문_생성_요청;
import static kitchenpos.table.TableGenerator.주문_테이블_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @MockBean
    private OrderTableRepository orderTableRepository;

    @MockBean
    private MenuRepository menuRepository;

    private OrderTable 주문_테이블 = spy(주문_테이블_생성(손님_수_생성(10)));
    private Menu 메뉴 = spy(메뉴_생성("메뉴", 1_000, 0L, 메뉴_상품_목록_생성(Collections.emptyList())));

    @DisplayName("주문 테이블에 주문 생성 시 예외가 발생해야 한다")
    @Test
    void validateOrderByNotSavedOrderTest() {
        // given
        Long 없는_주문_테이블_아이디 = -1L;
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(없는_주문_테이블_아이디, Collections.emptyList());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderMapper.mapFrom(주문_생성_요청));
    }

    @DisplayName("주문 생성 시 주문 물품이 포함되어 있지 않으면 예외가 발생해야 한다")
    @Test
    void validateOrderByNotIncludeOrderItemsTest() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블));
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(0L, Collections.emptyList());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderMapper.mapFrom(주문_생성_요청));
    }

    @DisplayName("주문 생성 시 없는 메뉴가 포함되어 있으면 예외가 발생해야 한다")
    @Test
    void validateOrderByIncludeNotSavedMenuTest() {
        // given
        Long 없는_메뉴_아이디 = -1L;
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블));
        when(menuRepository.findAllById(any())).thenReturn(Collections.emptyList());
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(0L, Collections.singletonList(주문_물품_생성_요청(없는_메뉴_아이디, 1L)));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderMapper.mapFrom(주문_생성_요청));
    }

    @DisplayName("빈 주문 테이블이 포함된 메뉴 생성 요청 시 예외가 발생해야 한다")
    @Test
    void validateOrderByIncludeEmptyTableTest() {
        // given
        when(메뉴.getId()).thenReturn(0L);
        when(주문_테이블.isEmpty()).thenReturn(true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블));
        when(menuRepository.findAllById(any())).thenReturn(Collections.singletonList(메뉴));
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(0L, Collections.singletonList(주문_물품_생성_요청(메뉴.getId(), 1L)));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderMapper.mapFrom(주문_생성_요청));
    }

    @DisplayName("정상 상태의 주문 생성시 예외가 발생하지 않아야 한다")
    @Test
    void validateOrderTest() {
        // given
        when(메뉴.getId()).thenReturn(0L);
        when(주문_테이블.isEmpty()).thenReturn(false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블));
        when(menuRepository.findAllById(any())).thenReturn(Collections.singletonList(메뉴));
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(0L, Collections.singletonList(주문_물품_생성_요청(메뉴.getId(), 1L)));

        // then
        assertThatNoException().isThrownBy(() -> orderMapper.mapFrom(주문_생성_요청));
    }
}
