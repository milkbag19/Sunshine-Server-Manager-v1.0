
public class ServerCheck {
  


  public static void main(String[] args)
  {
    MineStat ms = new MineStat("174.115.21.188", 25565);
    System.out.println("Minecraft server status of " + ms.getAddress() + " on port " + ms.getPort() + ":");
    if(ms.isServerUp())
    {
      System.out.println("Server is online running version " + ms.getVersion() + " with " + ms.getCurrentPlayers() + " out of " + ms.getMaximumPlayers() + " players.");
      System.out.println("Message of the day: " + ms.getMotd());
      System.out.println("Latency: " + ms.getLatency() + "ms");
    }
    else
      System.out.println("Server is offline!");
  }
}