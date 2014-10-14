import java.io.FileNotFoundException;
import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class Upload implements Runnable
{
	
	public Thread t;
	String data;
	Upload(String dataToUpload)
	{
		System.out.println("Creating an instance of Upload");
		data = dataToUpload;
	}
	public void start ()
	{
	      System.out.println("Starting upload thread");
	      if (t == null)
	      {
	         t = new Thread ();
	         t.start ();
	      }
	}
	public void run()
	{
		System.out.println(" Running the started upload thread");
		PrintWriter writer;
		try {
			writer = new PrintWriter("newContact.txt");
			writer.println(data);
			writer.close();
			final String fromEmail = "dummya996@gmail.com"; //requires valid gmail id
	        final String password = "password@12"; // correct password for gmail id
	        final String toEmail = "dummya996@gmail.com"; // can be any email id 
	         
	        System.out.println("TLSEmail Start");
	        //Properties props = new Properties();
	        java.util.Properties props = new java.util.Properties();

	        props.put("mail.smtp.host", "smtp.googlemail.com"); //SMTP Host
	        props.put("mail.smtp.port", "587"); //TLS Port
	        props.put("mail.smtp.auth", "true"); //enable authentication
	        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
	        props.put("mail.smtp.connectiontimeout", 60000); // session will close after 1 min
	         
	                //create Authenticator object to pass in Session.getInstance argument
	        Authenticator auth = new Authenticator() {
	            //override the getPasswordAuthentication method
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(fromEmail, password);
	            }
	        };
	        Session session = Session.getInstance(props, auth);
	         
	        //EmailUtil.sendEmail(session, toEmail,"TLSEmail Testing Subject", "TLSEmail Testing Body");
	         sendAttachmentEmail(session, toEmail,"new contact", "Body");
	    
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}
		
		
	}
	
	
	
	
	public static void sendAttachmentEmail(Session session, String toEmail, String subject, String body) throws FileNotFoundException {
	    try{
	         MimeMessage msg = new MimeMessage(session);
	         msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	         msg.addHeader("format", "flowed");
	         msg.addHeader("Content-Transfer-Encoding", "8bit");
	           
	         msg.setFrom(new InternetAddress("dummya996@gmail.com"));
	 
	         msg.setReplyTo(InternetAddress.parse("dummya996@gmail.com", false));
	 
	         msg.setSubject(subject, "UTF-8");
	 
	         msg.setSentDate(new Date());
	 
	         msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	           
	         // Create the message body part
	         BodyPart messageBodyPart = new MimeBodyPart();
	 
	         // Fill the message
	         messageBodyPart.setText(body);
	          
	         // Create a multipart message for attachment
	         Multipart multipart = new MimeMultipart();
	 
	         // Set text message part
	         multipart.addBodyPart(messageBodyPart);
	 
	         // Second part is attachment
	         messageBodyPart = new MimeBodyPart();
	         String filename = "newContact.txt";
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);
	 
	         // Send the complete message parts
	         msg.setContent(multipart);
	 
	         // Send message
	         Transport.send(msg);
	         //Transport.close();	
	         System.out.println("EMail Sent Successfully with attachment!!");
	      }catch (MessagingException e) {
	         e.printStackTrace();
	      }// catch (UnsupportedEncodingException e) {
	         //e.printStackTrace();
	    }
	}

	
	
	
	
