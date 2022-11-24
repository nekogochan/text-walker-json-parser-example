package nekogochan.example.jsonparser.util;

import nekogochan.textwalker.charautomate.CharAutomate.MatchRule;

import static nekogochan.textwalker.util.CharAutomateRules.currentIs;

public class JsonCharAutomateRules {
  private JsonCharAutomateRules() {}

  public static MatchRule currentIsWhitespace() {
    return currentIs(JsonStandard::isWhitespace);
  }

  public static MatchRule currentIsDigit() {
    return currentIs(JsonStandard::isDigit);
  }
}
