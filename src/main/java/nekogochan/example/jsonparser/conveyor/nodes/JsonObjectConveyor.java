package nekogochan.example.jsonparser.conveyor.nodes;

import nekogochan.example.jsonparser.conveyor.JsonConveyor;
import nekogochan.example.jsonparser.exception.InvalidJsonObjectException;
import nekogochan.example.jsonparser.model.JsonNode;
import nekogochan.example.jsonparser.model.JsonObject;
import nekogochan.textwalker.charautomate.CharAutomate;

import java.util.ArrayList;
import java.util.List;

import static nekogochan.example.jsonparser.util.JsonCharAutomateRules.currentIsWhitespace;
import static nekogochan.textwalker.util.CharAutomateRules.currentEq;

public final class JsonObjectConveyor implements JsonElementConveyor<JsonObject> {
  JsonConveyor jsonConveyor;

  public JsonObjectConveyor(JsonConveyor jsonConveyor) {
    this.jsonConveyor = jsonConveyor;
  }

  @Override
  public JsonObject parse(CharAutomate auto) {
    try {
      return new JsonObject(internalParse(auto));
    } catch (IndexOutOfBoundsException ex) {
      throw new InvalidJsonObjectException(auto, "Unexpected end of json object, expects '}'");
    }
  }

  public List<JsonObject.Entry> internalParse(CharAutomate auto) {
    if (auto.current() != '{') {
      throw new InvalidJsonObjectException(auto, "Expects '{'");
    }
    var obj = new ArrayList<JsonObject.Entry>();
    auto.goForward();
    parseEntries(auto, obj);
    auto.goForward();
    return obj;
  }

  private void parseEntries(CharAutomate auto, ArrayList<JsonObject.Entry> obj) {
    auto.skipWhile(currentIsWhitespace());
    if (auto.current() == '}') {
      return;
    }
    while (true) {
      var key = extractKey(auto);
      goToValue(auto);
      var val = extractValue(auto);
      obj.add(new JsonObject.Entry(key, val));
      auto.skipWhile(currentIsWhitespace());
      if (auto.current() == ',') {
        auto.goForward();
        auto.skipWhile(currentIsWhitespace());
        continue;
      }
      if (auto.current() == '}') {
        break;
      }
      throw new InvalidJsonObjectException(auto, "Unexpected token");
    }
  }

  private static String extractKey(CharAutomate auto) {
    if (auto.current() != '"') {
      throw new InvalidJsonObjectException(auto, "Unexpected token before key parsing, expects '\"'");
    }
    auto.goForward();
    var key = auto.takeUntil(currentEq('"')).toString();
    auto.goForward();
    return key;
  }

  private static void goToValue(CharAutomate auto) {
    auto.skipWhile(currentIsWhitespace());
    if (auto.current() != ':') {
      throw new InvalidJsonObjectException(auto, "Unexpected token after value parsing, expects ':'");
    }
    auto.goForward();
    auto.skipWhile(currentIsWhitespace());
  }

  private JsonNode extractValue(CharAutomate auto) {
    var elementConveyor = jsonConveyor.suitableConveyor(auto.current());
    if (elementConveyor == null) {
      throw new InvalidJsonObjectException(auto, "Unexpected token on value parsing");
    }
    return elementConveyor.parse(auto);
  }
}
