import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowData {

    public String extractData(List<PcapNs3> listPcap,String fileName) throws IOException {
        String filePath=System.getProperty("user.dir");
        File extractData=new File(filePath+File.separator+fileName.split("\\.")[0]+".txt");
        System.out.println(extractData.getAbsolutePath());
        extractData.createNewFile();
        PrintWriter writer = new PrintWriter(extractData, StandardCharsets.UTF_8);
        Map<String,Integer> numberOfIpOrigin=new HashMap<>();
        Map<String,Integer> numberOfIpDestination=new HashMap<>();
        for(PcapNs3 pcap:listPcap)
        {
            if(numberOfIpOrigin.containsKey(pcap.ipOrigin))
            {
                Integer integer=numberOfIpOrigin.get(pcap.ipOrigin);
                numberOfIpOrigin.put(pcap.ipOrigin,++integer);
            }
            else
            {
                numberOfIpOrigin.put(pcap.ipOrigin,1);
            }
        }
        for(PcapNs3 pcap:listPcap)
        {
            if(numberOfIpDestination.containsKey(pcap.ipDestination))
            {
                Integer integer=numberOfIpDestination.get(pcap.ipDestination);
                numberOfIpDestination.put(pcap.ipDestination,++integer);
            }
            else
            {
                numberOfIpDestination.put(pcap.ipDestination,1);
            }
        }
        Map<String,Integer> mapOfPairs=new HashMap<>();
        for(PcapNs3 pcap : listPcap)
        {
            if(mapOfPairs.containsKey(pcap.ipOrigin+"->"+pcap.ipDestination+" Protocol: "+pcap.protocol))
            {
                Integer integer=mapOfPairs.get(pcap.ipOrigin+"->"+pcap.ipDestination+" Protocol: "+pcap.protocol);
                mapOfPairs.put(pcap.ipOrigin+"->"+pcap.ipDestination+" Protocol: "+pcap.protocol,++integer);
            }
            else
            {
                mapOfPairs.put(pcap.ipOrigin+"->"+pcap.ipDestination+" Protocol: "+pcap.protocol,1);
            }
        }
        writer.println("----------------Connections-----------------------");
        for(Map.Entry<String,Integer> entry : numberOfIpOrigin.entrySet())
        {
            writer.println("Ip address: "+entry.getKey()+ " has "+entry.getValue()+" packages sent.");
        }
        writer.println();
        for(Map.Entry<String,Integer> entry : numberOfIpDestination.entrySet())
        {
            writer.println("Ip address: "+entry.getKey()+ " received "+entry.getValue()+" packages. ");
        }
        writer.println();
        for(Map.Entry<String,Integer> entry: mapOfPairs.entrySet())
        {
            writer.println(entry.getKey()+ " Count: "+entry.getValue());
        }
        writer.println("--------------------------------------------------");

        List<PcapNs3> errorPackages = getErrorPackages(listPcap);


        HashMap<String,Integer> protocols=protocolMap(listPcap);
        Integer howMany=0;
        String protocolName="";
        for(Map.Entry<String,Integer> entry : protocols.entrySet())
        {
            if(entry.getValue()>howMany)
            {
                protocolName=entry.getKey();
                howMany=entry.getValue();
            }
        }
        float sumExecutionTime=averageExecutionTime(listPcap);
        writer.println();
        writer.println("-----------------Protocol-------------------------");
        writer.println("The most used protocol is: "+protocolName+" with a percentage of "+(float)howMany/listPcap.size()*100.0f+"%");
        writer.println();
        writer.println("----------------Execution Time--------------------");
        writer.println("Average Execution Time " + sumExecutionTime/(listPcap.size()));
        writer.println();
        writer.println("---------------ERROR Packages---------------------");
        for (PcapNs3 singlePackage: errorPackages) {
            writer.println(singlePackage.toString());
        }
        writer.println("--------------------------------------------------");
        writer.println();
        writer.close();
        return "";
    }
    public List<PcapNs3> getErrorPackages(List<PcapNs3> list)
    {
        List<PcapNs3> errorPackages=new ArrayList<>();
        for(PcapNs3 singlePackage: list)
            if (singlePackage.protocol.contains("ICMP"))
                errorPackages.add(singlePackage);
            return errorPackages;
    }

    public float averageExecutionTime(List<PcapNs3> listPcap)
    {
        float sumExecutionTime = 0;
        for(PcapNs3 singlePackage: listPcap) {
                sumExecutionTime += singlePackage.timeOfExecution;
        }
        return sumExecutionTime;
    }

    public HashMap<String,Integer> protocolMap(List<PcapNs3> listPcap)
    {
        HashMap<String,Integer> protocols=new HashMap<>();
        for(PcapNs3 pcapEntry: listPcap)
        {
            if(protocols.containsKey(pcapEntry.protocol))
            {
                Integer integer=protocols.get(pcapEntry.protocol);
                protocols.put(pcapEntry.protocol,++integer);
            }
            else
            {
                protocols.put(pcapEntry.protocol,1);
            }
        }
        return protocols;
    }

    public String compare(List<PcapNs3> firstList,List<PcapNs3> secondList,String fileName) throws IOException {
        String filePath=System.getProperty("user.dir");

        File extractData=new File(filePath+File.separator+"Compare-"+fileName.split(";")[0].split("\\.")[0]+"_"+fileName.split(";")[1].split("\\.")[0]+".txt");
        extractData.createNewFile();
        PrintWriter writer = new PrintWriter(extractData, StandardCharsets.UTF_8);
        HashMap<String,Integer> protocolsFirst=protocolMap(firstList);
        HashMap<String,Integer> protocolsSecond=protocolMap(secondList);
        Integer howMany=0;
        String protocolName="";
        for(Map.Entry<String,Integer> entry : protocolsFirst.entrySet())
        {
            if(entry.getValue()>howMany)
            {
                protocolName=entry.getKey();
                howMany=entry.getValue();
            }
        }
        Integer howMany1=0;
        String protocolName1="";
        for(Map.Entry<String,Integer> entry : protocolsSecond.entrySet())
        {
            if(entry.getValue()>howMany1)
            {
                protocolName1=entry.getKey();
                howMany1=entry.getValue();
            }
        }
        if(!protocolName.equals(protocolName1))
        {
            writer.println("The majority of the packages are not using the same protocol so you should not compare this data.");
            writer.close();
            return "";
        }
        else
        {
            float percentageOfFirst=(float)howMany/firstList.size()*100.0f;
            float percentageOfSecond=(float)howMany1/secondList.size()*100.0f;
            if(percentageOfFirst==percentageOfSecond)
            {
                writer.println("The two files use the protocol "+protocolName+" with the same percentage("+percentageOfFirst+"%).");
            }
            else if(percentageOfFirst>percentageOfSecond)
            {
                writer.println("The protocol "+protocolName+" is more used in the first file with a difference of "+(percentageOfFirst-percentageOfSecond)+"%.");
            }
            else
            {
                writer.println("The protocol "+protocolName+" is more used in the first file with a difference of "+(percentageOfSecond-percentageOfFirst)+"%.");

            }
        }
        writer.println("------------------------------------------------------");
        float averageExecutionTimeFirstList=(averageExecutionTime(firstList)/firstList.size())*100.0f;
        float averageExecutionTimeSecondList=(averageExecutionTime(secondList)/secondList.size())*100.0f;
        if(averageExecutionTimeFirstList==averageExecutionTimeSecondList)
        {
            writer.println("The two files have the exact same execution time.");
        }
        if(averageExecutionTimeFirstList<averageExecutionTimeSecondList)
            writer.println("The average execution time of the first .pcap file is faster by "+
                    ((averageExecutionTimeSecondList-averageExecutionTimeFirstList)/averageExecutionTimeSecondList)*100.0f+"%");
        else
            writer.println("The average execution time of the second .pcap file is faster by "+
                    ((averageExecutionTimeFirstList-averageExecutionTimeSecondList)/averageExecutionTimeFirstList)*100.0f+"%");
        writer.println("------------------------------------------------------");
        List<PcapNs3> errorPackagesFirst = getErrorPackages(firstList);
        List<PcapNs3> errorPackagesSecond = getErrorPackages(secondList);
        if(errorPackagesFirst!=null)
            writer.println("The first file has "+errorPackagesFirst.size()+" error packages with the protocol "+
                errorPackagesFirst.get(0).protocol);
        if(errorPackagesSecond!=null)
            writer.println("The second file has "+errorPackagesSecond.size()+" error packages with the protocol "+
                errorPackagesSecond.get(0).protocol);
        if(errorPackagesFirst!=null&&errorPackagesSecond!=null)
        {
            if(errorPackagesFirst.size()==errorPackagesSecond.size())
            {
                writer.println("They have the same number of error packages.");
            }
            else if(errorPackagesFirst.size()>errorPackagesSecond.size())
            {
                writer.println("The first data file has more errors than the second.");
            }
            else
            {
                writer.println("The second data file has more errors than the first.");
            }
            if((float)errorPackagesFirst.size()/firstList.size()==(float)errorPackagesSecond.size()/secondList.size())
            {
                writer.println("They additionally have the same percentage error rate which is: "+(float)errorPackagesFirst.size()/firstList.size()*100.0f+"%.");
            }
            else if((float)errorPackagesFirst.size()/firstList.size()>(float)errorPackagesSecond.size()/secondList.size())
            {
                writer.println("The first data file has a higher error rate which is: "+(float)errorPackagesFirst.size()/firstList.size()*100.0f+"%.");
            }
            else
                writer.println("The second data file has a higher error rate which is: "+(float)errorPackagesSecond.size()/secondList.size()*100.0f+"%.");
        }
        writer.println("------------------------------------------------------");
        writer.close();
        return "";
    }

}
