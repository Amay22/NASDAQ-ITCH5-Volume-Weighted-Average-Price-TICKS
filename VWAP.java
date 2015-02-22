import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VWAP {

    public static class VWAPData {

        double price;
        int volume;

        public VWAPData(double price, int volume) {
            this.price = price;
            this.volume = volume;
        }

        @Override
        public String toString() {
            return (this.price + " " + this.volume);
        }
    }

    public static class VWAPDataPrice {

        double price;
        String name;

        public VWAPDataPrice(double price, String name) {
            this.price = price;
            this.name = name;
        }
    }
    static HashMap<String, ArrayList<VWAPData>> C = new HashMap<>();

    public static void main(String args[]) throws IOException, InterruptedException {
        HashMap<Long, VWAPDataPrice> A = new HashMap<>();
        InputStream input = new FileInputStream(new File("06022014.NASDAQ_ITCH50"));
        int count = 1;
        while (input.read() != -1) {           
            
            int payLength = input.read();
            byte[] payBytes = new byte[payLength];
            input.read(payBytes);
            String str = new String(payBytes);
             if (count % 5000000 == 0) {   
                System.out.println(count);                 
            }
            count++;
            switch (str.charAt(0)) {
                case 'A':
                case 'F':                    
                    if ((char) payBytes[19] == 'B') {
                        A.put((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong()),
                                new VWAPDataPrice(((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 32, 36)).getInt()) / 10000),
                                        ("" + ((char) payBytes[24]) + "" + ((char) payBytes[25])
                                        + "" + ((char) payBytes[26]) + "" + ((char) payBytes[27])
                                        + "" + ((char) payBytes[28]) + "" + ((char) payBytes[29])
                                        + "" + ((char) payBytes[30]) + "" + ((char) payBytes[31])).trim()));
                    }
                    break;
                case 'U':
                    if (A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())) != null) {
                        A.put((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 19, 27)).getLong()),
                                new VWAPDataPrice(((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 31, 35)).getInt()) / 10000),
                                        A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())).name));
                        A.remove(ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong());
                    }
                    break;
                case 'E':
                    if (A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())) != null) {// && (char) payBytes[31] == 'Y') {
                        ArrayList<VWAPData> temp;
                        if (C.get(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())).name) == null) {
                            temp = new ArrayList<>();
                        } else {
                            temp = C.get(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())).name);
                        }
                        temp.add(new VWAPData(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())).price,
                                (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 19, 23)).getInt())));
                        C.put(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())).name, temp);
                        A.remove((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong()));
                    }
                    break;
                case 'C':
                    if (A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())) != null && (char) payBytes[31] == 'Y') {
                        ArrayList<VWAPData> temp;
                        if (C.get(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())).name) == null) {
                            temp = new ArrayList<>();
                        } else {
                            temp = C.get(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())).name);
                        }
                        temp.add(new VWAPData(((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 32, 36)).getInt()) / 10000),
                                (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 19, 23)).getInt())));
                        C.put(A.get((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong())).name, temp);
                        A.remove((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong()));
                    }
                    break;
                case 'P':                    
                    ArrayList<VWAPData> temp;
                    String stock_name = ("" + ((char) payBytes[24]) + "" + ((char) payBytes[25])
                            + "" + ((char) payBytes[26]) + "" + ((char) payBytes[27])
                            + "" + ((char) payBytes[28]) + "" + ((char) payBytes[29])
                            + "" + ((char) payBytes[30]) + "" + ((char) payBytes[31])).trim();
                    if (C.get(stock_name) != null) {
                        temp = C.get(stock_name);
                    } else {
                        temp = new ArrayList<>();
                    }
                    temp.add(new VWAPData(
                            (((double) (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 32, 36)).getInt())) / 10000),
                            (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 20, 24)).getInt())));
                    C.put(stock_name, temp);                    
                    break;
<<<<<<< HEAD
                case 'Q':                    
                    ArrayList<VWAPData> tempQ;
                    String stock_nameQ = ("" + ((char) payBytes[19]) + "" + ((char) payBytes[20])
                            + "" + ((char) payBytes[21]) + "" + ((char) payBytes[22])
                            + "" + ((char) payBytes[23]) + "" + ((char) payBytes[24])
                            + "" + ((char) payBytes[25]) + "" + ((char) payBytes[26])).trim();
                    if (C.get(stock_nameQ) != null) {
                        tempQ = C.get(stock_nameQ);
                    } else {
                        tempQ = new ArrayList<>();
                    }
                    tempQ.add(new VWAPData(
                            (((double) (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 27, 31)).getInt())) / 10000),
                            (ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getInt())));
                    C.put(stock_nameQ, tempQ);                                            
                    break;
=======
>>>>>>> 9d12a2abac3a1bd1efddccb25ac3888a8df2e19f
                case 'D':
                    A.remove((ByteBuffer.wrap(Arrays.copyOfRange(payBytes, 11, 19)).getLong()));
                    break;
            }
        }
        double cummulative_price, vol_avg;
        long cummulative_volume;     
        for (Map.Entry<String, ArrayList<VWAPData>> entrySet : C.entrySet()) {
                String key;
            key = entrySet.getKey().replace("*", "").replace("^", "").replace("#", "").replace("+", "");
                ArrayList<VWAPData> value = entrySet.getValue();
                cummulative_price = 0;
                vol_avg = 0;
                cummulative_volume = 0;
                int i = 0;      
        FileWriter fw = new FileWriter("stocks\\"+key+".html", true);
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("<html><body>");     
               bw.write("<BR><BOLD>" + key + "</BOLD><BR><table border=\"1\"><tr><td>Tick</td><td>Price</td><td>Volume</td><td></td><td>&#8721(volume)</td>"
                        + "<td>&#8721(volume x Price)</td><td></td><td>Price</td><td>Volume Weighted Average Price</td></tr>");
                for (Iterator<VWAPData> iterator = value.iterator(); iterator.hasNext();) {
                    i++;
                    VWAPData next = iterator.next();
                    cummulative_price += next.volume * next.price;
                    cummulative_volume += next.volume;
                    vol_avg = cummulative_price / cummulative_volume;
                    bw.write("<tr><td>" + i + "</td><td>" + next.price + "</td><td>" + next.volume + "</td><td>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</td>"
                            + "<td>" + cummulative_volume + "</td><td>" + cummulative_price + "</td><td>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</td><td>"
                            + next.price + "</td><td>" + vol_avg + "</td></tr>");
                }
                bw.write("</table><BR>");
                 bw.write("<body><html>");                
            }           
        }
        System.out.println((count - 1) + " Done");
    }
}
