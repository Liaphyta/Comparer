
public class PcapNs3 {
    int number;
    float timeOfExecution;
    String ipOrigin;
    String ipDestination;
    String protocol;
    int byteLength;
    String info;
    public PcapNs3(int number,float timeOfExecution,String ipOrigin,String ipDestination,
                   String protocol,int byteLength, String info)
    {
        this.number=number;
        this.timeOfExecution=timeOfExecution;
        this.ipOrigin=ipOrigin;
        this.ipDestination=ipDestination;
        this.protocol=protocol;
        this.byteLength=byteLength;
        this.info=info;
    }

    @Override
    public String toString() {
        return "PcapNs3{" +
                "number=" + number +
                ", timeOfExecution=" + timeOfExecution +
                ", ipOrigin='" + ipOrigin + '\'' +
                ", ipDestination='" + ipDestination + '\'' +
                ", protocol='" + protocol + '\'' +
                ", byteLength=" + byteLength +
                ", info='" + info + '\'' +
                '}';
    }
}
