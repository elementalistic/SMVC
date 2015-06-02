package starNet;

import java.util.Date;

public class ServerInfo
{
  private final Byte infoVersion;
  private final Float version;
  private final String name;
  private final String desc;
  private final Long startTime;
  private final Integer playerCount;
  private final Integer maxPlayers;
  private final long ping;
  private final String host;
  private final int port;
  
  public ServerInfo(String host, int port, Object[] returnValues, long roundTripTime)
  {
    this.host = host;
    this.port = port;
    this.infoVersion = ((Byte)returnValues[0]);
    this.version = ((Float)returnValues[1]);
    this.name = ((String)returnValues[2]);
    this.desc = ((String)returnValues[3]);
    this.startTime = ((Long)returnValues[4]);
    this.playerCount = ((Integer)returnValues[5]);
    this.maxPlayers = ((Integer)returnValues[6]);
    this.ping = roundTripTime;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("SERVER INFO FOR " + this.host + ":" + this.port + "(INFO VERSION: " + this.infoVersion + ")\n");
    sb.append("Version: " + this.version + "\n");
    sb.append("Name: " + this.name + "\n");
    sb.append("Description: " + this.desc + "\n");
    sb.append("Started: " + new Date(this.startTime.longValue()) + "\n");
    sb.append("Players: " + this.playerCount + "/" + this.maxPlayers + "\n");
    sb.append("Ping: " + this.ping + "\n");
    
    return sb.toString();
  }
  
  public Byte getInfoVersion()
  {
    return this.infoVersion;
  }
  
  public Float getVersion()
  {
    return this.version;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getDesc()
  {
    return this.desc;
  }
  
  public Long getStartTime()
  {
    return this.startTime;
  }
  
  public Integer getPlayerCount()
  {
    return this.playerCount;
  }
  
  public Integer getMaxPlayers()
  {
    return this.maxPlayers;
  }
  
  public long getPing()
  {
    return this.ping;
  }
  
  public String getHost()
  {
    return this.host;
  }
  
  public int getPort()
  {
    return this.port;
  }
}