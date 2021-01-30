package kitchenpos.order.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menu.MenuGroup;
import kitchenpos.menu.domain.menu.MenuProduct;
import kitchenpos.menu.domain.product.Product;
import kitchenpos.order.domain.ordertable.OrderTable;

@DisplayName("주문 도메인 테스트")
public class OrderTest {

	private Product 후라이드치킨;
	private Product 양념치킨;
	private MenuGroup 치킨세트;
	private Menu 후라이드한마리_양념치킨한마리;
	private MenuProduct 후라이드치킨한마리;
	private MenuProduct 양념치킨한마리;

	private Order 주문;
	private OrderLineItem 주문_항목;
	private OrderTable 주문테이블_비어있지_않음;
	private List<OrderLineItem> 주문_항목_목록;

	@BeforeEach
	public void setup() {
		후라이드치킨 = new Product(1L, "후라이드치킨", new BigDecimal(16000));
		양념치킨 = new Product(2L, "양념치킨", new BigDecimal(16000));

		치킨세트 = new MenuGroup("후라이드앙념치킨");

		후라이드한마리_양념치킨한마리 = new Menu("후라이드+양념", BigDecimal.valueOf(32000), 치킨세트);

		후라이드치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 후라이드치킨, 1L);
		양념치킨한마리 = new MenuProduct(후라이드한마리_양념치킨한마리, 양념치킨, 1L);

		후라이드한마리_양념치킨한마리.addMenuProducts(Arrays.asList(후라이드치킨한마리, 양념치킨한마리));

		주문테이블_비어있지_않음 = spy(new OrderTable(5, false));
		given(주문테이블_비어있지_않음.getId()).willReturn(1L);

		주문_항목 = new OrderLineItem(후라이드한마리_양념치킨한마리, 1L);
		주문_항목_목록 = new ArrayList<>();
		주문_항목_목록.add(주문_항목);

		주문 = Order.createToCook(주문테이블_비어있지_않음, 주문_항목_목록);
	}

	@DisplayName("주문 생성")
	@Test
	void create() {
		assertThat(주문).isNotNull();
		assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
	}

	@DisplayName("주문 상태 변경")
	@Test
	void changeOrderStatus() {
		주문.changeOrderStatus(OrderStatus.COMPLETION.name());
		assertThat(주문).isNotNull();
		assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
	}

	@DisplayName("주문 상태 변경 예외: 이미 완료된 상태 변경 안됨")
	@Test
	void changeOrderStatusThrowExceptionWhenAlreadyCompletionOrder() {
		주문.changeOrderStatus(OrderStatus.COMPLETION.name());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> 주문.changeOrderStatus(OrderStatus.COOKING.name()));
	}

}
