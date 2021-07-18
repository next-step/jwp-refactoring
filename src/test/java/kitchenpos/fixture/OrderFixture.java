package kitchenpos.fixture;

import kitchenpos.domain.Order;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목_감자튀김_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목_후라이드_치킨_생성;
import static kitchenpos.fixture.OrderTableFixture.*;

public class OrderFixture {
    private static Supplier<LocalDateTime> currentDateTimeSupplier = LocalDateTime::now;
    public static Order 주문_후라이드_한마리_감자튀김_조리중
            = new Order(1L,
            주문_테이블_조리_중인_주문_테이블.getId(),
            COOKING.name(),
            currentDateTimeSupplier.get(),
            asList(주문_항목_감자튀김_생성.apply(1L, 1L), 주문_항목_후라이드_치킨_생성.apply(1L, 1L)));

    public static Order 주문_후라이드_한마리_감자튀김_식사중
            = new Order(2L,
            주문_테이블_식사_중인_주문_테이블.getId(),
            MEAL.name(),
            currentDateTimeSupplier.get(),
            asList(주문_항목_감자튀김_생성.apply(1L, 1L), 주문_항목_후라이드_치킨_생성.apply(1L, 1L)));

    public static Order 주문_후라이드_한마리_감자튀김_계산완료
            = new Order(3L,
            주문_테이블_계산_완료된_주문_테이블.getId(),
            COMPLETION.name(),
            currentDateTimeSupplier.get(),
            asList(주문_항목_감자튀김_생성.apply(1L, 1L), 주문_항목_후라이드_치킨_생성.apply(1L, 1L)));

    public static Order 주문_2번_테이블_그룹_1번_테이블_후라이드_감자_튀김
            = new Order(4L,
            주문_테이블_2번_테이블_그룹에_속한_1번쨰_테이블.getId(),
            COMPLETION.name(),
            currentDateTimeSupplier.get(),
            asList(주문_항목_감자튀김_생성.apply(1L, 1L), 주문_항목_후라이드_치킨_생성.apply(1L, 1L)));

    public static Order 주문_2번_테이블_그룹_2번_테이블_후라이드_감자_튀김
            = new Order(5L,
            주문_테이블_2번_테이블_그룹에_속한_2번쨰_테이블.getId(),
            COMPLETION.name(),
            currentDateTimeSupplier.get(),
            asList(주문_항목_감자튀김_생성.apply(1L, 1L), 주문_항목_후라이드_치킨_생성.apply(1L, 1L)));
}
