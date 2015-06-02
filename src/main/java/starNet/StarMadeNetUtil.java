package starNet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Observable;

public class StarMadeNetUtil
  extends Observable
{
  public static final byte TYPE_INT = 1;
  public static final byte TYPE_LONG = 2;
  public static final byte TYPE_FLOAT = 3;
  public static final byte TYPE_STRING = 4;
  public static final byte TYPE_BOOLEAN = 5;
  public static final byte TYPE_BYTE = 6;
  public static final byte TYPE_SHORT = 7;
  private static final byte TYPE_BYTE_ARRAY = 8;
  
  public ServerInfo getServerInfo(String host, int port)
    throws UnknownHostException, IOException
  {
    Socket s = new Socket(host, port);
    

    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
    DataInputStream in = new DataInputStream(s.getInputStream());
    




    out.writeInt(9);
    











    new Header((byte)1, (short)-1, (byte)111).write(out);
    






    writeParametriziedCommand(new Object[0], -1, 0, out);
    


    long started = System.currentTimeMillis();
    



    out.flush();
    







    int size = in.readInt();
    long ts = in.readLong();
    


    byte[] receive = new byte[size];
    in.readFully(receive);
    
    DataInputStream bin = new DataInputStream(new ByteArrayInputStream(receive));
    




    byte check = (byte)bin.read();
    



    Header h = new Header();
    h.read(bin);
    



    Object[] returnValues = readParameters(h, bin);
    



    long ended = System.currentTimeMillis();
    long roundTripTime = ended - started;
    
    ServerInfo info = new ServerInfo(host, port, returnValues, roundTripTime);
    notifyObservers(info.toString());
    return info;
  }
  
  public String executeAdminCommand(String host, int port, String serverPassword, String command)
    throws UnknownHostException, IOException
  {
	  
	String ret = "";
	
    Socket s = new Socket(host, port);
    

    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
    DataInputStream in = new DataInputStream(s.getInputStream());
    ByteArrayOutputStream bb = new ByteArrayOutputStream();
    DataOutputStream dI = new DataOutputStream(bb);
    dI.writeUTF(serverPassword);
    dI.writeUTF(command);
    


    out.writeInt(11 + bb.size());
    











    new Header((byte)2, (short)-1, (byte)111).write(out);
    






    writeParametriziedCommand(new Object[] { serverPassword, command }, -1, 0, out);
    

    long started = System.currentTimeMillis();
    



    out.flush();
    Object[] returnValues;
    long roundTripTime;
    do
    {
      int size = in.readInt();
      long ts = in.readLong();
      


      byte[] receive = new byte[size];
      in.readFully(receive);
      
      DataInputStream bin = new DataInputStream(new ByteArrayInputStream(receive));
      




      byte check = (byte)bin.read();
      



      Header h = new Header();
      h.read(bin);
      



      returnValues = readParameters(h, bin);
      
      System.err.println("RETURN: " + Arrays.toString(returnValues));
      
      ret += Arrays.toString(returnValues);
      
      
      setChanged();
      notifyObservers(Arrays.toString(returnValues));
      
      long ended = System.currentTimeMillis();
      roundTripTime = ended - started;
    } while ((returnValues.length < 2) || (!returnValues[1].toString().startsWith("END;")));
    
    return ret;
  }
  
  public static void writeParametriziedCommand(Object[] attribs, int fromId, int receiver, DataOutputStream ntProcessor)
    throws IOException
  {
    ntProcessor.writeInt(attribs.length);
    for (int i = 0; i < attribs.length; i++) {
      if ((attribs[i] instanceof Long))
      {
        ntProcessor.write(2);
        ntProcessor.writeLong(((Long)attribs[i]).longValue());
      }
      else if ((attribs[i] instanceof String))
      {
        ntProcessor.write(4);
        ntProcessor.writeUTF((String)attribs[i]);
      }
      else if ((attribs[i] instanceof Float))
      {
        ntProcessor.write(3);
        ntProcessor.writeFloat(((Float)attribs[i]).floatValue());
      }
      else if ((attribs[i] instanceof Integer))
      {
        ntProcessor.write(1);
        ntProcessor.writeInt(((Integer)attribs[i]).intValue());
      }
      else if ((attribs[i] instanceof Boolean))
      {
        ntProcessor.write(5);
        ntProcessor.writeBoolean(((Boolean)attribs[i]).booleanValue());
      }
      else if ((attribs[i] instanceof Byte))
      {
        ntProcessor.write(6);
        ntProcessor.writeByte(((Byte)attribs[i]).byteValue());
      }
      else if ((attribs[i] instanceof Short))
      {
        ntProcessor.write(7);
        ntProcessor.writeShort(((Short)attribs[i]).shortValue());
      }
      else if ((attribs[i] instanceof byte[]))
      {
        byte[] b = (byte[])attribs[i];
        ntProcessor.write(8);
        ntProcessor.writeInt(b.length);
        ntProcessor.write(b);
      }
     
    }
  }
  
  public static Object[] readParameters(Header header, DataInputStream inputStream)
    throws IOException
  {
    int parameterSize = inputStream.readInt();
    
    Object[] parameters = new Object[parameterSize];
    for (int i = 0; i < parameterSize; i++)
    {
      byte type = (byte)inputStream.read();
      switch (type)
      {
      case 2: 
        parameters[i] = Long.valueOf(inputStream.readLong()); break;
      case 4: 
        parameters[i] = inputStream.readUTF(); break;
      case 3: 
        parameters[i] = Float.valueOf(inputStream.readFloat()); break;
      case 1: 
        parameters[i] = Integer.valueOf(inputStream.readInt()); break;
      case 5: 
        parameters[i] = Boolean.valueOf(inputStream.readBoolean()); break;
      case 6: 
        parameters[i] = Byte.valueOf(inputStream.readByte()); break;
      case 7: 
        parameters[i] = Short.valueOf(inputStream.readShort()); break;
      case 8: 
        int l = inputStream.readInt();
        byte[] b = new byte[l];
        inputStream.readFully(b);
        parameters[i] = b;
        break;
      default: 
        throw new IllegalArgumentException("Type: " + type + " unknown. parameter " + i + " of " + parameterSize + " in command " + header.getCommandId());
      }
    }
    return parameters;
  }
}
