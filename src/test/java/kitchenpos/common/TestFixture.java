package kitchenpos.common;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.OrderLineItemRequest;

public class TestFixture {
	public static final String 예제_메뉴그룹명 = "인도요리";
	public static final String 예제_상품명 = "피자";
	public static final BigDecimal 예제_상품_가격 = BigDecimal.valueOf(15000);
	public static final long 주문대상_테이블ID = 7L;
	public static final long 빈_테이블ID = 1L;
	public static final long 존재하지_않는_테이블ID = 999L;
	public static final long 예제_테이블_그룹_ID = 1L;
	public static final long 조리상태인_테이블그룹_ID = 2L;
	public static final BigDecimal 후라이드_가격 = BigDecimal.valueOf(16000);
	public static final BigDecimal 양념치킨_가격 = BigDecimal.valueOf(16000);
	public static final BigDecimal 예제_메뉴_가격 = BigDecimal.valueOf(20000);
	public static final Long 예제_메뉴_그룹_ID = 1L;
	public static final Long 메뉴_그룹_ID_없음 = null;
	public static final String 예제_메뉴명 = "치킨 선물 박스";
	public static final long 예제테이블1_ID = 1L;
	public static final long 예제테이블2_ID = 2L;
	public static final long 비어있지않은테이블_ID = 7L;
	public static final long 단체지정되어있지만_주문없는_테이블_ID = 5L;
	public static final long 존재하지않는_테이블ID = 999L;

	public static final long 빈테이블ID = 1L;
	public static final long 단체지정_테이블ID = 5L;
	public static final long 주문상태_조리인_테이블ID = 7L;
	public static final long 주문상태_식사인_테이블ID = 8L;
	public static final OrderLineItemRequest 주문_메뉴1 = OrderLineItemRequest.of(1L, 1L);
	public static final OrderLineItemRequest 주문_메뉴2 = OrderLineItemRequest.of(2L, 1L);
	public static final OrderLineItemRequest 주문_존재하지않은메뉴 = OrderLineItemRequest.of(999L, 1L);
	public static final long 조리상태_주문ID = 1L;
	public static final long 식사상태_주문ID = 2L;
	public static final long 완료상태_주문ID = 5L;

	public static final MenuProductRequest 메뉴_후라이드_갯수 = MenuProductRequest.of(1L, 1L);
	public static final MenuProductRequest 메뉴_양념_갯수 = MenuProductRequest.of(2L, 1L);

	public static OrderTable 그룹_지정된_테이블_객체() {
		OrderTable table = OrderTable.create();
		table.saveGroupInfo(TableGroup.create(Collections.emptyList()));
		return table;
	}

	public static OrderTable 비어있지않은_테이블_객체() {
		OrderTable table = OrderTable.create();
		table.changeEmpty(false);
		return table;
	}

	public static OrderTable 빈_테이블_객체() {
		return OrderTable.create();
	}

	public static OrderTable 빈_테이블_객체1() {
		return OrderTable.create();
	}

	public static OrderTable 빈_테이블_객체2() {
		return OrderTable.create();
	}

	public static Product 예제_상품1() {
		return Product.create("예제상품1", BigDecimal.valueOf(5000));
	}

	public static Product 예제_상품2() {
		return Product.create("예제상품2", BigDecimal.valueOf(6000));
	}

	public static Product 오천원_상품() {
		return Product.create("오천원_상품", BigDecimal.valueOf(5000));
	}

	public static Product 육천원_상품() {
		return Product.create("육천원_상품", BigDecimal.valueOf(6000));
	}

	public static Menu 일반_메뉴1() {
		return Menu.create("일반1", BigDecimal.valueOf(4000L), MenuGroup.create("한식"), Arrays.asList(오천원_상품()), Arrays.asList(1L));
	}

	public static Menu 일반_메뉴2() {
		return Menu.create("일반1", BigDecimal.valueOf(4000L), MenuGroup.create("한식"), Arrays.asList(오천원_상품()), Arrays.asList(1L));
	}

	public static Order 일반_주문() {
		return Order.create(비어있지않은_테이블_객체(), Arrays.asList(일반_메뉴1(), 일반_메뉴2()), Arrays.asList(1L, 2L));
	}

	public static Order 완료된_주문() {
		Order order = 일반_주문();
		order.changeOrderStatus("COMPLETION");
		return order;
	}
}
