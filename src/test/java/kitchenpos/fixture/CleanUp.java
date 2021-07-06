package kitchenpos.fixture;

public class CleanUp {
    public static void cleanUp() {
        MenuGroupFixture.cleanUp();
        TableGroupFixture.cleanUp();
        ProductFixture.cleanUp();
        try {
            MenuFixture.cleanUp();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        OrderLineItemFixture.cleanUp();
        OrderTableFixture.cleanUp();
        OrderFixture.cleanUp();
    }
}
