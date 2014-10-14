import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeBodyPart;

public class Download implements Runnable
{
	private String saveDirectory;
    private static int HOLDS_MESSAGES;

    public void setSaveDirectory(String dir) {
        this.saveDirectory = dir;
    }
	public Thread t;
	Download()
	{
		System.out.println("Creating an instance of Download");
	}
	public void start ()
	{
	      System.out.println("Starting download thread");
	      if (t == null)
	      {
	         t = new Thread ();
	         t.start ();
	      }
	}
	public void run()
	{
		String host = "imap.gmail.com";
        String port = "993";
        String userName = "dummya996@gmail.com";
        String password = "password@12";
 
        String saveDirectory = "/Users/Ather/Documents/workspace/thread";
        setSaveDirectory(saveDirectory);
        downloadEmailAttachments(host, port, userName, password);
	}
    public void downloadEmailAttachments(String host, String port,
            String userName, String password) {
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(properties);
 
        try {
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", userName, password);
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_WRITE);
            Folder outputFolder = store.getFolder("App");
            if (!outputFolder.exists()) // create
            {
                outputFolder.create(HOLDS_MESSAGES);
            }
            
                       
            Message[] arrayMessages = folderInbox.getMessages();
            
            
            
            String subj = "new contact";

            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                String subject = message.getSubject();
                boolean result = subject.equals(subj);
                if (result == true)
        		{
                
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();
                String From = "dummy account <dummya996@gmail.com>";
                String orfrom = "dummya996@gmail.com";
                boolean sender = from.equals(From);
                boolean orsender = from.equals(orfrom);
                if(sender == true || orsender == true)
                {
                
                String sentDate = message.getSentDate().toString();
 
                String contentType = message.getContentType();
                String messageContent = "";
                String attachFiles = "";
        		if (contentType.contains("multipart")) {
                    // content that contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            part.saveFile(saveDirectory + File.separator + fileName);
                        } else {
                            messageContent = part.getContent().toString();
                        }
                    }
 
                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    Object content = message.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }
                System.out.println("Message #" + (i + 1) + ":");
                System.out.println("\t From: " + from);
                System.out.println("\t Subject: " + subject);
                System.out.println("\t Sent Date: " + sentDate);
                System.out.println("\t Message: " + messageContent);
                System.out.println("\t Attachments: " + attachFiles);
                message.getFolder().copyMessages(new Message[] {message}, outputFolder);
                message.setFlag(Flag.DELETED, true);
                message.getFolder().expunge();
                fileHandling();
                }
            }
            }
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for imap.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void fileHandling() throws Exception {
		//String fileNameToRead = "newContact.txt";
		//String fileNameToWrite = "ContactsList.txt";
		String line;
		File fileToRead = new File("/Users/Ather/Documents/workspace/thread/newContact.txt");

		File fileToWrite = new File("/Users/Ather/Documents/workspace/thread/ContactsList.txt");
		if (!fileToWrite.exists()) {
			fileToWrite.createNewFile();
		}
		try {
			FileReader fileReader = new FileReader(fileToRead.getAbsoluteFile());
			BufferedReader bufferReader = new BufferedReader(fileReader);
			//boolean check = false;
			line = bufferReader.readLine();
			FileWriter fileWriter = new FileWriter(fileToWrite.getAbsoluteFile(), true);
			BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
			//bufferWriter.newLine();
			bufferWriter.write("\n" + line);
			/*do{
				System.out.println(line);
				String[] name = new String [4];
				name = line.split(";");
				System.out.println(name[0] + " another part of same line is " + name[1]);
				System.out.println(name);	
			}while(check == true) ;    */
			bufferReader.close();
			bufferWriter.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileToWrite + "'");				
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileToWrite + "'");					
			ex.printStackTrace();
		}

    
    }
}
