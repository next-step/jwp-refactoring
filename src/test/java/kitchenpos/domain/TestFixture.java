package kitchenpos.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.mockito.internal.matchers.Or;

public class TestFixture {
	//MenuGroup
	public static final MenuGroup 메뉴그룹_두마리메뉴 = new MenuGroup(1L, "두마리메뉴");
	public static final MenuGroup 메뉴그룹_한마리메뉴 = new MenuGroup(2L, "한마리메뉴");
	public static final MenuGroup 메뉴그룹_순살파닭두마리메뉴 = new MenuGroup(3L, "순살파닭두마리메뉴");
	public static final MenuGroup 메뉴그룹_신메뉴 = new MenuGroup(4L, "신메뉴");

	public static final Long 메뉴그룹_신규_ID = 5L;
	public static final String 메뉴그룹_신규_NAME = "윙봉콤보메뉴";
	public static final MenuGroup 메뉴그룹_신규_윙봉콤보메뉴 = new MenuGroup(메뉴그룹_신규_ID, 메뉴그룹_신규_NAME);

	//Product
	public static final Product 상품_후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
	public static final Product 상품_양념치킨 = new Product(2L, "양념치킨", BigDecimal.valueOf(16000));
	public static final Product 상품_반반치킨 = new Product(3L, "반반치킨", BigDecimal.valueOf(16000));
	public static final Product 상품_통구이 = new Product(4L, "통구이", BigDecimal.valueOf(16000));
	public static final Product 상품_간장치킨 = new Product(5L, "간장치킨", BigDecimal.valueOf(17000));
	public static final Product 상품_순살치킨 = new Product(6L, "순살치킨", BigDecimal.valueOf(17000));

	public static final Long 상품_신규_ID = 7L;
	public static final String 상품_신규_NAME = "허니윙봉콤보";
	public static final BigDecimal 상품_신규_PRICE = BigDecimal.valueOf(18000);
	public static final Product 상품_신규_허니윙봉콤보 = new Product(상품_신규_ID, 상품_신규_NAME, 상품_신규_PRICE);

	//Menu
	public static final Menu 메뉴_후라이드 = new Menu(1L, "후라이드", BigDecimal.valueOf(16000), 2L);
	public static final Menu 메뉴_양념치킨 = new Menu(2L, "양념치킨", BigDecimal.valueOf(16000), 2L);
	public static final Menu 메뉴_반반치킨 = new Menu(3L, "반반치킨", BigDecimal.valueOf(16000), 2L);
	public static final Menu 메뉴_통구이 = new Menu(4L, "통구이", BigDecimal.valueOf(16000), 2L);
	public static final Menu 메뉴_간장치킨 = new Menu(5L, "간장치킨", BigDecimal.valueOf(17000), 2L);
	public static final Menu 메뉴_순살치킨 = new Menu(6L, "순살치킨", BigDecimal.valueOf(17000), 2L);

	public static final Long 메뉴_신규_ID = 7L;
	public static final String 메뉴_신규_NAME = "후라이드양념두마리";
	public static final BigDecimal 메뉴_신규_PRICE = BigDecimal.valueOf(25000);
	public static final Long 메뉴_신규_MENU_GROUP_ID = 1L;
	public static final Menu 메뉴_신규_후라이드양념두마리 = new Menu(메뉴_신규_ID, 메뉴_신규_NAME, 메뉴_신규_PRICE, 메뉴_신규_MENU_GROUP_ID);

	//MenuProduct
	public static final MenuProduct 메뉴상품_후라이드 = new MenuProduct(1L, 1L, 1);
	public static final MenuProduct 메뉴상품_양념치킨 = new MenuProduct(2L, 2L, 1);
	public static final MenuProduct 메뉴상품_반반치킨 = new MenuProduct(3L, 3L, 1);
	public static final MenuProduct 메뉴상품_통구이 = new MenuProduct(4L, 4L, 1);
	public static final MenuProduct 메뉴상품_간장치킨 = new MenuProduct(5L, 5L, 1);
	public static final MenuProduct 메뉴상품_순살치킨 = new MenuProduct(6L, 6L, 1);

	public static final Long 메뉴상품_신규_MENU_ID = 7L;
	public static final Long 메뉴상품_신규_1_후라이드_ID = 1L;
	public static final int 메뉴상품_신규_1_후라이드_QUANTITY = 1;
	public static final Long 메뉴상품_신규_2_양념_ID = 2L;
	public static final int 메뉴상품_신규_2_양념_QUANTITY = 1;
	public static final MenuProduct 메뉴상품_신규_후라이드 = new MenuProduct(메뉴상품_신규_MENU_ID, 메뉴상품_신규_1_후라이드_ID, 메뉴상품_신규_1_후라이드_QUANTITY);
	public static final MenuProduct 메뉴상품_신규_양념 = new MenuProduct(메뉴상품_신규_MENU_ID, 메뉴상품_신규_2_양념_ID, 메뉴상품_신규_2_양념_QUANTITY);
	public static final Menu 메뉴_신규_후라이드양념두마리_WITH_상품목록 = new Menu(메뉴_신규_ID
		, 메뉴_신규_NAME
		, 메뉴_신규_PRICE
		, 메뉴_신규_MENU_GROUP_ID
		, Arrays.asList(메뉴상품_신규_후라이드, 메뉴상품_신규_양념));

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

	public static final Long 테이블_신규_ID = 10L;
	public static final int 테이블_신규_NUM_OF_GUESTS = 0;
	public static final boolean 테이블_신규_EMPTY = false;
	public static final OrderTable 테이블_신규_후라이드양념두마리 = new OrderTable(테이블_신규_ID, null, 테이블_신규_NUM_OF_GUESTS, 테이블_신규_EMPTY);

	//TableGroup
	public static final Long 테이블단체_신규_ID = 1L;
	public static final LocalDateTime 테이블단체_신규_CREATED_DATE = LocalDateTime.of(2021, 1, 28, 19, 30);
	public static final TableGroup 테이블단체_신규 = new TableGroup(테이블단체_신규_ID, 테이블단체_신규_CREATED_DATE);

	public static final Long 테이블단체_신규_테이블1_ID = 1L;
	public static final Long 테이블단체_신규_테이블2_ID = 2L;
	public static final OrderTable 테이블단체_테이블_1 = new OrderTable(테이블단체_신규_테이블1_ID, 테이블단체_신규_ID, 0, false);
	public static final OrderTable 테이블단체_테이블_2 = new OrderTable(테이블단체_신규_테이블2_ID, 테이블단체_신규_ID, 0, false);

	public static final TableGroup 테이블단체_신규_WITH_테이블목록 = new TableGroup(테이블단체_신규_ID
		, 테이블단체_신규_CREATED_DATE
		, Arrays.asList(테이블단체_테이블_1, 테이블단체_테이블_2));

	//Order
	public static final Long 주문_신규_ID = 1L;
	public static final Long 주문_신규_테이블_ID = 9L;
	public static final String 주문_신규_주문상태 = OrderStatus.COOKING.name();
	public static final LocalDateTime 주문_신규_ORDERED_DATE = LocalDateTime.of(2021, 1, 28, 19, 30);
	public static final Order 주문_신규 = new Order(주문_신규_ID, 주문_신규_테이블_ID, 주문_신규_주문상태, 주문_신규_ORDERED_DATE);

	public static final Long 주문_신규_메뉴1_ID = 1L;
	public static final Long 주문_신규_메뉴2_ID = 2L;
	public static final OrderLineItem 주문_신규_후라이드_1개 = new OrderLineItem(주문_신규_ID, 주문_신규_메뉴1_ID, 1);
	public static final OrderLineItem 주문_신규_양념_2개 = new OrderLineItem(주문_신규_ID, 주문_신규_메뉴2_ID, 2);
	public static final Order 주문_신규_WITH_주문아이템목록 = new Order(주문_신규_ID
		, 주문_신규_테이블_ID
		, 주문_신규_주문상태
		, 주문_신규_ORDERED_DATE
		, Arrays.asList(주문_신규_후라이드_1개, 주문_신규_양념_2개));
}
