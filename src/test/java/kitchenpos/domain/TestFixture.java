package kitchenpos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

//migration sql 기반 데이터
public class TestFixture {
	//common
	public static final Long 존재하지않는_ID = 0L;

	//MenuGroup
	public static final MenuGroup 메뉴그룹_두마리메뉴 = new MenuGroup(1L, "두마리메뉴");
	public static final MenuGroup 메뉴그룹_한마리메뉴 = new MenuGroup(2L, "한마리메뉴");
	public static final MenuGroup 메뉴그룹_순살파닭두마리메뉴 = new MenuGroup(3L, "순살파닭두마리메뉴");
	public static final MenuGroup 메뉴그룹_신메뉴 = new MenuGroup(4L, "신메뉴");

	public static final String 메뉴그룹_신규_NAME = "윙봉콤보메뉴";

	//Product
	public static final Product 상품_후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
	public static final Product 상품_양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));
	public static final Product 상품_반반치킨 = new Product(3L, "반반치킨", BigDecimal.valueOf(16000));
	public static final Product 상품_통구이 = new Product(4L, "통구이", BigDecimal.valueOf(16000));
	public static final Product 상품_간장치킨 = new Product(5L, "간장치킨", BigDecimal.valueOf(17000));
	public static final Product 상품_순살치킨 = new Product(6L, "순살치킨", BigDecimal.valueOf(17000));

	public static final String 상품_신규_NAME = "허니윙봉콤보";
	public static final BigDecimal 상품_신규_PRICE = BigDecimal.valueOf(18000);

	//Menu
	public static final Menu 메뉴_후라이드 = new Menu(1L, "후라이드", BigDecimal.valueOf(16000), 메뉴그룹_한마리메뉴);
	public static final Menu 메뉴_양념치킨 = new Menu(2L, "양념치킨", BigDecimal.valueOf(16000), 메뉴그룹_한마리메뉴);
	public static final Menu 메뉴_반반치킨 = new Menu(3L, "반반치킨", BigDecimal.valueOf(16000), 메뉴그룹_한마리메뉴);
	public static final Menu 메뉴_통구이 = new Menu(4L, "통구이", BigDecimal.valueOf(16000), 메뉴그룹_한마리메뉴);
	public static final Menu 메뉴_간장치킨 = new Menu(5L, "간장치킨", BigDecimal.valueOf(17000), 메뉴그룹_한마리메뉴);
	public static final Menu 메뉴_순살치킨 = new Menu(6L, "순살치킨", BigDecimal.valueOf(17000), 메뉴그룹_한마리메뉴);

	public static final String 메뉴_신규_NAME = "후라이드양념두마리";
	public static final BigDecimal 메뉴_신규_PRICE = BigDecimal.valueOf(25000);
	public static final Long 메뉴_신규_MENU_GROUP_ID = 1L;

	//MenuProduct
	public static final MenuProduct 메뉴상품_후라이드 = new MenuProduct(메뉴_후라이드, 상품_후라이드, 1);
	public static final MenuProduct 메뉴상품_양념치킨 = new MenuProduct(메뉴_양념치킨, 상품_양념치킨, 1);
	public static final MenuProduct 메뉴상품_반반치킨 = new MenuProduct(메뉴_반반치킨, 상품_반반치킨, 1);
	public static final MenuProduct 메뉴상품_통구이 = new MenuProduct(메뉴_통구이, 상품_통구이, 1);
	public static final MenuProduct 메뉴상품_간장치킨 = new MenuProduct(메뉴_간장치킨, 상품_간장치킨, 1);
	public static final MenuProduct 메뉴상품_순살치킨 = new MenuProduct(메뉴_순살치킨, 상품_순살치킨, 1);

	public static final Long 메뉴상품_신규_1_후라이드_ID = 1L;
	public static final int 메뉴상품_신규_1_후라이드_QUANTITY = 1;
	public static final Long 메뉴상품_신규_2_양념_ID = 2L;
	public static final int 메뉴상품_신규_2_양념_QUANTITY = 1;
	public static final BigDecimal 메뉴상품_신규_가격_총합 = BigDecimal.valueOf(32000);

	//OrderTable
	public static final OrderTable 테이블_비어있는_0명_1 = new OrderTable(1L, null,0, true);
	public static final OrderTable 테이블_비어있는_0명_2 = new OrderTable(2L, null,0, true);
	public static final OrderTable 테이블_비어있는_0명_3 = new OrderTable(3L, null,0, true);
	public static final OrderTable 테이블_비어있는_0명_4 = new OrderTable(4L, null,0, true);
	public static final OrderTable 테이블_비어있는_0명_5 = new OrderTable(5L, null,0, true);
	public static final OrderTable 테이블_비어있는_0명_6 = new OrderTable(6L, null,0, true);
	public static final OrderTable 테이블_비어있는_0명_7 = new OrderTable(7L, null,0, true);
	public static final OrderTable 테이블_비어있는_0명_8 = new OrderTable(8L, null,0, true);
	public static final OrderTable 테이블_비어있지않은_2명_9 = new OrderTable(9L, null,2, false);
	public static final OrderTable 테이블_단체지정됨_0명_10 = new OrderTable(10L, 1L,0, false);
	public static final OrderTable 테이블_요리중_3명_11 = new OrderTable(11L, null,3, false);
	public static final OrderTable 테이블_식사중_4명_12 = new OrderTable(12L, null,4, false);
	public static final OrderTable 테이블_계산완료_5명_13 = new OrderTable(13L, null,5, false);

	public static final Long 테이블_신규_ID = 10L;
	public static final int 테이블_신규_NUM_OF_GUESTS = 0;
	public static final boolean 테이블_신규_EMPTY = false;

	//TableGroup
	public static final TableGroup 테이블단체_1 = new TableGroup(1L, LocalDateTime.of(2021, 1, 28, 19, 30));

	public static final Long 테이블단체_신규_ID = 1L;
	public static final LocalDateTime 테이블단체_신규_CREATED_DATE = LocalDateTime.of(2021, 1, 28, 19, 30);

	//Order
	public static final Order 주문_요리중_테이블11 = new Order(1L, 11L, OrderStatus.COOKING.name(), LocalDateTime.of(2021, 1, 28, 12, 30));
	public static final Order 주문_식사중_테이블12 = new Order(2L, 12L, OrderStatus.MEAL.name(), LocalDateTime.of(2021, 1, 28, 13, 30));
	public static final Order 주문_계산완료_테이블13 = new Order(3L, 13L, OrderStatus.COMPLETION.name(), LocalDateTime.of(2021, 1, 28, 14, 30));

	public static final Long 주문_신규_ID = 1L;
	public static final Long 주문_신규_테이블_ID = 9L;
	public static final String 주문_신규_주문상태 = OrderStatus.COOKING.name();
	public static final LocalDateTime 주문_신규_ORDERED_DATE = LocalDateTime.of(2021, 1, 28, 19, 30);
	public static final Order 주문_신규 = new Order(주문_신규_ID, 주문_신규_테이블_ID, 주문_신규_주문상태, 주문_신규_ORDERED_DATE);

	//OrderLineItem
	public static final OrderLineItem 주문아이템_요리중_테이블11_후라이드_1개 = new OrderLineItem(1L, 1L, 1L, 1);
	public static final OrderLineItem 주문아이템_요리중_테이블11_양념_1개 = new OrderLineItem(2L, 1L, 2L, 1);
	public static final OrderLineItem 주문아이템_식사중_테이블12_반반_2개 = new OrderLineItem(3L, 2L, 3L, 2);
	public static final OrderLineItem 주문아이템_계산완료_테이블13_통구이_1개 = new OrderLineItem(4L, 3L, 4L, 1);
	public static final OrderLineItem 주문아이템_계산완료_테이블13_간장_1개 = new OrderLineItem(5L, 3L, 5L, 1);
	public static final OrderLineItem 주문아이템_계산완료_테이블13_순살_1개 = new OrderLineItem(6L, 3L, 6L, 1);
}
