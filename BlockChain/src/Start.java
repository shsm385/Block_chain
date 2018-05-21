import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;

import javax.swing.*;

public class Start{
	private static int flag=0;

	public static void main(String[] args) throws NoSuchAlgorithmException{
		JFrame frame = new JFrame("Block chain");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(125, 125);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new FlowLayout());

		JButton button1 = new JButton("採掘");
		JButton button2 = new JButton("チェーン全体");
		BlockChainServer bls = new BlockChainServer();



		// ボタンを押した時の処理を設定
		button1.addActionListener(e -> {
			String block;
			try {
				block = bls.mine("aaa");
				//System.out.println("New Block Forged");
				if(flag == 0) {
					System.out.println("chain :");
					System.out.println(block);
					flag++;
				}else {
					System.out.println(block);
				}
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
		button2.addActionListener(e -> {
			String chain = bls.chain().toString();
			System.out.println("チェーン全体");
			System.out.println(chain);
		});
		frame.add(button1);
		frame.add(button2);
		frame.setVisible(true);


	}
}
