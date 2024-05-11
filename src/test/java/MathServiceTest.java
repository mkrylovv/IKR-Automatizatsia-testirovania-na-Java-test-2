import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class MathServiceTest {

    static MathService mathService;

    @BeforeAll
    static void init() {
        mathService = new MathService();
    }

    @Test
    void testPairToString() {
        Pair pair = new Pair(1.4568, -0.78);
        Assertions.assertEquals("Answer{first=1.4568, second=-0.78}", pair.toString());
    }

    @ParameterizedTest
    @CsvSource({"2, 4, 2, 0", "2, -4, 2, 0", "1, 3, -4, 25", "-1, 3, 4, 25",
            "-10, -10, -10, -300", "1, 5, 1, 21"})
    void testGetD(int a, int b, int c, int result) {
        int d = mathService.getD(a, b, c);
        Assertions.assertEquals(result, d);
    }

    @ParameterizedTest
    @CsvSource({"1, -7, 12, 4.0, 3.0", "2, 5, 3, -1.0, -1.5"})
    void testGetAnswerDiscriminantOverZero(int a, int b, int c, double x1, double x2) throws NotFoundAnswerException {
        Pair pair = mathService.getAnswer(a, b, c);
        String result = "Answer{first=" + x1 + ", second=" + x2 + "}";
        Assertions.assertAll(() -> Assertions.assertEquals(x1, pair.first),
                () ->Assertions.assertEquals(x2, pair.second),
                () ->Assertions.assertEquals(result, pair.toString()));
    }

    @ParameterizedTest
    @CsvSource({"2, 4, 2, -1.0", "16, -8, 1, 0.25"})
    void testGetAnswerDiscriminantEqualZero(int a, int b, int c, double x) throws NotFoundAnswerException {
        Pair pair = mathService.getAnswer(a, b, c);
        String result = "Answer{first=" + x + ", second=" + x + "}";
        System.out.println(pair.toString());
        Assertions.assertAll(() -> Assertions.assertEquals(x, pair.first),
                () ->Assertions.assertEquals(x, pair.second),
                () ->Assertions.assertEquals(result, pair.toString()));
    }

    @Test
    void testExceptionInGetAnswerDiscriminantLessZero() {
        String message = "";
        Assertions.assertThrows(NotFoundAnswerException.class, () -> mathService.getAnswer(1, 1, 1));
        try {
            Pair pair = mathService.getAnswer(8, 2, 1);
        } catch (NotFoundAnswerException e) {
            message = e.getMessage();
        }
        Assertions.assertEquals("Корни не могут быть найдены", message);
    }

    @Test
    void testGetExceptionInGetAnswerWithAEqualZero() throws NotFoundAnswerException {
        Assertions.assertThrows(NotFoundAnswerException.class, () -> mathService.getAnswer(0, 5, 1));
    }

    @Test
    void testGetExceptionInGetAnswerWithIntOverflow() {
        Assertions.assertThrows(NotFoundAnswerException.class, () -> mathService.getAnswer(2147483647+1, 5, 1));
    }

}
