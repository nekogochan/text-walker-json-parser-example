package nekogochan.example.jsonparser.util;

public class JsonStandard {
  private JsonStandard() {}

  public static boolean isWhitespace(char ch) {
    return ch == 0x20 ||
           ch == 0x0D ||
           ch == 0x0A ||
           ch == 0x09;
  }

  public static boolean isValidAfterToken(char x) {
    return isWhitespace(x) ||
           x == ',' ||
           x == ']' ||
           x == '}';
  }

  public static boolean isDigit(char ch) {
    return ch >= '0' && ch <= '9';
  }
}
