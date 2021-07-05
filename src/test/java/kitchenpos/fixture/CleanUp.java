package kitchenpos.fixture;

public class CleanUp {
    public static void cleanUpOrderFirst() {
        commonCleanUp();
        OrderFixture.cleanUp();
        OrderTableFixture.cleanUp();
    }

    public static void cleanUpTableFirst() {
        commonCleanUp();
        OrderTableFixture.cleanUp();
        OrderFixture.cleanUp();
    }

    private static void commonCleanUp() {
        MenuGroupFixture.cleanUp();
        TableGroupFixture.cleanUp();
        ProductFixture.cleanUp();
        MenuFixture.cleanUp();
        OrderLineItemFixture.cleanUp();
    }
}
