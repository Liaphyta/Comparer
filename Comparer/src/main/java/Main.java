
import java.io.*;
import java.util.ArrayList;
import java.util.List;



public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(System.in));
        List<PcapNs3> firstFileData=new ArrayList<>();
        List<PcapNs3> secondFileData=new ArrayList<>();
        String filePath="";
        String filePath2="";
        File firstFile=null;
        File secondFile=null;
        try {
            System.out.println("Please insert path of the first file.");
            firstFile = new File(bufferedReader.readLine());
            filePath=firstFile.getAbsolutePath();
            System.out.println("Please insert path of the second file.");
            secondFile = new File(bufferedReader.readLine());
            filePath2=secondFile.getAbsolutePath();
            BufferedReader readerCSV= new BufferedReader(new FileReader(firstFile));
            String line;
            boolean firstLine=false;
            float beforeExecutionTime=0.0f;
            while((line=readerCSV.readLine())!=null)
            {
                if(!firstLine)
                {
                    firstLine=true;
                }
                else
                {
                    String[] fields=line.split(",");
                    for(int i=0;i<fields.length;i++)
                    {
                        System.out.println(fields[i]);
                        fields[i] = fields[i].replaceAll("^\"+|\"+$", "");
                        System.out.println(fields[i]);
                    }
                    PcapNs3 pcap=new PcapNs3(Integer.parseInt(fields[0].replaceAll("\"","")),
                            Float.parseFloat(fields[1].replaceAll("\"",""))-beforeExecutionTime
                            ,fields[2],fields[3],fields[4],
                            Integer.parseInt(fields[5].replaceAll("\"","")),fields[6]);
                    beforeExecutionTime=Float.parseFloat(fields[1].replaceAll("\"",""));

                    firstFileData.add(pcap);
                }
            }
            readerCSV=new BufferedReader(new FileReader(secondFile));
            firstLine=false;
            beforeExecutionTime=0.0f;
            while((line=readerCSV.readLine())!=null)
            {
                if(!firstLine)
                {
                    firstLine=true;
                }
                else
                {
                    String[] fields=line.split(",");
                    for(int i=0;i<fields.length;i++)
                    {
                        System.out.println(fields[i]);
                        fields[i] = fields[i].replaceAll("^\"+|\"+$", "");
                        System.out.println(fields[i]);
                    }
                    PcapNs3 pcap=new PcapNs3(Integer.parseInt(fields[0].replaceAll("\"","")),
                            Float.parseFloat(fields[1].replaceAll("\"",""))-beforeExecutionTime
                            ,fields[2],fields[3],fields[4],
                            Integer.parseInt(fields[5].replaceAll("\"","")),fields[6]);
                    beforeExecutionTime=Float.parseFloat(fields[1].replaceAll("\"",""));

                    secondFileData.add(pcap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ShowData extractData=new ShowData();
        System.out.println(firstFile.getName());
        extractData.extractData(firstFileData,firstFile.getName());
        extractData.extractData(secondFileData,secondFile.getName());
        extractData.compare(firstFileData,secondFileData,firstFile.getName()+";"+secondFile.getName());
    }
}
