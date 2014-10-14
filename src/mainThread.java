public class mainThread {
   public static void main(String args[]) {
	   
	   // new contact data from NFC tapping in the form of encrypted String.
	   // decrypt the STring and ask synchronize to upload that string.
	   
	   String newData = "Ather;12345;12:50";
   Synchronize s = new Synchronize(newData);
	   s.start();
	   s.run();
	   
	   // whenever NFC tapp function has new data, we just ask the same synchronize thread that we have new data now so upload it.
	   s.dataToUpload = newData;
}
}