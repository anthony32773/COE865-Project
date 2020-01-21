import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//Code: 0 = Forwarder
//      1 = Source
//      2 = Receiver

//First 7 Lines:
// Node ID, code, PORT
//Next Lines:
// End1, End2, Cost

public class Network
{
    public static void main (String args[])
    {
        File configFile = new File("config.txt");
        int counter = 0;
        ArrayList <Integer> idList = new ArrayList<Integer>();
        ArrayList <Integer> typeList = new ArrayList<Integer>();
        ArrayList <Integer> portList = new ArrayList<Integer>();
        ArrayList <Link> linkList = new ArrayList<Link>();
        ArrayList <Receiver> recvrList = new ArrayList<Receiver>();
        ArrayList <Forwarder> forwarderList = new ArrayList<Forwarder>();
        Source source;
        try
        {
            Scanner fileReader = new Scanner(configFile);
            while (fileReader.hasNextLine())
            {
                counter++;
                String data = fileReader.nextLine();
                if (counter < 8)
                {
                    idList.add(Character.getNumericValue(data.charAt(0)));
                    typeList.add(Character.getNumericValue(data.charAt(2)));
                    portList.add(Integer.parseInt(data.substring(4)));
                }
                else
                {
                    int id1 = Character.getNumericValue(data.charAt(0));
                    int id2 = Character.getNumericValue(data.charAt(2));
                    int cost = Integer.parseInt(data.substring(4));
                    int port1Index = idList.indexOf(id1);
                    int port2Index = idList.indexOf(id2);
                    linkList.add(new Link(cost, id1, id2, portList.get(port1Index), portList.get(port2Index)));
                }
            }
            fileReader.close();
        }
        catch (IOException e)
        {
            System.out.println (e.getMessage());
        }

        DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge> configGraph = new DefaultUndirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        for (int i = 0 ; i < idList.size() ; i++)
        {
            configGraph.addVertex(Integer.toString(idList.get(i)));
        }

        for (int i = 0 ; i < linkList.size() ; i++)
        {
            configGraph.addEdge(Integer.toString(linkList.get(i).getEnd1()), Integer.toString(linkList.get(i).getEnd2()));
            configGraph.setEdgeWeight(Integer.toString(linkList.get(i).getEnd1()), Integer.toString(linkList.get(i).getEnd2()), (double)linkList.get(i).getCost());
        }

        DijkstraShortestPath<String, DefaultWeightedEdge> dAlg = new DijkstraShortestPath<>(configGraph);

        int sourceID = 0, sourcePort = 0;
        for (int i = 0 ; i < typeList.size() ; i++)
        {
            if (typeList.get(i) == 0)
            {
                forwarderList.add(new Forwarder(idList.get(i), portList.get(i)));
            }
            else if (typeList.get(i) == 1)
            {
                sourceID = idList.get(i);
                sourcePort = portList.get(i);
            }
            else
            {
                recvrList.add(new Receiver(idList.get(i), portList.get(i)));
            }
        }

        source = new Source(sourceID, sourcePort, dAlg);

        for (int i = 0 ; i < linkList.size() ; i++)
        {
            boolean check1 = false, check2 = false;
            int host1 = 0;
            int host2 = 0;
            host1 = linkList.get(i).getEnd1();
            host2 = linkList.get(i).getEnd2();


            if (host1 == source.getId() || host2 == source.getId())
            {
                source.getLinks().add(linkList.get(i));
            }
            for (int j = 0 ; j < forwarderList.size() ; j++)
            {
                if (host1 == forwarderList.get(j).getId() || host2 == forwarderList.get(j).getId())
                {
                    forwarderList.get(j).getLinks().add(linkList.get(i));
                }
            }
            for (int j = 0 ; j < recvrList.size() ; j++)
            {
                if (host1 == recvrList.get(j).getId() || host2 == recvrList.get(j).getId())
                {
                    recvrList.get(j).getLinks().add(linkList.get(i));
                }
            }
        }


        for (int i = 0 ; i < recvrList.size() ; i++)
        {
            source.generateShortestPath(recvrList.get(i).getId());
        }


        boolean check = false;
        for (int i = 0 ; i < source.getShortestPaths().size() ; i++)
        {
            for (int j = 0 ; j < source.getShortestPaths().get(i).size() - 1 ; j++)
            {
                //If node in path is equal to source
                boolean sourceCheck = false;
                check = false;
                if (source.getShortestPaths().get(i).get(j).equals(Integer.toString(source.getId())))
                {
                    check = false;
                    for (int k = 0 ; k < forwarderList.size() ; k++)
                    {
                        if (source.getShortestPaths().get(i).get(j + 1).equals(Integer.toString(forwarderList.get(k).getId())))
                        {
                            if (source.getmCastGroup().getRecvrList().contains(forwarderList.get(k)) == false)
                            {
                                source.getmCastGroup().getRecvrList().add(forwarderList.get(k));
                                forwarderList.get(k).getApartOfAddresses().add(source.getmCastGroup().getmAddr());
                            }
                            check = true;
                            sourceCheck = true;
                            break;
                        }
                    }
                    if (check == false)
                    {
                        for (int k = 0 ; k < recvrList.size() ; k++)
                        {
                            if (source.getShortestPaths().get(i).get(j + 1).equals(Integer.toString(recvrList.get(k).getId())))
                            {
                                if (source.getmCastGroup().getRecvrList().contains(recvrList.get(k)) == false)
                                {
                                    source.getmCastGroup().getRecvrList().add(recvrList.get(k));
                                    recvrList.get(k).getApartOfAddresses().add(source.getmCastGroup().getmAddr());
                                }
                                sourceCheck = true;
                                break;
                            }
                        }
                    }
                }

                if (sourceCheck == true)
                {
                    continue;
                }

                check = false;
                //If node in path is equal to a node in forwarder list
                for (int n = 0 ; n < forwarderList.size() ; n++)
                {
                    if (source.getShortestPaths().get(i).get(j).equals(Integer.toString(forwarderList.get(n).getId())))
                    {
                        check = false;
                        for (int m = 0 ; m < forwarderList.size() ; m++)
                        {
                            if (source.getShortestPaths().get(i).get(j + 1).equals(Integer.toString(forwarderList.get(m).getId())))
                            {
                                if (forwarderList.get(n).getmCastGroup().getRecvrList().contains(forwarderList.get(m)) == false)
                                {
                                    forwarderList.get(n).getmCastGroup().getRecvrList().add(forwarderList.get(m));
                                    forwarderList.get(m).getApartOfAddresses().add(forwarderList.get(n).getmCastGroup().getmAddr());
                                }
                                check = true;
                                break;
                            }
                        }

                        if (check == false)
                        {
                            for (int m = 0 ; m < recvrList.size() ; m++)
                            {
                                if (source.getShortestPaths().get(i).get(j + 1).equals(Integer.toString(recvrList.get(m).getId())))
                                {
                                    if (forwarderList.get(n).getmCastGroup().getRecvrList().contains(recvrList.get(m)) == false)
                                    {
                                        forwarderList.get(n).getmCastGroup().getRecvrList().add(recvrList.get(m));
                                        recvrList.get(m).getApartOfAddresses().add(forwarderList.get(n).getmCastGroup().getmAddr());
                                    }
                                    check = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (check == true)
                    {
                        break;
                    }
                }
            }
        }

        System.out.println (source);
        System.out.println ("");

        for (int i = 0 ; i < forwarderList.size() ; i++)
        {
            System.out.println (forwarderList.get(i));
            System.out.println ("");
        }
        for (int i = 0 ; i < recvrList.size() ; i++)
        {
            System.out.println (recvrList.get(i));
            System.out.println ("");
        }

        System.out.println (source.getAllShortestPaths());

//        System.out.println (source.getmCastGroup());
//        for (int i = 0 ; i < forwarderList.size() ; i++)
//        {
//            System.out.println (forwarderList.get(i).getmCastGroup());
//            for (int j = 0 ; j < forwarderList.get(i).getApartOfAddresses().size() ; j++)
//            {
//                System.out.println ("Apart of: " + forwarderList.get(i).getApartOfAddresses().get(j));
//            }
//            System.out.println ("");
//        }
//
//        for (int i = 0 ; i < recvrList.size() ; i++)
//        {
//            System.out.println (recvrList.get(i).getmCastGroup());
//            for (int j = 0 ; j < recvrList.get(i).getApartOfAddresses().size() ; j++)
//            {
//                System.out.println ("Apart of: " + recvrList.get(i).getApartOfAddresses().get(j));
//            }
//            System.out.println ("");
//        }

        Thread sourceThread = new Thread(source);
        source.setupAllSockets();
        ArrayList<Thread> forwardThreadList = new ArrayList<Thread>();
        ArrayList<Thread> recvrThreadList = new ArrayList<Thread>();

        for (int i = 0 ; i < forwarderList.size() ; i++)
        {
            forwarderList.get(i).setupAllSockets();
            forwardThreadList.add(new Thread(forwarderList.get(i)));
        }

        for (int i = 0 ; i < recvrList.size() ; i++)
        {
            recvrList.get(i).setupAllSockets();
            recvrThreadList.add(new Thread(recvrList.get(i)));
        }

        for (int i = 0 ; i < recvrThreadList.size() ; i++)
        {
            recvrThreadList.get(i).start();
        }

        for (int i = 0 ; i < forwardThreadList.size() ; i++)
        {
            forwardThreadList.get(i).start();
        }

        sourceThread.start();

        try
        {
            sourceThread.join();

            for (int i = 0 ; i < recvrThreadList.size() ; i++)
            {
                recvrThreadList.get(i).join();
            }

            for (int i = 0 ; i < forwardThreadList.size() ; i++)
            {
                forwardThreadList.get(i).join();
            }
        }
        catch (InterruptedException e)
        {
            System.out.println (e.getMessage());
        }

        System.out.println ("PACKET TRACING:\n");

        for (int i = 0 ; i < forwarderList.size() ; i++)
        {
            if (forwarderList.get(i).getApartOfAddresses().isEmpty() != true)
            {
                forwarderList.get(i).getOut();
            }

        }

        for (int i = 0 ; i < recvrList.size() ; i++)
        {
            recvrList.get(i).getOut();
        }
    }
}