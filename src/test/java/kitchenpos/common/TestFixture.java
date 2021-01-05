package kitchenpos.common;

import java.util.Collections;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableRequest;

public class TestFixture {
	public static final String 예제_메뉴그룹명 = "인도요리";
	public static final String 예제_상품명 = "피자";
	public static final int 예제_상품_가격 = 15000;
	public static final long 주문대상_테이블ID = 7L;
	public static final long 빈_테이블ID = 1L;
	public static final long 존재하지_않는_테이블ID = 999L;
	public static final long 예제_테이블_그룹_ID = 1L;
	public static final long 조리상태인_테이블그룹_ID = 2L;
	public static final int 후라이드_가격 = 16000;
	public static final int 양념치킨_가격 = 16000;
	public static final int 예제_메뉴_가격 = 20000;
	public static final Long 예제_메뉴_그룹_ID = 1L;
	public static final Long 메뉴_그룹_ID_없음 = null;
	public static final String 예제_메뉴명 = "치킨 선물 박스";
	public static final long 예제테이블1_ID = 1L;
	public static final long 예제테이블2_ID = 2L;
	public static final long 비어있지않은테이블_ID = 7L;
	public static final long 단체지정되어있지만_주문없는_테이블_ID = 5L;
	public static final long 존재하지않는테이블_ID = 999L;

	public static final OrderTableRequest 존재하지않는테이블 = TestDataUtil.createOrderTableById(존재하지않는테이블_ID);
	public static final OrderTableRequest 빈테이블 = TestDataUtil.createOrderTableById(1L);
	public static final OrderTableRequest 단체지정_테이블 = TestDataUtil.createOrderTableById(5L);
	public static final OrderTableRequest 주문상태_조리인_테이블 = TestDataUtil.createOrderTableById(7L);
	public static final OrderTableRequest 주문상태_식사인_테이블 = TestDataUtil.createOrderTableById(8L);
	public static final Long 일반_메뉴1_ID = 1L;
	public static final Long 일반_메뉴2_ID = 2L;
	public static final Long 존재하지않은메뉴_ID = 999L;
	public static final OrderRequest 조리상태_주문 = TestDataUtil.createOrderByIdAndStatus(1L, OrderStatus.COOKING);
	public static final OrderRequest 식사상태_주문 = TestDataUtil.createOrderByIdAndStatus(2L, OrderStatus.MEAL);
	public static final OrderRequest 완료상태_주문 = TestDataUtil.createOrderByIdAndStatus(5L, OrderStatus.COMPLETION);
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
}
