package starNet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Header
{
  public static final byte packetByte = 42;
  public static final byte pingByte = 23;
  public static final byte testByte = 100;
  public static final byte logoutByte = 65;
  private byte commandId;
  public short packetId;
  byte commandType;
  private byte type;
  public static final byte TYPE_PARAMETRIZED_COMMAND = 111;
  public static final byte TYPE_STREAM_COMMAND = 123;
  
  public Header() {}
  
  public Header(byte commandId, short packetId, byte type)
  {
    this.commandId = commandId;
    this.type = type;
    this.packetId = packetId;
  }
  
  public byte getCommandId()
  {
    return this.commandId;
  }
  
  public byte getType()
  {
    return this.type;
  }
  
  public void read(DataInputStream inputStream)
    throws IOException
  {
    this.packetId = inputStream.readShort();
    this.commandId = inputStream.readByte();
    this.type = inputStream.readByte();
  }
  
  public String toString()
  {
    return 
    
      "\n||commandId: " + getCommandId() + "; \n" + "||type: " + getType() + "; \n" + "||packetId: #" + this.packetId;
  }
  
  public void write(DataOutputStream outputStream)
    throws IOException
  {
    outputStream.write(42);
    outputStream.writeShort(this.packetId);
    outputStream.writeByte(getCommandId());
    outputStream.writeByte(getType());
  }
}