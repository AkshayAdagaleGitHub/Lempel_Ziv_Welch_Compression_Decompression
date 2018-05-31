import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Name : Akshay Adagale
 * ID   : 800987050
 * */

public class Encoder {

	/*This Function implements the LZW Encoder algorithm */
	private List<Integer> encodeData(String inputString, int bit) throws FileNotFoundException {

		//Map to store the character and their values in a HashMap to check for key and value
		Map<String, Integer> charTable = new HashMap<String, Integer>();
		List<Integer> encodedArrayList = new ArrayList<Integer>();//List to store the encoded values
		double maxSize = Math.pow(2, bit);//Sets the maximum size of the table
		String str ="";
		double tableSize = 255;//Sets the max size from 0-255

		for (int i = 0; i <= 255; i++) {
			charTable.put("" + (char) i, i);
		}
		//Implementing the LZW Encoding algorithm
		for (char a : inputString.toCharArray()) {
			String word = str + a;
			if (charTable.containsKey(word))/*Check if the HashMap contains the character*/ {
				str = word;
			} else {
				encodedArrayList.add(charTable.get(str));
				if (charTable.size() < maxSize) /* If the string is not present the add that to the HashMap*/{
					charTable.put(word, (int) ++tableSize);
				}
				str = "" + a;
			}
		}
		encodedArrayList.add(charTable.get(str));
		return encodedArrayList;//Return the encodedArrayList which contains the encoded values
	}

	/*This function is used to write to the file*/
	private void writeToFile(List<Integer> encodedList, String inputFileName) {
		//Buffered writer to write to the file
		BufferedWriter outputWriter = null;
		String outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".")) + ".lzw";
		try {
			
			outputWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), StandardCharsets.UTF_16BE));//Charset UTF_16BE used to store and opent he file
			for (int i = 0; i < encodedList.size(); i++) {
				System.out.println(encodedList.get(i));
				int value = encodedList.get(i);
				outputWriter.write(value);
				outputWriter.flush();

			}
			outputWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*This function is used to read from the file*/
	private String readFile(String fileName) {
		//StringBuffer to all the lines in stringBuffer
		StringBuffer stringBuffer = new StringBuffer();
		try {
			String readLine = null;
			//BufferReader to read from the file 
			BufferedReader br = Files.newBufferedReader(Paths.get(fileName), StandardCharsets.UTF_8);//CharSet UTF_8 to read
			while ((readLine = br.readLine()) != null) {
				stringBuffer.append(readLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Passes the stringBuffer which contains the inputText
		return stringBuffer.toString();
	}

	public static void main(String[] args) throws Exception {

		Encoder encoder = new Encoder();
		String inputFileName="";
		int bitLength=0;
		if((!args[0].isEmpty() || !args[1].isEmpty() || args[0] == null || args[1] == null)){
			inputFileName = args[0]; // check for null and if file exists
			try{
			bitLength = Integer.parseInt(args[1]); // check if its a valid input
			}catch(NumberFormatException nfe){
				System.out.println("Please enter an Integer Number" + nfe);
				}
			}else{
			System.out.println("Please Input File and bitLength");
			return;
		}
	
		String inputData = encoder.readFile(inputFileName);
		if(inputData.equals("")){
			System.out.println("Unable to read data from Input file " + inputFileName);
			return;
		}
		//Calling the encoder function 
		List<Integer> encodedList = encoder.encodeData(inputData, bitLength);
		encoder.writeToFile(encodedList, inputFileName);// Passing the encodedList to the writeToFile function
	}
}
