package nekogochan.example.jsonparser.conveyor.nodes;

import nekogochan.example.jsonparser.conveyor.JsonConveyor;
import nekogochan.example.jsonparser.exception.InvalidJsonValueException;
import nekogochan.example.jsonparser.model.JsonValue;
import nekogochan.example.jsonparser.util.JsonStandard;
import nekogochan.textwalker.charautomate.CharAutomate;

import static nekogochan.example.jsonparser.util.JsonCharAutomateRules.currentIsDigit;
import static nekogochan.example.jsonparser.util.JsonCharAutomateRules.currentIsWhitespace;
import static nekogochan.example.jsonparser.util.JsonStandard.isDigit;
import static nekogochan.example.jsonparser.util.JsonStandard.isValidAfterToken;
import static nekogochan.example.jsonparser.util.JsonStandard.isWhitespace;
import static nekogochan.textwalker.util.CharAutomateRules.currentEq;

public final class JsonValueConveyor implements JsonElementConveyor<JsonValue> {
  private final JsonConveyor jsonConveyor;

  public JsonValueConveyor(JsonConveyor jsonConveyor) {
    this.jsonConveyor = jsonConveyor;
  }

  @Override
  public JsonValue parse(CharAutomate auto) {
    try {
      return new JsonValue(internalParse(auto));
    } catch (IndexOutOfBoundsException ex) {
      throw new InvalidJsonValueException(auto, "Unexpected end of JSON");
    }
  }

  public Object internalParse(CharAutomate auto) {
    return switch (auto.current()) {
      case '"' -> parseString(auto);
      case 't' -> parseTrue(auto);
      case 'f' -> parseFalse(auto);
      case 'n' -> parseNull(auto);
      case '-' -> parseNumber(auto, true);
      default -> {
        if (isDigit(auto.current())) {
          yield parseNumber(auto, false);
        }
        throw new InvalidJsonValueException(auto, "Unexpected symbol");
      }
    };
  }

  private Object parseString(CharAutomate auto) {
    auto.goForward();
    try {
      var sb = auto.takeUntil(currentEq('"'));
      auto.goForward();
      return sb.toString();
    } catch (IndexOutOfBoundsException ex) {
      throw new InvalidJsonValueException(auto, "Expects '\"' while parsing string");
    }
  }

  private boolean parseTrue(CharAutomate auto) {
    var sb = auto.takeUntil(currentIsWhitespace()).toString();
    if (sb.equals("true")) {
      return true;
    } else {
      throw new InvalidJsonValueException(auto, "Unexpected token, expected 'true'");
    }
  }

  private boolean parseFalse(CharAutomate auto) {
    var sb = auto.takeUntil(currentIsWhitespace()).toString();
    if (sb.equals("false")) {
      return false;
    } else {
      throw new InvalidJsonValueException(auto, "Unexpected token, expected 'true'");
    }
  }

  private Object parseNull(CharAutomate auto) {
    var sb = auto.takeUntil(currentIsWhitespace()).toString();
    if (sb.equals("null")) {
      return null;
    } else {
      throw new InvalidJsonValueException(auto, "Unexpected token, expected 'true'");
    }
  }

  private Object parseNumber(CharAutomate auto, boolean negate) {
    if (negate) {
      auto.goForward();
    }
    var whole = auto.takeWhile(currentIsDigit())
                    .toString();
    if (isValidAfterToken(auto.current())) {
      var n = Integer.parseInt(whole);
      return negate ? -n : n;
    }
    return switch (auto.current()) {
      case '.' -> parseDouble(auto, negate, whole);
      case 'e', 'E' -> parseDoubleWithE(auto, negate, whole);
      default -> {throw new InvalidJsonValueException(auto, "Unexpected token, while parsing number");}
    };
  }

  private double parseDouble(CharAutomate auto, boolean negate, String whole) {
    auto.goForward();
    var fract = auto.takeWhile(currentIsDigit()).toString();
    var mantissa = whole + '.' + fract;
    if (isWhitespace(auto.current())) {
      var n = Double.parseDouble(mantissa);
      return negate ? -n : n;
    }
    if (auto.current() == 'e' || auto.current() == 'E') {
      auto.goForward();
      var expo = auto.takeWhile(currentIsDigit());
      var n = Double.parseDouble(mantissa + "e" + expo);
      return negate ? -n : n;
    }
    throw new InvalidJsonValueException(auto, "Unexpected token, while parsing number");
  }

  private static double parseDoubleWithE(CharAutomate auto, boolean negate, String whole) {
    auto.goForward();
    var expo = auto.takeWhile(currentIsDigit());
    var n = Double.parseDouble(whole + "e" + expo);
    return negate ? -n : n;
  }
}
