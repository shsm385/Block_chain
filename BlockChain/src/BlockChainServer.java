import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.security.NoSuchAlgorithmException;

public class BlockChainServer{

	Blockchain bl = new Blockchain();
	public int send(String sender, String recipient, int amount){
		return bl.createTransaction(sender,recipient,amount);
	}
	
	// 採掘処理
	public String mine(String recipient) throws NoSuchAlgorithmException{
		JSONObject lastblock = bl.lastBlock();

		int proof = 0;
		int lastproof = 0;
		if(lastblock != null) {
			lastproof = (int)lastblock.get("proof");
			proof = bl.proofWork(lastproof);
		}
		bl.createTransaction("0",recipient,1);

		String hash = Blockchain.hash(lastblock);

		JSONObject block = bl.createBlock(lastproof, hash);

		JSONObject response = new JSONObject();
		response.put("message","Mined new block");
		response.put("index", block.get("index"));
		response.put("transactions", block.get("transaction"));
		response.put("proof", block.get("proof"));
		response.put("previous_hash", block.get("previous_hash"));

		return response.toString();
	}


	public JSONArray chain(){
		return bl.chain;
	}
}
