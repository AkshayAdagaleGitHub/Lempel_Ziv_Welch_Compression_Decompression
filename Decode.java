import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Name : Akshay Adagale
 * ID   : 800987050
 * */


public class Decode {

	//Decode function implements the LZW decoder Algorithm
	private String decodeFile(String encodedFileName, int bit) throws Exception{

		double maxSize = Math.pow(2, bit);

		//InputStream and Reader to read the encoded file
		InputStream fileEncoded = new FileInputStream(encodedFileName);
		Reader reader = new InputStreamReader(fileEncoded,"UTF-16BE");
		Reader br = new BufferedReader(reader);
		int tableSize = 255;

		List<Integer> encodedList = new ArrayList<Integer>();
		double fileData = 0;
		while((fileData = br.read())!=-1){
			encodedList.add((int)fileData);
		}
		br.close();

		//Map to store the Character in a HashMap and then compare it to check the key or Value
		Map<Integer, String> charTable = new HashMap<Integer,String>();
		for(int i =0; i<=255;i++){
			charTable.put( i, "" + (char)i);
		}
		//Implementing the LZW algorithm
		String encodeValue = encodedList.get(0).toString();
		StringBuffer decodedStringBuffer = new StringBuffer();

		for (int key:encodedList) {

			String value = "";
			if (charTable.containsKey(key))
			{	//Storing the string from the HashMap to value
				value = charTable.get(key);
			}
			else if (key == tableSize)
			{//Append if key==tablesize to value
				value = encodeValue + encodeValue.charAt(0);
			}
			decodedStringBuffer.append(value);

			if(tableSize < maxSize )
			{	//If not present then checking tablesize with maxsize and adding into the HashMap
				charTable.put(tableSize++, encodeValue + value.charAt(0));
			}
			encodeValue = value;
		}
		//Return the decodedStringBuffer which contains the decoded values
		return decodedStringBuffer.toString();
	}

	//Function to create and write the output in a _Decoded.txt file
	private void createDecodedFile(String decodedValues, String encodedFileName) throws Exception {


		String decodedFileName = encodedFileName.substring(0, encodedFileName.lastIndexOf(".")) + "_decoded.txt";
		//FileWriter and BufferedWriter to write and it overwrites into the file.
		FileWriter fileWriter = new FileWriter(decodedFileName, false);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		bufferedWriter.write(decodedValues);
		//Flush and Close the bufferedWriter
		bufferedWriter.flush();
		bufferedWriter.close();	
	}

	//Main method
	public static void main(String[] args) throws Exception {
		
		String encodedFileName;
		int bit=0;
		
		//Validation to Check for empty and Null inputs
		if(!args[0].isEmpty() || !args[1].isEmpty() || args[0] == null || args[1] == null){
			encodedFileName= args[0];
		try{
				bit = Integer.parseInt(args[1]);
		}catch(NumberFormatException nfe){
				System.out.println("Please Enter an Integer" + nfe);
		}
		}else{
			return;
		}
		
		//Creating the decoder class object
		Decode decoder = new Decode();
		String decodedString = decoder.decodeFile(encodedFileName, bit);
		decoder.createDecodedFile(decodedString, encodedFileName);
	}

}
