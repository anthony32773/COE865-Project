import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Source extends Node implements Runnable
{
    private ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge> paths;
    private ArrayList<List<String>> shortestPaths;

    public Source (int id, int port, DijkstraShortestPath<String, DefaultWeightedEdge> dAlg)
    {
        super (id, port);
        this.paths = dAlg.getPaths(Integer.toString(id));
        this.shortestPaths = new ArrayList<List<String>>();
    }

    public void generateShortestPath(int idNumber)
    {
        this.getShortestPaths().add(this.getPaths().getPath(Integer.toString(idNumber)).getVertexList());
    }

    public String getAllShortestPaths()
    {
        String output = "";
        for (int i = 0 ; i < this.getShortestPaths().size() ; i++)
        {
            output = output + "Path from Source to: " + this.getShortestPaths().get(i).get(this.getShortestPaths().get(i).size() - 1) + "\n";
            for (int j = 0 ; j < this.getShortestPaths().get(i).size() ; j++)
            {
                output = output + this.getShortestPaths().get(i).get(j) + " ";
            }
            output = output + "\n";
        }
        return output;
    }

    public ArrayList<List<String>> getShortestPaths()
    {
        return shortestPaths;
    }

    public void setShortestPaths(ArrayList<List<String>> shortestPaths)
    {
        this.shortestPaths = shortestPaths;
    }

    public ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge> getPaths()
    {
        return paths;
    }

    public void setPaths(ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge> paths)
    {
        this.paths = paths;
    }

    public void setupAllSockets()
    {
        try
        {
            this.socket = new DatagramSocket(this.getmCastGroup().getSendPort());
        }
        catch (SocketException e)
        {
            System.out.println (e.getMessage());
        }
    }

    public void run()
    {
        String message = "Packet from Source!";
        byte[] buffer = message.getBytes();
        DatagramPacket packet;
        try
        {
            InetAddress addrToSend = InetAddress.getByName(this.getmCastGroup().getmAddr());
            packet = new DatagramPacket(buffer, buffer.length, addrToSend, this.getmCastRecvPort());
            this.getSocket().send(packet);
        }
        catch (IOException e)
        {
            System.out.println (e.getMessage());
        }
        System.out.println ("Source Thread Sent Packet!\n");
    }

    public String toString()
    {
        return "Source:\n" + super.toString();
    }
}
