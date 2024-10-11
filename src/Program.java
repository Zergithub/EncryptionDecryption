import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Program {

	final static String TEMP_ENCODING_CODE = "ZYXWVUTSRQPONMLKJIHGFEDCBA";

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());

		// set the top panel where inputs are taken from user
		JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Input"));

		// add substitution text fields
		inputPanel.add(new JLabel("Enter substitution Code"));
		JTextField substitutionCode = new JTextField(26);
		inputPanel.add(substitutionCode);

		// add text fields
		inputPanel.add(new JLabel("Enter text"));
		JTextField textField = new JTextField(20);
		inputPanel.add(textField);

		// add buttons
		JButton encode = new JButton("Encrypt");
		inputPanel.add(encode);

		JButton decode = new JButton("Decrypt");
		inputPanel.add(decode);

		// add north panel to the frame
		frame.add(inputPanel, BorderLayout.NORTH);

		// this panel will show result
		JPanel outputPanel = new JPanel(new BorderLayout());
		outputPanel.setBorder(BorderFactory.createTitledBorder("Result"));

		JTextArea resultText = new JTextArea(10, 15);
		resultText.setEditable(false);
		outputPanel.add(new JScrollPane(resultText), BorderLayout.CENTER);

		// when encode button is clicked
		encode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String substitutionString = substitutionCode.getText();
				String text = textField.getText();

				resultText.setText(encodeString(text, substitutionString));
			}
		});
		
		// when decode button is clicked
		decode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String substitutionString = substitutionCode.getText();
				String text = resultText.getText();

				resultText.setText(encodeString(text, reverseCodeString(substitutionString)));
			}
		});

		frame.add(outputPanel, BorderLayout.CENTER);
		frame.setTitle("Encrypter/Decrypter");
		frame.setSize(900, 200);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	/**
	 * 
	 * @param ch character to check
	 * @return true if given character is alphabet, returns false otherwise
	 */
	public static boolean checkCodeCharacter(char ch) {
		return Character.isAlphabetic(ch);
	}

	/**
	 * 
	 * validates if the given encoded string is valid or not a valid encoded string
	 * must have 1) Length of 26 2) No repeating letters
	 * 
	 * @param encodedString to validate
	 * @return true if encoded string is valid, false otherwise
	 */
	public static boolean isValidEncodeString(String encodedString) {
		// check if length is correct
		if (encodedString.length() != 26) {
			return false;
		}
		encodedString = encodedString.toUpperCase();
		// check if there are repeating characters
		for (int i = 0; i < encodedString.length() - 1; i++) {
			for (int j = i + 1; j < encodedString.length(); j++) {
				if (encodedString.charAt(j) == encodedString.charAt(i)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Encodes the given message by using the encodingString given as input as well
	 * 
	 * @param message         to encode
	 * @param encodingMessage the substitution code to be used to encode message
	 * @return the encoded message
	 */
	public static String encodeString(String message, String encodingMessage) {
		String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		message = message.toUpperCase();
		String encoded = "";
		// check each character
		for (int i = 0; i < message.length(); i++) {
			// check if the characters is alphabet
			if (checkCodeCharacter(message.charAt(i))) {
				int position = alphabets.indexOf(message.charAt(i));
				encoded += encodingMessage.charAt(position);
			} else {
				// current characters is not alphabet, so add it as it is
				encoded += message.charAt(i);
			}
		}
		return encoded;
	}

	/**
	 * creates corresponding substitution code to be used to decode the message
	 * 
	 * @param substitutionCode to be reversed
	 * @return reversed substitution code
	 */
	public static String reverseCodeString(String substitutionCode) {
		String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		String reversedEncodedString = "";

		for (int i = 0; i < substitutionCode.length(); i++) {
			// reverse the positions
			int position = substitutionCode.indexOf(alphabets.charAt(i));
			reversedEncodedString += alphabets.charAt(position);
		}

		return reversedEncodedString;
	}

	
	/**
	 * counts frequency of each character in given string returns string such that
	 * first characters is most repeated and last character is least repeated
	 * 
	 * @param text
	 * @return the result string
	 */
	public static String computeCodeString(String text) {
		TreeMap<Character, Integer> map = new TreeMap<>();

		//calculates the frequency of each character
		for (int i = 0; i < text.length(); i++) {
			Character ch = text.charAt(i);
			if (!map.containsKey(ch)) {
				map.put(ch, 0);
			} else {
				map.put(ch, map.get(ch) + 1);
			}
		}
		//sorts the list based on the frequency or values
		List<Entry<Character, Integer>> values = map.entrySet().stream().sorted(Entry.comparingByValue())
				.collect(Collectors.toList());
		
		//converts the list to string
		String result = "";
		for (int i = values.size() - 1; i >= 0; i--) {
			if (checkCodeCharacter(values.get(i).getKey())) {
				result += values.get(i).getKey();
			}
		}

		return result;
	}

}
