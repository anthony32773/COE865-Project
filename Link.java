public class Link
{
    private int cost;
    private int end1;
    private int end2;
    private int end1Port;
    private int end2Port;

    public Link (int cost, int end1, int end2, int end1Port, int end2Port)
    {
        this.cost = cost;
        this.end1 = end1;
        this.end2 = end2;
        this.end1Port = end1Port;
        this.end2Port = end2Port;
    }

    public int getCost()
    {
        return cost;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public int getEnd1()
    {
        return end1;
    }

    public void setEnd1(int end1)
    {
        this.end1 = end1;
    }

    public int getEnd2()
    {
        return end2;
    }

    public void setEnd2(int end2)
    {
        this.end2 = end2;
    }

    public int getEnd1Port()
    {
        return end1Port;
    }

    public void setEnd1Port(int end1Port)
    {
        this.end1Port = end1Port;
    }

    public int getEnd2Port()
    {
        return end2Port;
    }

    public void setEnd2Port(int end2Port)
    {
        this.end2Port = end2Port;
    }

    public String toString()
    {
        return "Link End 1: " + this.getEnd1() + "\nLink End 1 Port: " + this.getEnd1Port() + "\nLink End 2: " + this.getEnd2() + "\nLink End 2 Port: " + this.getEnd2Port() + "\nCost: " + this.getCost();
    }
}
