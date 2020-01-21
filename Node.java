import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public abstract class Node
{
    protected DatagramSocket socket;
    protected MulticastSocket mSocket;
    private int id;
    private ArrayList<Link> links;
    private int port;
    private int mCastRecvPort;
    private Group mCastGroup;
    private ArrayList<String> apartOfAddresses;

    public Node(int id, int port)
    {
        this.id = id;
        this.port = port;
        this.mCastRecvPort = 4000;
        this.links = new ArrayList<Link>();
        try
        {
            this.mSocket = new MulticastSocket(this.mCastRecvPort);
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        this.mCastGroup = new Group (this.id, this.port);
        this.apartOfAddresses = new ArrayList<String>();
    }

    public abstract void setupAllSockets();

    public ArrayList<String> getApartOfAddresses()
    {
        return apartOfAddresses;
    }

    public void setApartOfAddresses(ArrayList<String> apartOfAddresses)
    {
        this.apartOfAddresses = apartOfAddresses;
    }

    public MulticastSocket getmSocket()
    {
        return mSocket;
    }

    public void setmSocket(MulticastSocket mSocket)
    {
        this.mSocket = mSocket;
    }

    public int getmCastRecvPort()
    {
        return mCastRecvPort;
    }

    public void setmCastRecvPort(int mCastRecvPort)
    {
        this.mCastRecvPort = mCastRecvPort;
    }

    public Group getmCastGroup()
    {
        return mCastGroup;
    }

    public void setmCastGroup(Group mCastGroup)
    {
        this.mCastGroup = mCastGroup;
    }

    public DatagramSocket getSocket()
    {
        return socket;
    }

    public void setSocket(DatagramSocket socket)
    {
        this.socket = socket;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ArrayList<Link> getLinks()
    {
        return links;
    }

    public void setLinks(ArrayList<Link> links)
    {
        this.links = links;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String toString()
    {
        String output = "";
        output = output + "Node ID: " + this.getId() + "\nPort: " + this.getPort();
        for (int i = 0 ; i < this.getLinks().size() ; i++)
        {
            output = output + "\nLink " + i + ":\n" + this.getLinks().get(i);
        }
        output = output + "\n" + this.getmCastGroup();
        if (this.getApartOfAddresses().isEmpty() != true)
        {
            output = output + "\nApart of: " + this.getApartOfAddresses().get(0);
        }
        return output;
    }
}
