import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Blockchain{
  JSONArray chain = new JSONArray();
  JSONArray current_transactions = new JSONArray();

  public Blockchain(){
    createBlock(100,"1");
  }

  // 新しいブロックを作り、チェーンに加える
  public JSONObject createBlock(int proof, String previousHash){
    JSONObject block = new JSONObject();
    block.put("index", this.chain.length() + 1);
    block.put("timestamp", new Timestamp(System.currentTimeMillis()));
    block.put("transaction",this.current_transactions);
    block.put("proof",proof);

    this.current_transactions = new JSONArray();
    this.chain.put(block);

    return block;
  }

  // 新しいトランザクションをリストに加える
  public int createTransaction(String sender, String recipient, int amount){
    JSONObject transaction = new JSONObject();

    transaction.put("sender",sender);
    transaction.put("recipient",recipient);
    transaction.put("amount",amount);

    if(lastBlock() == 0){
      return 1;
    }

    return lastBlock() + 1;
  }

  // チェーンの最後のブロックをリターンする
  public int lastBlock(){
    if(this.chain.length() == 0){
      return 0;
    }
    return (int)((JSONObject)this.chain.get(chain.length()-1)).get("index");
  }

  // ブロックをハッシュ化する
  public static String hash(JSONObject block) throws NoSuchAlgorithmException{
    String bstring = block.toString();
    String result = hashLib(bstring);
    return result;
  }

  public static String hashLib(String bstring) throws NoSuchAlgorithmException{
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    Charset charset = StandardCharsets.UTF_8;

    md.update(bstring.getBytes(charset));
    byte[] c = md.digest();
    StringBuilder result = new StringBuilder(2 * c.length);
		for(byte b : c){
			result.append(String.format("%02x", b&0xff));
    }

    return result.toString();
  }

  // proof のアルゴリズム
  public int proofWork(int lastproof) throws NoSuchAlgorithmException{
    int proof = 0;
		while (valid_proof(lastproof, proof) == false){
			proof++;
		}
    return proof;
  }

  public static boolean valid_proof(int lastproof, int proof) throws NoSuchAlgorithmException{
		String guess = String.valueOf(lastproof)+String.valueOf(proof);
		String guesshash = hashLib(guess);
		return "0000".equals(guesshash.substring(0,4));
  }
}
