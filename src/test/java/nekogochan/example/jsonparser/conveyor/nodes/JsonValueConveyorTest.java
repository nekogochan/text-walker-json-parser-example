package nekogochan.example.jsonparser.conveyor.nodes;

import nekogochan.example.jsonparser.conveyor.JsonConveyor;
import nekogochan.example.jsonparser.exception.InvalidJsonValueException;
import nekogochan.example.jsonparser.model.JsonValue;
import nekogochan.textwalker.charautomate.CharArrayAutomate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonValueConveyorTest {

  private static JsonValueConveyor conveyor() {
    return new JsonConveyor().forValue();
  }

  private static JsonValue parse(String raw) {
    var auto = new CharArrayAutomate(raw);
    return conveyor().parse(auto);
  }

  @Test
  void testString() {
    var raw = "\"some data\"";
    var data = parse(raw);
    assertEquals("some data", data.asStr());
  }

  @ParameterizedTest
  @CsvSource({
    "123    o , 123",
    "4342   o , 4342",
    "123123 o , 123123",
  })
  void testIntegers(String raw, int expected) {
    var val = parse(raw);
    assertEquals(expected, val.asInt());
  }

  @ParameterizedTest
  @CsvSource({
    "123.123 o , 123.123",
    "10e2    o , 1000.0",
    "10.1e2  o , 1010.0"
  })
  void testDoubles(String raw, double expected) {
    var val = parse(raw);
    assertEquals(expected, val.asDouble());
  }

  @ParameterizedTest
  @CsvSource({
    "false o , false",
    "true  o , true"
  })
  void testBooleans(String raw, boolean expected) {
    var val = parse(raw);
    assertEquals(expected, val.asBoolean());
  }

  @Test
  void testNull() {
    var raw = "null o";
    var val = parse(raw);
    assertNull(val.value());
  }

  @Test
  void skipColonAfterParseStringTest() {
    var raw = "\"some data\"    ";
    var auto = new CharArrayAutomate(raw);
    var conveyor = conveyor();
    var data = conveyor.parse(auto);
    assertEquals("some data", data.asStr());
    assertEquals(' ', auto.current());
    assertEquals(11, auto.currentPos());
  }

  @ParameterizedTest
  @CsvSource({
    "null",
    "false",
    "true",
    "123",
    "123.123",
    "123.123e123",
  })
  void regularJsonValuesNotSupported_forNonStringValues(String raw) {
    assertThrows(InvalidJsonValueException.class, () -> parse(raw));
  }

  @ParameterizedTest
  @CsvSource(delimiter = '|', value = {
    "123,",
    "123]",
    "123}"
  })
  void parseIfItsBeforeCommaOrEndOfObjectOrArray(String raw) {
    var auto = new CharArrayAutomate(raw);
    var conveyor = new JsonConveyor().forValue();
    var val = conveyor.parse(auto);
    assertEquals(123, val.value());
    assertEquals(3, auto.currentPos());
  }
}