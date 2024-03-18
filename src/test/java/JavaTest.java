import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
public class JavaTest {
    @Test
    void test() {
        System.out.println(resolveEsignPrivId(1));
    }


    private Object resolveEsignPrivId(int apprLevel) {
        return String.format("E-SIGN.%02d", apprLevel);
    }
}
