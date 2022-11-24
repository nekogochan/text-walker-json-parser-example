package nekogochan.example.jsonparser.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

  @ParameterizedTest
  @CsvSource({
    "I-love-my-cat , 3 , 2 , -love , 2",
    "0123456789    , 5 , 2 , 34567 , 2",
    "0123456789    , 1 , 2 , 0123  , 1",
    "0123456789    , 8 , 2 , 6789  , 2",
    "0123456789    , 0 , 2 , 012   , 0",
    "0123456789    , 9 , 2 , 789   , 2",
  })
  void atMiddleWithOffset(String src, int idx, int offset, String expectedResult, int expectedIdxPos) {
    var result = Strings.atMiddleWithOffset(src, idx, offset);
    assertEquals(expectedResult, result.str());
    assertEquals(expectedIdxPos, result.idxPos());
  }
}