import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Blockchain{
	// ブロックチェーンを収めるための空のリスト
	JSONArray chain = new JSONArray();

	// トランザクションを収めるための空のリスト
	JSONArray current_transactions = new JSONArray();


	// ブロックチェーンに新しいブロックを作る
	public JSONObject createBlock(int proof, String previous_hash) throws JSONException, NoSuchAlgorithmException{
		JSONObject block = new JSONObject();

		block.put("timestamp", new Timestamp(System.currentTimeMillis()));
		block.put("proof",proof);
		block.put("transaction",this.current_transactions);
		block.put("previous_hash",previous_hash != null ? previous_hash :
			this.chain.length() == 0 ? "0" : hash((JSONObject)this.chain.get(this.chain.length()-1)));
		block.put("index", this.chain.length() + 1);
		this.current_transactions = new JSONArray();
		this.chain.put(block);

		return block;
	}

	// 次に採掘されるブロックに加える新しいトランザクションを作る
	public int createTransaction(String sender, String recipient, int amount){
		JSONObject transaction = new JSONObject();

		transaction.put("sender",sender);
		transaction.put("recipient",recipient);
		transaction.put("amount",amount);
		this.current_transactions.put(transaction);
		if(lastBlock() == null){
			return 0;
		}
		return (int)lastBlock().get("index") + 1;
	}

	// チェーンの最後のブロックをリターンする
	public JSONObject lastBlock(){
		if(this.chain.length() == 0){
			return null;
		}
		return (JSONObject)this.chain.get(chain.length()-1);
	}

	// ブロックをハッシュ化する 
	public static String hash(JSONObject block) throws NoSuchAlgorithmException{
		if(block == null){
			return null;
		}
		String bstring = block.toString();
		String result = hashLib(bstring);
		return result;
	}

	// SHA-256 ハッシュの作成
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
	// hash の4つが0となるようなproofを探す
	public int proofWork(int lastproof) throws NoSuchAlgorithmException{
		int proof = 0;
		while (valid_proof(lastproof, proof) == false){
			proof++;
		}
		return proof;
	}

	// プルーフが正しいかの確認
	public static boolean valid_proof(int lastproof, int proof) throws NoSuchAlgorithmException{
		String guess = String.valueOf(lastproof) + String.valueOf(proof);
		String guesshash = hashLib(guess);
		return "0000".equals(guesshash.substring(0,4));
	}
}
