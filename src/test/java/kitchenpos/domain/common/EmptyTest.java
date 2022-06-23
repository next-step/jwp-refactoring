package kitchenpos.domain.common;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EmptyTest {
    @Test
    void equalityTest() {
        Empty source = new Empty(true);
        Empty target = new Empty(true);
        assertEquals(source, target);
    }
}
