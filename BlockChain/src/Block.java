import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

class Block{
  int index; //インデックス
  Double timestamp;　// timestamp
  JSONArray transaction; // transaction
  int proof; // proof
  String previousHash; // 以前のブロックのハッシュ
}
