package com.jms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLockInterruptionException;
import java.util.Properties;
/*
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
*/
class A
{
	 void display() throws IOException {System.out.print("A");}
};
	class B extends A
	{
		B()
		{
			System.out.print("B COns");
		}
		B(int a)
		{
			super();	
			
		}
		{
			System.out.print("C");
		}
		static{
			System.out.print("D");
		}
		void display() throws OutOfMemoryError {System.out.print("B");}
	};

	interface X{
		void abc()throws IOException;
	}
	
	
	class Z implements X
	{

		public void abc() throws FileNotFoundException  {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
		
public class TestJMS {
	
	
	 static final public  void main(String[]args) throws IOException
	{
		 System.out.print(args[0]);
		 A a=new B();
		 //a.display();
		File file=new File("C:\\DK\\DK\\Work\\2-nov-2015\\abc.zip");
		File filee=new File("C:\\DK\\DK\\Work\\2-nov-2015\\abcc.zip");
		FileInputStream fis=new FileInputStream(file);
		FileOutputStream fos=new FileOutputStream(filee);
		int i=fis.read();
		for(;i!=-1;)
		{
			fos.write(i);
			i=fis.read();
		}
		
		fis.close();
		fos.flush();
		fos.close();
		
		/*Properties env = new Properties();
		env.setProperty( "java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" );
		env.setProperty( "java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" );
		env.setProperty( Context.PROVIDER_URL, "jnp://localhost:1099" );    // Change this to your desired IP/host
		 
		// To Lookup for Queue Connection Factory
		Context ctx = new InitialContext(env);
		ConnectionFactory qCF = (ConnectionFactory) ctx.lookup( "jms/TestQueueConnectionFactory" );
		 
		// To Lookup for Topic Connection Factory
		ctx = new InitialContext(env);
		ConnectionFactory tCF = (ConnectionFactory) ctx.lookup( "jms/TestTopicConnectionFactory" );

		Queue orderQueue = (Queue)ctx.lookup("/jms/TestQueue");

		
		Connection con= qCF.createConnection();
		
		Session session=con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		MessageProducer producer= session.createProducer(orderQueue);
		
		MessageConsumer consumer = session.createConsumer(orderQueue);
		
		con.start();
		
		TextMessage message = session.createTextMessage("This is an order");
		producer.send(message);
		
		TextMessage receivedMessage = (TextMessage)consumer.receive();
		
		System.out.println("Got order: " + receivedMessage.getText());

		con.close();

		
		*/
		
	}

}
