import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
public class Server extends ServerMenu{

  public Process p;
  public Runtime rt;
  public String rAM;
  public File file = new File(userHomePath+"//logs//latest.log");
  public File log = new File(userHomePath+"//logs//latest.log");
  public MineStat ms;

  //Constructor with all needed data/properties for the operation of the server.
  public Server(String difficulty, String gamemode, String pvp,String forceGamemode,String hardCoreEnabled, String ram, String ip) {

    rAM = ram;//'ram' from constructor is only accessable in the constructor, so I transferred the data to a public String so that all methods may use it.
    try{

      FileOutputStream out = new FileOutputStream(userHomePath+"//server.properties");
      FileInputStream in = new FileInputStream(userHomePath+"//server.properties");
      Properties props = new Properties();
      props.load(in);
      in.close();
      props.setProperty("spawn-protection", "16");
      props.setProperty("max-tick-time", "60000");
      props.setProperty("query.port", "25565");
      props.setProperty("generator-settings", "");
      props.setProperty("force-gamemode", forceGamemode);
      props.setProperty("allow-nether", "true");
      props.setProperty("enforce-whitelist", "false");
      props.setProperty("gamemode", gamemode);
      props.setProperty("broadcast-console-to-ops", "true");
      props.setProperty("enable-query", "false");
      props.setProperty("player-idle-timeout", "0");
      props.setProperty("difficulty", difficulty);
      props.setProperty("spawn-monsters", "true");
      props.setProperty("broadcast-rcon-to-ops", "true");
      props.setProperty("op-permission-level", "4");
      props.setProperty("pvp", pvp);
      props.setProperty("snooper-enabled", "true");
      props.setProperty("level-type", "default");
      props.setProperty("hardcore", hardCoreEnabled);
      props.setProperty("enable-command-block", "false");
      props.setProperty("max-players", "20");
      props.setProperty("network-compression-threshold", "256");
      props.setProperty("resource-pack-sha1", "");
      props.setProperty("max-world-size", "29999984");
      props.setProperty("rcon.port", "25575");
      props.setProperty("server-port", "25565");
      props.setProperty("server-ip", ip);
      props.setProperty("spawn-npcs", "true");
      props.setProperty("allow-flight", "false");
      props.setProperty("level-name", "world");
      props.setProperty("view-distance", "10");
      props.setProperty("resource-pack", "");
      props.setProperty("spawn-animals", "true");
      props.setProperty("white-list", "false");
      props.setProperty("rcon.password", "1234");
      props.setProperty("generate-structures", "true");
      props.setProperty("max-build-height", "256");
      props.setProperty("online-mode", "true");
      props.setProperty("level-seed", "");
      props.setProperty("use-native-transport", "true");
      props.setProperty("prevent-proxy-connections", "false");
      props.setProperty("enable-rcon", "true");
      props.setProperty("motd", "JahCraft");
      props.store(out, null);
      out.close();

    }catch(Exception e){e.printStackTrace();}

  }
  public boolean start(){
    try{
      rt = Runtime.getRuntime();
      rt.exec(new String[]{"cmd", "/C", "java -Xms"+rAM+" -Xmx"+rAM+" -jar spigot-1.14.4.jar "}, null, new File(userHomePath+"//"));

      return true;
    }catch(Exception e){return false;}
  }

  public String getStatus(){
    try{

    ms = new MineStat(ip, 25565);
    ms.refresh();
    }catch(Exception e){}
    if(ms.isServerUp())
    {
      return "Online";
    }
    else{
      return "Offline";
  }
}
  public Long getPing(){
    try{
    ms = new MineStat(ip, 25565);
    }catch(Exception e){}
    ms.refresh();
    return ms.getLatency();
  }
public String getLog(){
  try{
    FIO files = new FIO(file);
    StringBuilder stb = new StringBuilder();

    try{
      for(int i = 1;;i++){
        stb.append(files.load(i).toString()+"\n");
      }
    }catch(Exception e){}

    return stb.toString();
  }catch(Exception g){}
  return "";
}

public boolean getPvp(){
    if(pvpEnabled.equals("true"))
      return true;
    else
      return false;
}

  public String getHardcore(){
    return hardCoreEnabled;
  }

}
