package kitchenpos.fixture;

public class CleanUp {
    public static void cleanUp() {
        MenuGroupFixture.cleanUp();
        TableGroupFixture.cleanUp();
        ProductFixture.cleanUp();
        MenuFixture.cleanUp();
        OrderLineItemFixture.cleanUp();
        OrderTableFixture.cleanUp();
        OrderFixture.cleanUp();
    }
}
