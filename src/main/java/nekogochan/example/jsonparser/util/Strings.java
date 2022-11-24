package nekogochan.example.jsonparser.util;

import static java.lang.Math.max;
import static java.lang.Math.min;

public interface Strings {

  record StringWithOffset(int idxPos, String str) {
  }

  static StringWithOffset atMiddleWithOffset(String x, int idx, int offset) {
    var s = new StringBuilder();
    var startPos = max(idx - offset, 0);
    var endPos = min(idx + offset, x.length() - 1);
    for (int i = startPos; i <= endPos; i++) {
      s.append(x.charAt(i));
    }
    return new StringWithOffset(min(offset, idx - startPos), s.toString());
  }
}
